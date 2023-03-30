package com.example.analisis.domain.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SuggestionFromOpenIAResponseDto {

    private String clientSuggestionRoutine;
    private String clientSuggestionComponents;
    private String mainBrand;

}
