package com.example.analisis.service;

import com.example.analisis.domain.entity.SuggestionClientRequestDto;
import com.example.analisis.domain.entity.SuggestionClientResponseDto;

public interface ProductSuggestion {

    SuggestionClientResponseDto getBestProductIdsAndSuggestion(SuggestionClientRequestDto boardClientRequest);

}
