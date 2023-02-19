package com.example.analisis.servicio.imlp;

import com.example.analisis.entidad.DtoProduct;
import com.example.analisis.servicio.LuxeneProductService;
import com.example.analisis.servicio.ProductSearcher;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.stereotype.Service;

@Service
public class LuxeneProductServiceImpl implements LuxeneProductService {


	private ProductSearcher productSearcher;

	public LuxeneProductServiceImpl(ProductSearcher productSearcher) {
		this.productSearcher = productSearcher;
	}


	@Override
	public List<DtoProduct> searchWithoutStanford(String problem) throws IOException, ParseException {
		problem = cleanTextByNumbers(problem);
//		List<String> splitProblem = getUnicWords(problem);
//
//		String queryString = String.join(" ", splitProblem);
//		queryString = cleanTextByNumbers(queryString);
		return productSearcher.search(problem);
	}

	@Override
	public List<Integer> searchBest(String problem) {

		problem = cleanTextByNumbers(problem);
		List<String> splitProblem = getUnicWords(problem);

		String queryString = String.join(" ", splitProblem);
		queryString = cleanTextByNumbers(queryString);

		return productSearcher.search(queryString)
				.stream()
				.map(DtoProduct::getId)
				.collect(Collectors.toList());
	}

	private static List<String> getUnicWords(String problem) {
		List<String> splitProblem = Arrays.stream(problem.split(" ")).collect(Collectors.toList());
		return new ArrayList<>(new HashSet<>(splitProblem));
	}

	private static String cleanTextByNumbers(String problem) {
		problem = problem.replaceAll("<[^>]*>|\\r|\\n|\\t|\n|\r|\t|([0-9].)", "");
		return problem;
	}

}

