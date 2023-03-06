package com.example.analisis.service;

import com.example.analisis.domain.entity.dto.SuggestionFromOpenIAResponseDto;

import java.util.List;

public interface LuxeneProductService {

    List<Integer> searchBestProductsAcordingToSuggestion(String problem, SuggestionFromOpenIAResponseDto suggestion);
}
