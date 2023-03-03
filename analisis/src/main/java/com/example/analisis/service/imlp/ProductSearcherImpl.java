package com.example.analisis.service.imlp;

import com.example.analisis.domain.entity.dto.ProductDto;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.example.analisis.service.ProductSearcher;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.QueryParserBase;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.stereotype.Service;

@Service
public class ProductSearcherImpl implements ProductSearcher {

	private static final String DESCRIPTION_ATTRIBUTE = "description";
	private static final String PRODUCT_ID_ATTRIBUTE = "productId";
	private static final String NAME_ATTRIBUTE = "name";
	private static final int TOP_PRODUCTS = 10;

	private final IndexSearcher searcher;
	private final QueryParser queryParser;

	public ProductSearcherImpl(String indexDirectoryPath) throws IOException {
		Directory indexDirectory = FSDirectory.open(Paths.get(indexDirectoryPath));
		IndexReader indexReader = DirectoryReader.open(indexDirectory);
		searcher = new IndexSearcher(indexReader);
		Analyzer analyzer = new StandardAnalyzer();
		queryParser = new QueryParser(DESCRIPTION_ATTRIBUTE, analyzer);
	}

	public List<ProductDto> relevantProducts(String queryStringProblema, String queryStringSolucion) {

		Query problemaQuery = getQuery(queryStringProblema,queryParser);
		Query solucionQuery = getQuery(queryStringSolucion,queryParser);
		Query query = new BooleanQuery.Builder()
				.add(problemaQuery, BooleanClause.Occur.MUST)
				.add(solucionQuery, BooleanClause.Occur.SHOULD)
				.build();

		TopDocs topDocs = getTopDocuments(query);
		ScoreDoc[] scoreDocs = topDocs.scoreDocs;

		List<ProductDto> products = getTopProductsInLucene(scoreDocs);
		float relevanceThreshold = topDocs.scoreDocs[0].score * 0.8f;

		return products.stream()
				.filter(product -> product.getScore() >= relevanceThreshold)
				.collect(Collectors.toList());
	}

	private List<ProductDto> getTopProductsInLucene(ScoreDoc[] scoreDocs) {
		List<ProductDto> products = new ArrayList<>();
		for (ScoreDoc scoreDoc : scoreDocs) {
			Document doc = null;
			try {
				doc = searcher.doc(scoreDoc.doc);
			} catch (IOException e) {
				continue;
			}
			if (doc != null) {
				String id = doc.get(PRODUCT_ID_ATTRIBUTE);
				String name = doc.get(NAME_ATTRIBUTE);
				String description = doc.get(DESCRIPTION_ATTRIBUTE);
				products.add(new ProductDto(Integer.valueOf(id), name, description, scoreDoc.score));
			}

		}
		return products;
	}

	private static Query getQuery(String queryString, QueryParser queryParser) {
		Query query = null;
		try {
			query = queryParser.parse(QueryParserBase.escape(queryString));
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		return query;
	}

	private TopDocs getTopDocuments(Query query)  {
		TopDocs topDocs = null;
		try {
			topDocs = searcher.search(query, TOP_PRODUCTS);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return topDocs;
	}


}

