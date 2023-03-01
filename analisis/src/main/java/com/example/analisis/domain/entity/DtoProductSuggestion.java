package com.example.analisis.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DtoProductSuggestion {

    private String board;
    private List<DtoProduct> products;
    private String audioUrl;
    private byte[] voice;

}
