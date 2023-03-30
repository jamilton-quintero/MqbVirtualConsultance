package com.example.analisis.util;

public class Filter {

    public static String cleanTextOfSpecialCharacters(String problem) {
        problem = problem.replaceAll("<[^>]*>|\\r|\\n|\\t|\n|\r|\t", "");
        return problem;
    }

}
