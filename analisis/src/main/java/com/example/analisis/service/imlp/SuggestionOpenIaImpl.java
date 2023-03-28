package com.example.analisis.service.imlp;

import com.example.analisis.domain.entity.ChatGptResponse;
import com.example.analisis.domain.entity.dto.SuggestionClientRequestDto;
import com.example.analisis.domain.entity.dto.SuggestionFromOpenIAResponseDto;
import com.example.analisis.service.BotService;
import com.example.analisis.service.SuggestionOpenIa;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class SuggestionOpenIaImpl implements SuggestionOpenIa {

    public static final String PREFIX_MESSAGE = "(Generar Recomendacion enfocada en productos de belleza y cuidado personal sin mencionar marcas especificas.Esta recomendacion debe estar dada para para una %s de %s años, la recomendacion debe ser muy optimizada y detallada para mejorar la situacion mencioanda)";
    public static final String BASE_PROMPT_MAIN_BRAND = "De estas opciones donde solo es posible una respuesta, A=Cuidado capilar,B=Cuidado corporal, C=Cuidado facial. P:Tengo mucha caspa y el cabello muy grasoso :A, P:Tengo muchos granos en la cara :C, P:Tengo muchos granos en la espalda :B, P:Tengo muchos granos en la cabeza :A, P:Se me cae mucho el pelo :A, P:%s:";
    public static final String BASE_PROMPT_SUGGESTION_COMPONENTS = "Como experta en %s, ¿podrías recomendarme algunos productos y componentes naturales o químicos que sean efectivos para tratar el siguiente problema: %s de %s de %s años? Preferiblemente, evitando mencionar marcas específicas pero proporcionando información detallada sobre los ingredientes activos que son beneficiosos para tratar este problema.";
    public static final String BASE_PROMPT_SUGGESTION_ROUTINE = "Como experta en %s, ¿podrías recomendarme tips o una rutina diaria, semanal o mensual de %s para tratar el siguiente problema: %s de %s de %s años? Por favor, proporciona información detallada del paso a paso a seguir para mejorar esta situación, evitando mencionar productos y marcas específicas.";

    private final BotService botService;

    public SuggestionOpenIaImpl(BotService botService) {
        this.botService = botService;
    }

    @Override
    public SuggestionFromOpenIAResponseDto getSuggestion(SuggestionClientRequestDto boardClientRequest){

        String brand = getMainBrand(boardClientRequest);

        CompletableFuture<String> futureSuggestionRoutine = CompletableFuture.supplyAsync(() -> getSuggestionSuggestionRoutine(boardClientRequest, brand));

        CompletableFuture<String> futureSuggestionComponent = CompletableFuture.supplyAsync(() -> getSuggestionComponents(boardClientRequest, brand));

        CompletableFuture<SuggestionFromOpenIAResponseDto> combinedFuture = futureSuggestionRoutine.thenCombine(futureSuggestionComponent, (routine, components) -> new SuggestionFromOpenIAResponseDto(routine, components, brand));

        return combinedFuture.join();
    }

    private String getMainBrand(SuggestionClientRequestDto boardClientRequest) {
        String message = String.format(BASE_PROMPT_MAIN_BRAND, boardClientRequest.getMessage());
        ChatGptResponse response = botService.getSuggestion(message);
        return getMainBrand(response.getChoices().get(0).getText());
    }

    private String getSuggestionComponents(SuggestionClientRequestDto boardClientRequest, String brand) {
        String message = getSuggestionComponentsOnBaseAgeAndGender(boardClientRequest, brand);
        ChatGptResponse response = botService.getSuggestion(message);
        return response.getChoices().get(0).getText();
    }

    private String getSuggestionComponentsOnBaseAgeAndGender(SuggestionClientRequestDto boardClientRequest, String brand) {
        return String.format(BASE_PROMPT_SUGGESTION_COMPONENTS,brand, boardClientRequest.getMessage(),getGender(boardClientRequest.getClientGender()), boardClientRequest.getClientAge());

    }

    private String getSuggestionSuggestionRoutine(SuggestionClientRequestDto boardClientRequest, String brand) {
        String message = getSuggestionRoutineOnBaseAgeAndGender(boardClientRequest, brand);
        ChatGptResponse response = botService.getSuggestion(message);
        return response.getChoices().get(0).getText();
    }

    private String getSuggestionRoutineOnBaseAgeAndGender(SuggestionClientRequestDto boardClientRequest, String brand) {
        return String.format(BASE_PROMPT_SUGGESTION_ROUTINE,brand, brand, boardClientRequest.getMessage(),getGender(boardClientRequest.getClientGender()), boardClientRequest.getClientAge());
    }

    private static String getMainBrand(String brand) {
        switch (brand){
            case "A":
                return "cuidado capilar";
            case "B":
                return "cuidado corporal";
            case "C":
                return "cuidado facial";
            default:
                return "cuidado de la belleza";
        }
    }


    private String getGender(char typeGender){
        return typeGender == 'M' ? "un hombre" : "una mujer";

    }

}
