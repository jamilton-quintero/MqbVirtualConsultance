package com.example.analisis.service.imlp;

import com.example.analisis.domain.entity.dto.SuggestionClientRequestDto;
import com.example.analisis.domain.entity.dto.SuggestionClientResponseDto;
import com.example.analisis.domain.entity.ChatGptResponse;
import com.example.analisis.service.AudioGeneratorService;
import com.example.analisis.service.AudioStorage;
import com.example.analisis.service.BotService;
import com.example.analisis.service.LuxeneProductService;
import com.example.analisis.service.ProductSuggestion;

import org.springframework.stereotype.Component;
import java.io.InputStream;
import java.util.List;

import static com.example.analisis.util.Filter.cleanTextOfSpecialCharacters;

@Component
public class ProductSuggestionImpl implements ProductSuggestion {

    public static final String PREFIX_MESSAGE = "(Generar Recomendacion enfocada en productos de belleza y cuidado personal sin mencionar marcas especificas.Esta recomendacion debe estar dada para para una %s de %s años, la recomendacion debe ser muy optimizada y detallada para mejorar la situacion mencioanda)";
    public static final String MESSAGE_WHEN_TAKE_PRODUCTS = " Te dejaremos estos productos como recomendacion a continuación";
    public static final String MESSAGE_WHEN_DONT_TAKE_PRODUCTS = "No tenemos productos para recomendarte pero podemos darte el siguiente consejo: ";
    private final LuxeneProductService luxeneProductService;
    private final BotService botService;
    private final AudioStorage audioStorage;
    private final AudioGeneratorService audioGeneratorService;
    public ProductSuggestionImpl(LuxeneProductServiceImpl luxeneProductService, BotService botService, AudioStorage audioStorage, AudioGeneratorService audioGeneratorService) {
        this.luxeneProductService = luxeneProductService;
        this.botService = botService;
        this.audioStorage = audioStorage;
        this.audioGeneratorService = audioGeneratorService;
    }

    @Override
    public SuggestionClientResponseDto getBestProductIdsAndSuggestion(SuggestionClientRequestDto request) {

        String suggestion = getBoarFromOpenIa(request);

        suggestion = cleanTextOfSpecialCharacters(suggestion);

        List<Integer> productIds = luxeneProductService.searchBestProductsAcordingToSuggestion(request.getMessage(),suggestion);

        InputStream audioSuggestion = audioGeneratorService.generateAudioSuggestion(suggestion);

        String audioUrl = audioStorage.saveAudio(audioSuggestion);

        String finalSuggestion = "";

        if(!productIds.isEmpty()){
            finalSuggestion = suggestion.concat(MESSAGE_WHEN_TAKE_PRODUCTS);
        } else {
            finalSuggestion = MESSAGE_WHEN_DONT_TAKE_PRODUCTS.concat(suggestion);
        }

        return new SuggestionClientResponseDto(finalSuggestion, audioUrl, productIds);

    }

    private String getBoarFromOpenIa(SuggestionClientRequestDto boardClientRequest) {
        String message = getSuggestionOnBaseAgeAndGender(boardClientRequest);
        ChatGptResponse response = botService.getSuggestion(message);
        return response.getChoices().get(0).getText();
    }

    private String getSuggestionOnBaseAgeAndGender(SuggestionClientRequestDto boardClientRequest) {
        return
                boardClientRequest.getMessage().concat(String.format(PREFIX_MESSAGE,
                getGender(boardClientRequest.getClientGender()),
                boardClientRequest.getClientAge())).concat(" Que me pueeds recomendar?");
    }

    private String getGender(char typeGender){
        return typeGender == 'M' ? "Hombre" : "Mujer";

    }

}
