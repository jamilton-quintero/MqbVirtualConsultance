package com.example.analisis.service;

import com.example.analisis.domain.entity.dto.SuggestionClientRequestDto;
import com.example.analisis.domain.entity.dto.SuggestionFromOpenIAResponseDto;

public interface SuggestionOpenIa {

    SuggestionFromOpenIAResponseDto getSuggestion(SuggestionClientRequestDto boardClientRequest);

}
