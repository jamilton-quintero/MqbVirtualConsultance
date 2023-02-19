package com.example.analisis.servicio;

import com.example.analisis.entidad.ChatGptResponse;

public interface BotService {

    ChatGptResponse askQuestion(String request);
}