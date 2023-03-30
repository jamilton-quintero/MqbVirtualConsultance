package com.example.analisis.controller;


import com.example.analisis.domain.entity.BotRequest;
import com.example.analisis.domain.entity.ChatGptResponse;
import com.example.analisis.service.BotService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/open-ia")
@PreAuthorize("hasRole('ADMIN')")
public class OpenIaController {

	private final BotService botService;

	public OpenIaController(BotService botService) {
		this.botService = botService;
	}


	@PostMapping("/send")
	public ChatGptResponse sendMessage(@RequestBody BotRequest request) {
		return botService.getSuggestion(request.getMessage());
	}

}
