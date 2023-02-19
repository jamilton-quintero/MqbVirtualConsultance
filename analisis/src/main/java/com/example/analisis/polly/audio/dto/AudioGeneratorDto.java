package com.example.analisis.polly.audio.dto;

import java.io.InputStream;


public class AudioGeneratorDto {
    private String audioPollyPathToSave;
    private InputStream audioStream;

    public AudioGeneratorDto(String audioPollyPathToSave, InputStream audioStream) {
        this.audioPollyPathToSave = audioPollyPathToSave;
        this.audioStream = audioStream;
    }

    public String getAudioPollyPathToSave() {
        return audioPollyPathToSave;
    }

    public InputStream getAudioStream() {
        return audioStream;
    }

}
