package com.example.analisis.service.imlp;

import com.example.analisis.domain.entity.ClientProblem;
import com.example.analisis.domain.entity.dto.SuggestionClientRequestDto;
import com.example.analisis.domain.entity.dto.SuggestionClientResponseDto;
import com.example.analisis.domain.entity.dto.SuggestionFromOpenIAResponseDto;
import com.example.analisis.domain.repositories.ClientProblemRepository;
import com.example.analisis.service.AudioGeneratorService;
import com.example.analisis.service.AudioStorage;
import com.example.analisis.service.LuxeneProductService;
import com.example.analisis.service.ProductSuggestion;

import com.example.analisis.service.SuggestionOpenIa;
import org.springframework.stereotype.Component;
import java.io.InputStream;
import java.util.List;

import static com.example.analisis.util.Filter.cleanTextOfSpecialCharacters;

@Component
public class ProductSuggestionImpl implements ProductSuggestion {

    public static final String MESSAGE_WHEN_TAKE_PRODUCTS = " Te dejaremos estos productos como recomendacion a continuación";
    public static final String MESSAGE_WHEN_DONT_TAKE_PRODUCTS = "No tenemos productos para recomendarte pero podemos darte el siguiente consejo: ";
    private final LuxeneProductService luxeneProductService;
    private final AudioStorage audioStorage;
    private final AudioGeneratorService audioGeneratorService;
    private final SuggestionOpenIa suggestionOpenIa;

    private final ClientProblemRepository clientProblemRepository;

    public ProductSuggestionImpl(LuxeneProductServiceImpl luxeneProductService, AudioStorage audioStorage, AudioGeneratorService audioGeneratorService, SuggestionOpenIa suggestionOpenIa, ClientProblemRepository clientProblemRepository) {
        this.luxeneProductService = luxeneProductService;
        this.audioStorage = audioStorage;
        this.audioGeneratorService = audioGeneratorService;
        this.suggestionOpenIa = suggestionOpenIa;
        this.clientProblemRepository = clientProblemRepository;
    }

    @Override
    public SuggestionClientResponseDto getBestProductIdsAndSuggestion(SuggestionClientRequestDto request) {

        SuggestionFromOpenIAResponseDto suggestion = suggestionOpenIa.getSuggestion(request);

        clientProblemRepository.save(new ClientProblem(null, request.getMessage(), request.getClientAge(),request.getClientGender(),
                suggestion.getClientSuggestionComponents(), suggestion.getClientSuggestionRoutine(), suggestion.getMainBrand()));

        List<Integer> productIds = luxeneProductService.searchBestProductsAcordingToSuggestion(request.getMessage(),suggestion);

        String finalSuggestioToClient = cleanTextOfSpecialCharacters(suggestion.getClientSuggestionRoutine());

        String finalSuggestionToAudio = getFinalSuggestion(suggestion, productIds);
        InputStream audioSuggestion = audioGeneratorService.generateAudioSuggestion(finalSuggestionToAudio);
        String audioUrl = audioStorage.saveAudio(audioSuggestion);

        return new SuggestionClientResponseDto(finalSuggestioToClient, audioUrl, productIds);

    }

    private static String getFinalSuggestion(SuggestionFromOpenIAResponseDto suggestion, List<Integer> productIds) {
        String finalSuggestion = "";

        if(!productIds.isEmpty()){
            finalSuggestion = suggestion.getClientSuggestionRoutine().concat(MESSAGE_WHEN_TAKE_PRODUCTS);
        } else {
            finalSuggestion = MESSAGE_WHEN_DONT_TAKE_PRODUCTS.concat(suggestion.getClientSuggestionRoutine());
        }
        return finalSuggestion;
    }


}
