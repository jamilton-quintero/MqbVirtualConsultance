package com.example.analisis.service.imlp;

import com.example.analisis.configuration.ChatGptConfig;
import com.example.analisis.domain.entity.ChatGptRequest;
import com.example.analisis.domain.entity.ChatGptResponse;
import com.example.analisis.service.BotService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BotServiceImpl implements BotService {

    private final String apiKey;
    public BotServiceImpl(@Value("${openai.api-key}")String apiKey) {
        this.apiKey = apiKey;
    }
    private static final RestTemplate restTemplate = new RestTemplate();

    public ChatGptResponse getSuggestion(String request) {
        return getResponse(
                this.buildHttpEntity(
                        new ChatGptRequest(
                                ChatGptConfig.MODEL,
                                request,
                                ChatGptConfig.TEMPERATURE,
                                ChatGptConfig.MAX_TOKEN,
                                ChatGptConfig.TOP_P)));
    }

    private ChatGptResponse getResponse(HttpEntity<ChatGptRequest> chatRequestHttpEntity) {
        ResponseEntity<ChatGptResponse> responseEntity = restTemplate.postForEntity(
                ChatGptConfig.URL,
                chatRequestHttpEntity,
                ChatGptResponse.class);
        return responseEntity.getBody();
    }

    private HttpEntity<ChatGptRequest> buildHttpEntity(ChatGptRequest chatRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(ChatGptConfig.MEDIA_TYPE));
        headers.add(ChatGptConfig.AUTHORIZATION, ChatGptConfig.BEARER + apiKey);
        return new HttpEntity<>(chatRequest, headers);
    }
}