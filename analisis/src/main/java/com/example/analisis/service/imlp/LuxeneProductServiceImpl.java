package com.example.analisis.service.imlp;

import com.example.analisis.domain.entity.DtoProduct;
import com.example.analisis.service.LuxeneProductService;

import java.util.List;
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
	public List<Integer> searchBestProductsAcordingToSuggestion(String problem) {

		problem = cleanTextOfSpecialCharacters(problem);

		return productSearcher.relevantProducts(problem)
				.stream()
				.map(DtoProduct::getId)
				.collect(Collectors.toList());
	}

}

