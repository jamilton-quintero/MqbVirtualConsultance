package com.example.analisis.servicio;

import java.io.InputStream;

public interface AudioGeneratorService {
    InputStream generateAudioBoard(String messageToAudio);

}
