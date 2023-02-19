package com.example.analisis.servicio;

import com.example.analisis.entidad.BoardClientRequestDto;
import com.example.analisis.entidad.BoardClientResponseDto;
import com.example.analisis.entidad.DtoProductBoard;
import com.example.analisis.entidad.ReproduceAudio;
import javazoom.jl.decoder.JavaLayerException;

public interface ProductBoard {

    DtoProductBoard getBestProductsAndBoard(String promt);

    DtoProductBoard genereteAudio(String promt);

    void reproduceteAudio(ReproduceAudio reproduceAudio) throws JavaLayerException;

    BoardClientResponseDto getBestProductIdsAndBoard(BoardClientRequestDto boardClientRequest);

    String saveAudio(ReproduceAudio reproduceAudio);

}
