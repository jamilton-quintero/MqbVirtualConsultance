package com.example.analisis.entidad;

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
