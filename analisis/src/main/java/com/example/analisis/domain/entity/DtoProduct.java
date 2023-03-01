package com.example.analisis.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DtoProduct {

    private Integer id;
    private String name;
    private String description;
    private float score;


}
