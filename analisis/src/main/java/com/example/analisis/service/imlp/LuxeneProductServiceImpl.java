package com.example.analisis.service.imlp;

import com.example.analisis.domain.entity.dto.ProductDto;
import com.example.analisis.domain.entity.dto.SuggestionFromOpenIAResponseDto;
import com.example.analisis.service.LuxeneProductService;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.example.analisis.service.ProductSearcher;
import org.springframework.stereotype.Service;

import static com.example.analisis.util.Filter.cleanTextOfSpecialCharacters;

@Service
public class LuxeneProductServiceImpl implements LuxeneProductService {


	private final ProductSearcher productSearcher;


	public LuxeneProductServiceImpl(ProductSearcher productSearcher) {
		this.productSearcher = productSearcher;
	}

	@Override
	public List<Integer> searchBestProductsAcordingToSuggestion(String problem, SuggestionFromOpenIAResponseDto suggestion) {

		problem = cleanTextOfSpecialCharacters(problem);

		/*String[] words = suggestion.split("\\s+");
		Set<String> uniqueWords = new HashSet<>(Arrays.asList(words));
		String cleanedSuggestion = String.join(" ", uniqueWords);*/

		return productSearcher.relevantProducts(problem, suggestion)
				.stream()
				.map(ProductDto::getId)
				.collect(Collectors.toList());
	}

}

