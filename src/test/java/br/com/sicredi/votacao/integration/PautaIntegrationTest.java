package br.com.sicredi.votacao.integration;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;

import br.com.sicredi.votacao.dto.PautaRequestDTO;
import br.com.sicredi.votacao.dto.PautaResponseDTO;
import io.restassured.http.ContentType;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class PautaIntegrationTest {

	private static final String BASE_PATH = "/pautas";

	@Test
	public void listaPautas() {

		List<PautaResponseDTO> pautas = 
			given()
				.basePath(BASE_PATH)
				.accept(ContentType.JSON)
			.when()
				.get()
			.then()
				.statusCode(HttpStatus.OK.value())
				.extract().body().jsonPath().getList("", PautaResponseDTO.class);

		assertEquals(false, pautas.isEmpty());
	}

	@Test
	public void listaPautaPorIdExistente() {
		Long id = 1L;

		PautaResponseDTO pauta = 
			given()
				.basePath(BASE_PATH)
				.pathParam("id", id)
				.accept(ContentType.JSON)
			.when()
				.get("/{id}")
			.then()
				.statusCode(HttpStatus.OK.value())
				.extract().body().as(PautaResponseDTO.class);

		assertNotNull(pauta.getId());
		assertEquals(id, pauta.getId());
	}

	@Test
	public void listaPautaPorIdInexistente() {
		Long id = 5L;

		given()
			.basePath(BASE_PATH)
			.pathParam("id", id)
			.accept(ContentType.JSON)
		.when()
			.get("/{id}")
		.then()
			.statusCode(HttpStatus.NO_CONTENT.value());
	}

	@Test
	public void cadastraPautaDadosValidos() {
		PautaRequestDTO pauta = new PautaRequestDTO();
		pauta.setNome("Teste");

		Long id = 
			given()
				.basePath(BASE_PATH)
				.accept(ContentType.JSON)
				.contentType(ContentType.JSON)
				.body(pauta)
			.when()
				.post()
			.then()
				.statusCode(HttpStatus.CREATED.value())
				.extract().body().as(PautaResponseDTO.class).getId();

		given()
			.basePath(BASE_PATH)
			.accept(ContentType.JSON)
			.contentType(ContentType.JSON)
			.pathParam("id", id)
		.when()
			.get("/{id}")
		.then()
			.statusCode(HttpStatus.OK.value());
	}

	@Test
	public void cadastraPautaDadosInvalidos() {
		PautaRequestDTO pauta = new PautaRequestDTO();
		pauta.setNome(null);

		given()
			.basePath(BASE_PATH)
			.accept(ContentType.JSON)
			.contentType(ContentType.JSON)
			.body(pauta)
		.when()
			.post()
		.then()
			.statusCode(HttpStatus.BAD_REQUEST.value());
	}

	@Test
	public void atualizaPautaDadosValidos() {
		Long id = 1L;
		String novoNome = "Novo nome";

		PautaResponseDTO pauta = 
			given()
				.basePath(BASE_PATH)
				.pathParam("id", id)
				.accept(ContentType.JSON)
			.when()
				.get("/{id}")
			.then()
				.statusCode(HttpStatus.OK.value())
				.extract().body().as(PautaResponseDTO.class);

		assertEquals(id, pauta.getId());

		PautaRequestDTO newPauta = new PautaRequestDTO();
		newPauta.setNome(novoNome);

		PautaResponseDTO response =
			given()
				.basePath(BASE_PATH)
				.accept(ContentType.JSON)
				.contentType(ContentType.JSON)
				.pathParam("id", id)
				.body(newPauta)
			.when()
				.put("/{id}")
			.then()
				.statusCode(HttpStatus.OK.value())
				.extract().body().as(PautaResponseDTO.class);
		
		assertEquals(novoNome, response.getNome());
	}

	@Test
	public void atualizaPautaDadosInvalidos() {
		Long id = 1L;
		String novoNome = null;

		PautaResponseDTO pauta = 
			given()
				.basePath(BASE_PATH)
				.accept(ContentType.JSON)
				.pathParam("id", id)
			.when()
				.get("/{id}")
			.then()
				.statusCode(HttpStatus.OK.value())
				.extract().body().as(PautaResponseDTO.class);

		assertEquals(id, pauta.getId());

		PautaRequestDTO newPauta = new PautaRequestDTO();
		newPauta.setNome(novoNome);

		given()
			.basePath(BASE_PATH)
			.accept(ContentType.JSON)
			.contentType(ContentType.JSON)
			.pathParam("id", id)
			.body(newPauta)
		.when()
			.put("/{id}")
		.then()
			.statusCode(HttpStatus.BAD_REQUEST.value());
	}

	@Test
	public void excluiPautaExistente() {
		Long id = 1L;

		given()
			.basePath(BASE_PATH)
			.accept(ContentType.JSON)
			.contentType(ContentType.JSON)
			.pathParam("id", id)
		.when()
			.delete("/{id}")
		.then()
			.statusCode(HttpStatus.OK.value());

		given()
			.basePath(BASE_PATH)
			.accept(ContentType.JSON)
			.contentType(ContentType.JSON)
			.pathParam("id", id)
		.when()
			.get("/{id}")
		.then()
			.statusCode(HttpStatus.NO_CONTENT.value());
	}

	@Test
	public void excluiPautaInexistente() {
		Long id = 10L;

		given()
			.basePath(BASE_PATH)
			.accept(ContentType.JSON)
			.contentType(ContentType.JSON)
			.pathParam("id", id)
		.when()
			.delete("/{id}")
		.then()
			.statusCode(HttpStatus.NO_CONTENT.value());

		given()
			.basePath(BASE_PATH)
			.accept(ContentType.JSON)
			.pathParam("id", id)
		.when()
			.get("/{id}")
		.then()
			.statusCode(HttpStatus.NO_CONTENT.value());
	}
}
