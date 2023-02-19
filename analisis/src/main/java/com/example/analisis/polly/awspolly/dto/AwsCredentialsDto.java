package com.example.analisis.polly.awspolly.dto;

public class AwsCredentialsDto {

    private String accessKey;
    private String secretKey;
    private String messageToAudio;

    public AwsCredentialsDto(String accessKey, String secretKey, String messageToAudio) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.messageToAudio = messageToAudio;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public String getMessageToAudio() {
        return messageToAudio;
    }
}
