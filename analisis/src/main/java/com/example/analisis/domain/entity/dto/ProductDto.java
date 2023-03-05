package com.example.analisis.domain.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

    private Integer id;
    private String name;
    private String description;
    private float score;
    private String mainBrand;


}
