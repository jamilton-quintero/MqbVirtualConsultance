package com.example.analisis.domain.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductSuggestionDto {

    private String board;
    private List<ProductDto> products;
    private String audioUrl;
    private byte[] voice;

}
