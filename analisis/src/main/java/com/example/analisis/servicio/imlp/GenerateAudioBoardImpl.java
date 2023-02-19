package com.example.analisis.servicio.imlp;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.polly.AmazonPolly;
import com.amazonaws.services.polly.AmazonPollyClientBuilder;
import com.amazonaws.services.polly.model.Engine;
import com.amazonaws.services.polly.model.OutputFormat;
import com.amazonaws.services.polly.model.SynthesizeSpeechRequest;
import com.amazonaws.services.polly.model.SynthesizeSpeechResult;
import com.amazonaws.services.polly.model.VoiceId;
import com.example.analisis.polly.GlobalConstants;
import com.example.analisis.polly.exceptions.AwsException;
import com.example.analisis.servicio.AudioGeneratorService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class GenerateAudioBoardImpl implements AudioGeneratorService {

    private final String accessKey;
    private final String secretKey;

    public GenerateAudioBoardImpl(@Value("${aws.polly.access-key}") String accessKey,
                                  @Value("${aws.polly.secret-key}")String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }
    public InputStream generateAudioBoard(String messageToAudio) {

        InputStream audioStream;

        try {

            BasicAWSCredentials awsCreds = new BasicAWSCredentials(this.accessKey , this.secretKey);

            AmazonPolly client = AmazonPollyClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                    .withRegion(Regions.US_EAST_1)
                    .build();

            VoiceId voice = typeVoiceBaseOnSizeMessage(messageToAudio);
            Engine engine = typeEngineBaseOnSizeMessage(messageToAudio);

            SynthesizeSpeechRequest synthReq = new SynthesizeSpeechRequest()
                    .withText(messageToAudio)
                    .withVoiceId(voice)
                    .withOutputFormat(OutputFormat.Mp3)
                    .withEngine(engine);
            SynthesizeSpeechResult synthRes = client.synthesizeSpeech(synthReq);

            audioStream = synthRes.getAudioStream();

            System.out.printf(GlobalConstants.LA_COMUNICACION_CON_AWS_FUE_EXITOSA);
        } catch (Exception e) {
            System.out.printf(GlobalConstants.SE_PRODUJO_UN_ERROR_EN_LA_COMUNICACION_CON_AWS, e);
            throw new AwsException(e.getMessage());
        }

        return audioStream;
    }

    private static VoiceId typeVoiceBaseOnSizeMessage(String messageToAudio) {
        return messageToAudio.length() <= GlobalConstants.LIMIT_NEURAL_VOICE ? VoiceId.Mia : VoiceId.Penelope;
    }

    private static Engine typeEngineBaseOnSizeMessage(String messageToAudio) {
        return messageToAudio.length() <= GlobalConstants.LIMIT_NEURAL_VOICE ? Engine.Neural : Engine.Standard;
    }
}
