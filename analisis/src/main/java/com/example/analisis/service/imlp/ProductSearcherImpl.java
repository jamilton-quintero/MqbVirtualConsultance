package com.example.analisis.service.imlp;

import com.example.analisis.domain.entity.dto.ProductDto;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.example.analisis.service.ProductSearcher;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.QueryParserBase;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.stereotype.Service;

@Service
public class ProductSearcherImpl implements ProductSearcher {

	private static final String DESCRIPTION_ATTRIBUTE = "description";
	private static final String TAG_ATTRIBUTE = "tags";
	private static final String PRODUCT_ID_ATTRIBUTE = "productId";
	private static final String NAME_ATTRIBUTE = "name";
	private static final int TOP_PRODUCTS = 10;
	public static final String BRANDS_ATRIBUTE = "brands";

	private final IndexSearcher searcher;
	private final QueryParser queryParser;
	private final QueryParser queryParserTags;

	public ProductSearcherImpl(String indexDirectoryPath) throws IOException {
		Directory indexDirectory = FSDirectory.open(Paths.get(indexDirectoryPath));
		IndexReader indexReader = DirectoryReader.open(indexDirectory);
		searcher = new IndexSearcher(indexReader);
		Analyzer analyzer = new StandardAnalyzer();
		queryParser = new QueryParser(DESCRIPTION_ATTRIBUTE, analyzer);
		queryParserTags = new QueryParser(TAG_ATTRIBUTE, analyzer);
	}

	public List<ProductDto> relevantProducts(String queryStringProblema, String queryStringSolucion) {

		Query problemaQuery = getQuery(queryStringProblema,queryParser);
		Query problemaQueryTags = getQuery(queryStringProblema,queryParserTags);
		Query solucionQuery = getQuery(queryStringSolucion,queryParser);
		Query solucionQueryTags = getQuery(queryStringProblema,queryParserTags);
		Query query = new BooleanQuery.Builder()
				.add(problemaQuery, BooleanClause.Occur.MUST)
				.add(problemaQueryTags, BooleanClause.Occur.SHOULD)
				.add(solucionQuery, BooleanClause.Occur.SHOULD)
				.add(solucionQueryTags, BooleanClause.Occur.SHOULD)
				.build();

		TopDocs topDocs = getTopDocuments(query);
		ScoreDoc[] scoreDocs = topDocs.scoreDocs;

		List<ProductDto> products = getTopProductsInLucene(scoreDocs);
		float relevanceThreshold = topDocs.scoreDocs[0].score * 0.7f;

		return products.stream()
				.filter(product -> product.getScore() >= relevanceThreshold)
				.collect(Collectors.toList());
	}

	private List<ProductDto> getTopProductsInLucene(ScoreDoc[] scoreDocs) {

		List<ProductDto> products = new LinkedList<>();

		Map<String, Integer> map = new HashMap<>();

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
				String mainBrand = doc.get(BRANDS_ATRIBUTE);

				Integer totManinsBrands = map.get(mainBrand);

				if (totManinsBrands == null){
					map.put(mainBrand,1);
				} else {
					map.replace(mainBrand, totManinsBrands+1);
				}

				products.add(new ProductDto(Integer.valueOf(id), name, description, scoreDoc.score, mainBrand));
			}
		}

		String topBrand = getTopBrand(map);

		products.removeIf(pd -> pd == null || !Objects.equals(pd.getMainBrand(), topBrand));

		return products;
	}

	private static String getTopBrand(Map<String, Integer> map) {
		String topBrand = null;
		Integer countMainBRand = 0;

		for (Map.Entry<String, Integer> element : map.entrySet()){
			Integer totBrands = element.getValue();
			String brand = element.getKey();

			if(totBrands > countMainBRand){
				countMainBRand = totBrands;
				topBrand = brand;
			}
		}
		return topBrand;
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

