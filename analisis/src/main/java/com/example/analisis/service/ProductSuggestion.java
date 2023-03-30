package com.example.analisis.service;

import com.example.analisis.domain.entity.dto.SuggestionClientRequestDto;
import com.example.analisis.domain.entity.dto.SuggestionClientResponseDto;

public interface ProductSuggestion {

    SuggestionClientResponseDto getBestProductIdsAndSuggestion(SuggestionClientRequestDto boardClientRequest);

}
