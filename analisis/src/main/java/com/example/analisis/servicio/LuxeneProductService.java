package com.example.analisis.servicio;

import com.example.analisis.entidad.DtoProduct;
import org.apache.lucene.queryparser.classic.ParseException;

import java.io.IOException;
import java.util.List;

public interface LuxeneProductService {

    List<DtoProduct> searchWithoutStanford(String problem) throws IOException, ParseException;

    List<Integer> searchBest(String problem);
}
