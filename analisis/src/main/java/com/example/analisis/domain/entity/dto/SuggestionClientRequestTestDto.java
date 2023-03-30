package com.example.analisis.domain.entity.dto;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SuggestionClientRequestTestDto {

    @NotNull
    private SuggestionFromOpenIAResponseDto suggestion;
    @NotNull
    private String problem;

}
