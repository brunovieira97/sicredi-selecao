package br.com.sicredi.votacao.integration.util;

import static io.restassured.RestAssured.given;

import org.springframework.http.HttpStatus;

import br.com.sicredi.votacao.dto.PautaResponseDTO;
import br.com.sicredi.votacao.dto.SessaoResponseDTO;
import io.restassured.http.ContentType;

public class IntegrationTestUtils {
	
	private static final String PATH_PAUTA = "/pautas";
	private static final String PATH_SESSAO = PATH_PAUTA + "/{idPauta}/sessoes";
	private static final String PATH_VOTO = PATH_SESSAO + "/{idSessao}/votos";


	public static Long getIdPauta() {
		Long id =
			given()
				.basePath(PATH_PAUTA)
				.accept(ContentType.JSON)
			.when()
				.get()
			.then()
				.statusCode(HttpStatus.OK.value())
				.extract().body().jsonPath().getList("", PautaResponseDTO.class)
					.get(0).getId();

		return id;
	}

	public static Long getIdSessao(Long idPauta) {
		Long id = 
			given()
				.basePath(PATH_SESSAO)
				.accept(ContentType.JSON)
				.pathParam("idPauta", idPauta)
			.when()
				.get()
			.then()
				.statusCode(HttpStatus.OK.value())
				.extract().body().jsonPath().getList("", SessaoResponseDTO.class)
					.get(0).getId();

		return id;
	}

	public static Long getIdVoto(Long idPauta, Long idSessao) {
		Long id = 
			given()
				.basePath(PATH_VOTO)
				.accept(ContentType.JSON)
				.pathParam("idPauta", idPauta)
				.pathParam("idSessao", idSessao)
			.when()
				.get()
			.then()
				.statusCode(HttpStatus.OK.value())
				.extract().body().jsonPath().getList("", SessaoResponseDTO.class)
					.get(0).getId();

		return id;
	}
}
