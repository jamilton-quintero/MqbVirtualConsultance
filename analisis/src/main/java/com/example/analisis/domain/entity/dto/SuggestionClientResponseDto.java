package com.example.analisis.domain.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SuggestionClientResponseDto {

    private String clientSuggestion;
    private String audioUrl;
    private List<Integer> productIdsRecommended;

}
