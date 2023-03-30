package com.example.analisis.service;

import java.io.InputStream;

public interface AudioGeneratorService {
    InputStream generateAudioSuggestion(String messageToAudio);

}
