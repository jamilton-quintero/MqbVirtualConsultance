package com.example.analisis.service;

import com.example.analisis.domain.entity.ChatGptResponse;

public interface BotService {

    ChatGptResponse getSuggestion(String request);
}