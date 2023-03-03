package com.example.analisis.service;

import java.util.List;

public interface LuxeneProductService {

    List<Integer> searchBestProductsAcordingToSuggestion(String problem, String suggestion);
}
