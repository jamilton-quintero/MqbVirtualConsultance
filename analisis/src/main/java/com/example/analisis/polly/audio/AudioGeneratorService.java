package com.example.analisis.polly.audio;

import com.example.analisis.polly.GlobalConstants;
import com.example.analisis.polly.audio.dto.AudioGeneratorDto;
import com.example.analisis.polly.exceptions.AudioProcesorException;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
/*
@Service
public class AudioGeneratorService {

    public void ejecute(AudioGeneratorDto audioGenerator) {
        FileOutputStream fileOutputStream;
        InputStream audioStream = audioGenerator.getAudioStream();
        String path = audioGenerator.getAudioPollyPathToSave();
        String finalMessage = "";
        try {
            fileOutputStream = new FileOutputStream(String.format(path, namAudioeGenerator()));
            byte[] buffer = new byte[2 * 1024];
            int length;
            while ((length = audioStream.read(buffer)) > 0) {
                fileOutputStream.write(buffer, 0, length);
            }
            fileOutputStream.close();
            audioStream.close();
            finalMessage = String.format(GlobalConstants.EL_AUDIO_SE_GUARDO_DE_MARENA_EXITOSA, path);
            System.out.printf(finalMessage);
        } catch (IOException e) {
            finalMessage = String.format(GlobalConstants.NO_FUE_POSIBLE_GUARDAR_EL_AUDIO, path);
            System.out.printf(finalMessage, e);
            throw new AudioProcesorException(e.getMessage());
        }

    }

    private static String namAudioeGenerator() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(GlobalConstants.FORMATE_DATE)) +
                GlobalConstants.DELIMITER + UUID.randomUUID() +
                GlobalConstants.AUDIO_FORMATE;
    }
}*/
