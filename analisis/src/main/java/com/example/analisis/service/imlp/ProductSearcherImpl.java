package com.example.analisis.service.imlp;

import com.example.analisis.domain.entity.DtoProduct;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.example.analisis.service.ProductSearcher;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.QueryParserBase;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.stereotype.Service;

@Service
public class ProductSearcherImpl implements ProductSearcher {

	public static final String DESCRIPTION_ATRIBUTE = "description";
	public static final String PRODUCT_ID_ATRIBUTE = "productId";
	public static final String NAME_ATRIBUTE = "name";
	public static final int TOP_PRODUCTS = 10;
	private final IndexSearcher indexSearcher;

	public ProductSearcherImpl(String indexDirectoryPath) throws
													  IOException {
		Directory indexDirectory = FSDirectory.open(Paths.get(indexDirectoryPath));
		IndexReader indexReader = DirectoryReader.open(indexDirectory);
		this.indexSearcher = new IndexSearcher(indexReader);
	}

	public List<DtoProduct> relevantProducts(String queryString){

		QueryParser queryParser = new QueryParser(DESCRIPTION_ATRIBUTE, new StandardAnalyzer());
		Query query = getQuery(queryString, queryParser);
		TopDocs topDocs = getTopDocuments(query);

		ScoreDoc[] scoreDocs = topDocs.scoreDocs;

		List<DtoProduct> products = getTopProductsInLuxene(scoreDocs);

		float relevanceThreshold = topDocs.getMaxScore() * 0.9f;
		List<DtoProduct> relevantProducts = new ArrayList<>();
		for (DtoProduct product : products) {
			if (product.getScore() >= relevanceThreshold) {
				relevantProducts.add(product);
			}
		}
		return relevantProducts;
	}

	private List<DtoProduct> getTopProductsInLuxene(ScoreDoc[] scoreDocs) {

		List<DtoProduct> products = new ArrayList<>();

		for (ScoreDoc scoreDoc : scoreDocs) {

			Document doc;

			try {
				doc = indexSearcher.doc(scoreDoc.doc);
			} catch (IOException e) {
				continue;
			}

			if(doc != null){
				String id = doc.get(PRODUCT_ID_ATRIBUTE);
				String name = doc.get(NAME_ATRIBUTE);
				String description = doc.get(DESCRIPTION_ATRIBUTE);
				products.add(new DtoProduct(Integer.valueOf(id), name, description, scoreDoc.score));
			}

		}
		return products;
	}

	private static Query getQuery(String queryString, QueryParser queryParser) {
		Query query = null;
		try {
			query = queryParser.parse(QueryParserBase.escape(queryString));
		}catch (Exception e){
			System.out.printf(e.getMessage());
		}
		return query;
	}

	private TopDocs getTopDocuments(Query query) {
		TopDocs topDocs;
		try {
			topDocs = indexSearcher.search(query, TOP_PRODUCTS);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return topDocs;
	}

}

