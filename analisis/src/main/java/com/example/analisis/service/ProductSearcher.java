package com.example.analisis.service;

import com.example.analisis.domain.entity.dto.ProductDto;
import com.example.analisis.domain.entity.dto.SuggestionFromOpenIAResponseDto;

import java.util.List;

public interface ProductSearcher {

    List<ProductDto> relevantProducts(String queryStringProblema, SuggestionFromOpenIAResponseDto suggestion);

}
