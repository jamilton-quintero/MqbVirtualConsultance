package com.example.analisis.servicio.imlp;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.example.analisis.polly.GlobalConstants;
import com.example.analisis.servicio.AudioStorage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class AudioStorageImpl implements AudioStorage {

    private final String connectionString;
    private final String containerName;

    public AudioStorageImpl(@Value("${azure.storage.connection-string}") String connectionString,
                            @Value("${azure.storage.containerName}")String containerName) {
        this.connectionString = connectionString;
        this.containerName = containerName;
    }
    @Override
    public String saveAudio(InputStream audioBoard) {

        byte[] audioOutputStream;
        try {
            audioOutputStream = genereateVoiceBytes(audioBoard);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        int audioLength = audioOutputStream.length;
        InputStream audioInputStream = new ByteArrayInputStream(audioOutputStream);

        try {
            BlobContainerClient containerClient = getContainerClient(this.containerName);
            BlobClient blobClient = containerClient.getBlobClient(namAudioeGenerator());
            blobClient.upload(audioInputStream, audioLength);
            return blobClient.getBlobUrl();
        } catch (Exception ex) {
            //LOGGER.error("Error uploading audio file to Blob Storage: " + ex.getMessage(), ex);
            throw new RuntimeException(ex);
        }

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

    private BlobContainerClient getContainerClient(String containerName) {
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder().connectionString(connectionString).buildClient();
        return blobServiceClient.getBlobContainerClient(containerName);
    }

    private static String namAudioeGenerator() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(GlobalConstants.FORMATE_DATE)) +
                GlobalConstants.DELIMITER + UUID.randomUUID() +
                GlobalConstants.AUDIO_FORMATE;
    }
}
