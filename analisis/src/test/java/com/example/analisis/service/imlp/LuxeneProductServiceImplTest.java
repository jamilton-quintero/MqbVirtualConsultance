package com.example.analisis.service.imlp;

import com.example.analisis.domain.entity.DtoProduct;

import java.util.ArrayList;
import java.util.List;

import com.example.analisis.service.ProductSearcher;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class LuxeneProductServiceImplTest {

    private static ProductSearcher productSearcher;
    private static LuxeneProductServiceImpl luxeneProductService;

    @BeforeAll
    public static void setUp() {
        productSearcher = Mockito.mock(ProductSearcher.class);
        luxeneProductService = new LuxeneProductServiceImpl(productSearcher);
    }

    @Test
    void testSearchBest() {
        String problem = "";

        List<DtoProduct> products = new ArrayList<>();
        DtoProduct product = new DtoProduct(123,"","", 100F);
        products.add(product);

        Mockito.when(productSearcher.relevantProducts(Mockito.anyString())).thenReturn(products);

        List<Integer> actualSearchBestResult = luxeneProductService.searchBestProductsAcordingToSuggestion(problem);

        Assertions.assertEquals(1, actualSearchBestResult.size());
        Assertions.assertEquals(123, actualSearchBestResult.get(0));
        Mockito.verify(productSearcher).relevantProducts(Mockito.anyString());

    }
}

