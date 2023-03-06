package com.example.analisis.service.imlp;

import com.example.analisis.domain.entity.dto.ProductDto;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.example.analisis.domain.entity.dto.SuggestionFromOpenIAResponseDto;
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

import static com.example.analisis.util.Filter.cleanTextOfSpecialCharacters;

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

	@Override
	public List<ProductDto> relevantProducts(String queryStringProblema, SuggestionFromOpenIAResponseDto suggestion) {

		Query query = getFinalQuery(queryStringProblema, suggestion);

		TopDocs topDocs = getTopDocuments(query);
		ScoreDoc[] scoreDocs = topDocs.scoreDocs;

		List<ProductDto> products = getTopProductsInLucene(scoreDocs, suggestion.getMainBrand());
		float relevanceThreshold = topDocs.scoreDocs[0].score * 0.5f;

		return products.stream()
				.filter(product -> product.getScore() >= relevanceThreshold)
				.collect(Collectors.toList());
	}

	private Query getFinalQuery(String queryStringProblema, SuggestionFromOpenIAResponseDto suggestion) {
		String componentSuggestion = cleanTextOfSpecialCharacters(suggestion.getClientSuggestionComponents());
		String routineSuggestion = cleanTextOfSpecialCharacters(suggestion.getClientSuggestionRoutine());

		String cleanedComponentSuggestion = getCleanedSuggestion(componentSuggestion);
		String cleanedRoutineSuggestion = getCleanedSuggestion(routineSuggestion);

		Query problemQuery = getQuery(queryStringProblema,queryParser);
		Query problemQueryTags = getQuery(queryStringProblema,queryParserTags);

		Query solucionComponentQuery = getQuery(cleanedComponentSuggestion,queryParser);

		Query solucionComponentQueryTags = getQuery(cleanedComponentSuggestion,queryParserTags);
//
//		Query solucionRoutineQuery = getQuery(cleanedRoutineSuggestion,queryParser);
//		Query solucionRoutineQueryTags = getQuery(cleanedRoutineSuggestion,queryParserTags);

		return new BooleanQuery.Builder()
				.add(problemQuery, BooleanClause.Occur.MUST)
				.add(problemQueryTags, BooleanClause.Occur.SHOULD)
				.add(solucionComponentQuery, BooleanClause.Occur.SHOULD)
				.add(solucionComponentQueryTags, BooleanClause.Occur.SHOULD)
				//.add(solucionRoutineQuery, BooleanClause.Occur.SHOULD)
				//.add(solucionRoutineQueryTags, BooleanClause.Occur.SHOULD)
				.build();
	}

	private static String getCleanedSuggestion(String suggestion) {
		String[] words = suggestion.split("\\s+");
		Set<String> uniqueWords = new HashSet<>(Arrays.asList(words));
		return String.join(" ", uniqueWords);
	}

	private List<ProductDto> getTopProductsInLucene(ScoreDoc[] scoreDocs, String topBrand) {

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

		//String topBrand = getTopBrand(map);

		products.removeIf(pd -> pd == null || !Objects.equals(pd.getMainBrand().toLowerCase(), topBrand.toLowerCase()));

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

