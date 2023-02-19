package com.example.analisis.entidad;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class BoardClientResponseDto {

    private String clientBoard;
    private String audioUrl;
    private List<Integer> productIdsRecommended;

}
