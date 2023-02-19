package com.example.analisis.servicio.imlp;

import com.example.analisis.entidad.BoardClientRequestDto;
import com.example.analisis.entidad.BoardClientResponseDto;
import com.example.analisis.entidad.ChatGptResponse;
import com.example.analisis.entidad.DtoProduct;
import com.example.analisis.entidad.DtoProductBoard;
import com.example.analisis.entidad.ReproduceAudio;
import com.example.analisis.servicio.AudioGeneratorService;
import com.example.analisis.servicio.AudioStorage;
import com.example.analisis.servicio.BotService;
import com.example.analisis.servicio.GenerateAudioService;
import com.example.analisis.servicio.LuxeneProductService;
import com.example.analisis.servicio.ProductBoard;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;
import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
public class ProductBoardImpl implements ProductBoard {

    public static final String PREFIX_MESSAGE = "(Generar Recomendacion enfocada en productos de belleza y cuidado personal para una %s de %s años, la recomendacion debe ser muy optimizada y detallada para mejorar la situacion mencioanda a continuacion). ";
    public static final String MESSAGE_WHEN_TAKE_PRODUCTS = " Te dejaremos estos productos como recomendacion a continuación";
    public static final String MESSAGE_WHEN_DONT_TAKE_PRODUCTS = "No tenemos productos para recomendarte pero podemos darte el siguiente consejo: ";
    private final LuxeneProductService luxeneProductService;
    private final BotService botService;
    private final GenerateAudioService generateAudioService;
    private final AudioStorage audioStorage;
    private final AudioGeneratorService audioGeneratorService;
    public ProductBoardImpl(LuxeneProductServiceImpl luxeneProductService, BotService botService, GenerateAudioService generateAudioService, AudioStorage audioStorage, AudioGeneratorService audioGeneratorService) {
        this.luxeneProductService = luxeneProductService;
        this.botService = botService;
        this.generateAudioService = generateAudioService;
        this.audioStorage = audioStorage;
        this.audioGeneratorService = audioGeneratorService;
    }

    @Override
    public DtoProductBoard getBestProductsAndBoard(String request) {

        ChatGptResponse response = botService.askQuestion(request);
        String board = response.getChoices().get(0).getText();
        DtoProductBoard dtoProductBoards = new DtoProductBoard();
        try {

            board = board.replaceAll("\n","");

            List<DtoProduct> products = luxeneProductService.searchWithoutStanford(board);

            if(!products.isEmpty()){
                dtoProductBoards.setBoard(board.concat(" Te dejaremos estos productos como recomendacion a continuación"));
                dtoProductBoards.setProducts(products);
            } else {
                dtoProductBoards.setBoard("No tenemos productos para recomendarte pero podemos darte el siguiente consejo:".concat(board));
            }

            InputStream voiceBoard = generateAudioService.ejecutar(board);


            dtoProductBoards.setAudioUrl("");
            dtoProductBoards.setVoice(genereateVoiceBytes(voiceBoard));

            return dtoProductBoards;

        } catch (IOException | ParseException e) {
            System.out.printf(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return dtoProductBoards;
    }

    @Override
    public DtoProductBoard genereteAudio(String promt) {
        try {
            promt = promt.replaceAll("\n|\n1","");
            InputStream voiceBoard = generateAudioService.ejecutar(promt);
            return new DtoProductBoard(null, null, null,genereateVoiceBytes(voiceBoard) );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void reproduceteAudio(ReproduceAudio reproduceAudio) throws JavaLayerException {
        InputStream inputStream = new ByteArrayInputStream(reproduceAudio.getVoice());
           //create an MP3 player
        AdvancedPlayer player = new AdvancedPlayer(inputStream,
                javazoom.jl.player.FactoryRegistry.systemRegistry().createAudioDevice());

        player.setPlayBackListener(new PlaybackListener() {
            @Override
            public void playbackStarted(PlaybackEvent evt) {
                System.out.println("Playback started");
                System.out.println(reproduceAudio.getMensaje());
            }

            @Override
            public void playbackFinished(PlaybackEvent evt) {
                System.out.println("Playback finished");
            }
        });

        player.play();

    }

    @Override
    public BoardClientResponseDto getBestProductIdsAndBoard(BoardClientRequestDto request) {

        String board = getBoarFromOpenIa(request);

        board = cleanTextOfSpecialCharacters(board);

        List<Integer> productIds = luxeneProductService.searchBest(board);

        InputStream audioBoard = audioGeneratorService.generateAudioBoard(board);

        String audioUrl = audioStorage.saveAudio(audioBoard);

        String finalBoard = "";

        if(!productIds.isEmpty()){
            finalBoard = board.concat(MESSAGE_WHEN_TAKE_PRODUCTS);
        } else {
            finalBoard = MESSAGE_WHEN_DONT_TAKE_PRODUCTS.concat(board);
        }

        return new BoardClientResponseDto(finalBoard, audioUrl, productIds);

    }

    @Override
    public String saveAudio(ReproduceAudio reproduceAudio) {
        InputStream inputStream = new ByteArrayInputStream(reproduceAudio.getVoice());
        return audioStorage.saveAudio(inputStream);
    }

    private String getBoarFromOpenIa(BoardClientRequestDto boardClientRequest) {
        String message = getBoardOnBaseAgeAndGender(boardClientRequest);

        ChatGptResponse response = botService.askQuestion(message);

        return response.getChoices().get(0).getText();
    }

    private String getBoardOnBaseAgeAndGender(BoardClientRequestDto boardClientRequest) {
        return String.format(PREFIX_MESSAGE,
                        getGender(boardClientRequest.getClientGender()),
                        boardClientRequest.getClientAge())
                .concat(boardClientRequest.getMessage());
    }

    private String getGender(char typeGender){
        return typeGender == 'M' ? "Hombre" : "Mujer";

    }

    private byte[] genereateVoiceBytes(InputStream voiceStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = voiceStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        return outputStream.toByteArray();
    }

    private static String cleanTextOfSpecialCharacters(String problem) {
        problem = problem.replaceAll("<[^>]*>|\\r|\\n|\\t|\n|\r|\t|([0-9].)", "");
        return problem;
    }

}
