package com.example.analisis.controller;


import com.example.analisis.domain.entity.SuggestionClientRequestDto;
import com.example.analisis.domain.entity.SuggestionClientResponseDto;
import com.example.analisis.service.ProductSuggestion;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequestMapping("/api/suggestions")
public class SuggestionController {

	private final ProductSuggestion productSuggestion;
	public SuggestionController(ProductSuggestion productSuggestion) {

		this.productSuggestion = productSuggestion;
	}

	@PostMapping("/products")
	public SuggestionClientResponseDto getProductIdsAndSuggestion(@RequestBody SuggestionClientRequestDto request) {
		return productSuggestion.getBestProductIdsAndSuggestion(request);
	}

	@PostMapping("/products-test")
	public SuggestionClientResponseDto getProductIdsAndSuggestionTest(@RequestBody SuggestionClientRequestDto request) throws InterruptedException {
		long start = System.currentTimeMillis();

		Thread.sleep(500);

		System.out.println("Sleep time in ms = " + (System.currentTimeMillis() - start));
		return new SuggestionClientResponseDto("Asegúrate de estar bebiendo suficiente agua. El cabello sano necesita hidratación, por lo que asegurarse de beber al menos ocho vasos de agua al día puede ayudar a mejorar la salud del cabello. Exfolia tu cuero cabelludo. La caspa puede ser causada por un exceso de acumulación de células muertas en el cuero cabelludo. Exfoliar el cuero cabelludo con un producto suave una o dos veces a la semana puede ayudar a eliminar la caspa y mejorar la salud del cabello. Utiliza un champú anticaspa. Existen muchos champús en el mercado que pueden ayudar a tratar la caspa. Busca un champú que contenga ingredientes como el ácido salicílico o el ketoconazol, que pueden ayudar a combatir la caspa. Prueba un aceite capilar. Aplicar un aceite capilar suave, como el aceite de coco o el aceite de jojoba, en el cuero cabelludo puede ayudar a hidratar y suavizar el cabello. No te laves el cabello todos los días. Lavar el cabello con frecuencia puede resecar el cuero cabelludo y empeorar la caspa. Trata de lavar el cabello una o dos veces a la semana, o menos si es posible. Utiliza un acondicionador suave. Aplicar un acondicionador suave después de lavar el cabello puede ayudar a hidratar y proteger el cabello. Busca un acondicionador que no contenga sulfatos ni parabenos. No uses productos con muchos químicos. Los productos que contienen muchos químicos, como los aerosibles para el cabello, pueden resecar el cuero cabelludo y empeorar la caspa. Trata de evitar los productos con muchos químicos y optar por productos naturales o orgánicos. No te estreses. El estrés puede empeorar la caspa. Trata de reducir el Te dejaremos estos productos como recomendacion a continuación",
				"https://clientboard.blob.core.windows.net/client-audios/20230221_125640_681_c439c091-f89f-4413-b0a3-dda0f0658256.mp3" , Arrays.asList(5683,
				5685,
				5689,
				5690,
				5691,
				5705,
				5706,
				5707,
				5708,
				5709));
	}

}
