package com.example.analisis.servicio;


import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.polly.AmazonPolly;
import com.amazonaws.services.polly.AmazonPollyClientBuilder;
import com.amazonaws.services.polly.model.*;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
public class GenerateAudioService {

    public InputStream ejecutar(String messageToAudio){

        BasicAWSCredentials awsCreds = new BasicAWSCredentials("AKIATBXKOYJXYZ6CFZAK", "oshOlFMekLqQSAAjeBfUOa3wTT0mYQ86j7iX50hc");
        AmazonPolly client = AmazonPollyClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .withRegion(Regions.US_EAST_1)
                .build();

        SynthesizeSpeechRequest synthReq = new SynthesizeSpeechRequest()
                .withText(messageToAudio)
                .withVoiceId(VoiceId.Mia)
                .withOutputFormat(OutputFormat.Mp3)
                .withEngine("standard");
        SynthesizeSpeechResult synthRes = client.synthesizeSpeech(synthReq);

        InputStream audioStream = synthRes.getAudioStream();

        return audioStream;

    }

}
