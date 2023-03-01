package com.example.analisis.service;

import com.example.analisis.domain.entity.DtoProduct;

import java.util.List;

public interface ProductSearcher {

    List<DtoProduct> relevantProducts(String queryString);

}
