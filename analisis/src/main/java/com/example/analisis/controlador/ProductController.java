package com.example.analisis.controlador;

import com.example.analisis.entidad.*;
import com.example.analisis.servicio.BotService;
import com.example.analisis.servicio.LuxeneProductService;
import com.example.analisis.servicio.ProductBoard;
import com.example.analisis.servicio.imlp.LuxeneProductServiceImpl;
import java.io.IOException;
import java.util.List;

import javazoom.jl.decoder.JavaLayerException;
import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {

	private final LuxeneProductService luxeneProductService;
	private final BotService botService;
	private final ProductBoard productBoard;
	public ProductController(LuxeneProductService luxeneProductService, BotService botService, ProductBoard productBoard) {
		this.luxeneProductService = luxeneProductService;
		this.botService = botService;
		this.productBoard = productBoard;
	}

	@PostMapping("/searchWithoutStanford")
	public ResponseEntity<List<DtoProduct>> searchWithoutStanford(@RequestBody BotRequest request) {
		try {
			List<DtoProduct> products = luxeneProductService.searchWithoutStanford(request.getMessage());
			return ResponseEntity.ok(products);
		} catch (IOException | ParseException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PostMapping("/send")
	public ChatGptResponse sendMessage(@RequestBody BotRequest request) {
		return botService.askQuestion(request.getMessage());
	}

	@PostMapping("/productBoards1")
	public DtoProductBoard getProductBoard(@RequestBody BotRequest request) {
		return productBoard.getBestProductsAndBoard(request.getMessage());
	}

	@PostMapping("/voice")
	public DtoProductBoard genereteSound(@RequestBody BotRequest request) {
		return productBoard.genereteAudio(request.getMessage());
	}

	@PostMapping("/reproduceVoice")
	public void genereteSound(@RequestBody ReproduceAudio reproduceAudio) throws JavaLayerException {
		productBoard.reproduceteAudio(reproduceAudio);
	}

	@PostMapping("/productBoards")
	public BoardClientResponseDto getProductIdsAndBoard(@RequestBody BoardClientRequestDto request) {
		return productBoard.getBestProductIdsAndBoard(request);
	}

	@PostMapping("/saveAudioInAzure")
	public String saveAudioInAzure(@RequestBody ReproduceAudio reproduceAudio){
		return productBoard.saveAudio(reproduceAudio);
	}

}
