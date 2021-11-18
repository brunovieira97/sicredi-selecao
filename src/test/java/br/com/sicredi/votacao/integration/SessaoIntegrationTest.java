package br.com.sicredi.votacao.integration;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;

import br.com.sicredi.votacao.dto.PautaRequestDTO;
import br.com.sicredi.votacao.dto.PautaResponseDTO;
import br.com.sicredi.votacao.dto.SessaoRequestDTO;
import br.com.sicredi.votacao.dto.SessaoResponseDTO;
import io.restassured.http.ContentType;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@TestInstance(Lifecycle.PER_CLASS)
public class SessaoIntegrationTest {
	
	private static final String PATH_PAUTA = "/pautas";
	private static final String PATH_SESSAO = "/{idPauta}/sessoes";
	private static final String BASE_PATH = PATH_PAUTA + PATH_SESSAO;
	private Long idPauta = 0L;
	private Long idSessao = 0L;

	@BeforeAll
	public void setup() {
		PautaRequestDTO pauta = new PautaRequestDTO();
		pauta.setNome("Teste");

		this.idPauta = 
			given()
				.basePath(PATH_PAUTA)
				.accept(ContentType.JSON)
				.contentType(ContentType.JSON)
				.body(pauta)
			.when()
				.post()
			.then()
				.statusCode(HttpStatus.CREATED.value())
				.extract().body().as(PautaResponseDTO.class).getId();

		SessaoRequestDTO sessao = new SessaoRequestDTO();
		sessao.setDataHoraAbertura(LocalDateTime.now().plusDays(1));
		sessao.setDataHoraFechamento(LocalDateTime.now().plusDays(2));
		
		this.idSessao = 
			given()
				.basePath(BASE_PATH)
				.accept(ContentType.JSON)
				.contentType(ContentType.JSON)
				.pathParam("idPauta", this.idPauta)
				.body(sessao)
			.when()
				.post()
			.then()
				.statusCode(HttpStatus.CREATED.value())
				.extract().body().as(SessaoResponseDTO.class).getId();
	}

	@AfterAll
	public void excluirPauta() {
		given()
			.basePath(PATH_PAUTA)
			.accept(ContentType.JSON)
			.contentType(ContentType.JSON)
			.pathParam("id", this.idPauta)
		.when()
			.delete("/{id}")
		.then()
			.statusCode(HttpStatus.OK.value());
	}

	@Test
	public void listaSessoes() {
		List<SessaoResponseDTO> sessoes = 
			given()
				.basePath(BASE_PATH)
				.accept(ContentType.JSON)
				.pathParam("idPauta", this.idPauta)
			.when()
				.get()
			.then()
				.statusCode(HttpStatus.OK.value())
				.extract().body().jsonPath().getList("", SessaoResponseDTO.class);

		assertEquals(false, sessoes.isEmpty());
	}

	@Test
	public void listaSessaoPorIdExistente() {
		SessaoResponseDTO sessao = 
			given()
				.basePath(BASE_PATH)
				.accept(ContentType.JSON)
				.pathParam("idPauta", this.idPauta)
				.pathParam("id", this.idSessao)
			.when()
				.get("/{id}")
			.then()
				.statusCode(HttpStatus.OK.value())
				.extract().body().as(SessaoResponseDTO.class);

		assertNotNull(sessao.getId());
		assertEquals(this.idSessao, sessao.getId());
	}

	@Test
	public void listaSessaoPorIdInexistente() {
		Long id = 5L;

		given()
			.basePath(BASE_PATH)
			.accept(ContentType.JSON)
			.pathParam("idPauta", this.idPauta)
			.pathParam("id", id)
		.when()
			.get("/{id}")
		.then()
			.statusCode(HttpStatus.NO_CONTENT.value());
	}

	@Test
	public void cadastraSessaoDadosValidos() {
		SessaoRequestDTO sessao = new SessaoRequestDTO();
		sessao.setDataHoraAbertura(LocalDateTime.now().plusDays(1));
		sessao.setDataHoraFechamento(sessao.getDataHoraAbertura().plusDays(1));

		Long id = 
			given()
				.basePath(BASE_PATH)
				.accept(ContentType.JSON)
				.contentType(ContentType.JSON)
				.pathParam("idPauta", this.idPauta)
				.body(sessao)
			.when()
				.post()
			.then()
				.statusCode(HttpStatus.CREATED.value())
				.extract().body().as(SessaoResponseDTO.class).getId();

		given()
			.basePath(BASE_PATH)
			.accept(ContentType.JSON)
			.contentType(ContentType.JSON)
			.pathParam("idPauta", idPauta)
			.pathParam("id", id)
		.when()
			.get("/{id}")
		.then()
			.statusCode(HttpStatus.OK.value());
	}

	@Test
	public void cadastraSessaoSemDataAbertura() {
		SessaoRequestDTO sessao = new SessaoRequestDTO();
		sessao.setDataHoraAbertura(null);
		sessao.setDataHoraFechamento(LocalDateTime.now().plusDays(1));

		given()
			.basePath(BASE_PATH)
			.accept(ContentType.JSON)
			.contentType(ContentType.JSON)
			.pathParam("idPauta", this.idPauta)
			.body(sessao)
		.when()
			.post()
		.then()
			.statusCode(HttpStatus.BAD_REQUEST.value());
	}

	@Test
	public void cadastraSessaoSemDataFechamento() {
		SessaoRequestDTO sessao = new SessaoRequestDTO();
		sessao.setDataHoraAbertura(LocalDateTime.now().plusDays(1));
		
		LocalDateTime expected = sessao.getDataHoraAbertura().plusMinutes(1);

		Long id = 
			given()
				.basePath(BASE_PATH)
				.accept(ContentType.JSON)
				.contentType(ContentType.JSON)
				.pathParam("idPauta", this.idPauta)
				.body(sessao)
			.when()
				.post()
			.then()
				.statusCode(HttpStatus.CREATED.value())
				.extract().body().as(SessaoResponseDTO.class).getId();

		SessaoResponseDTO response = 
			given()
				.basePath(BASE_PATH)
				.accept(ContentType.JSON)
				.contentType(ContentType.JSON)
				.pathParam("idPauta", idPauta)
				.pathParam("id", id)
			.when()
				.get("/{id}")
			.then()
				.statusCode(HttpStatus.OK.value())
				.extract().body().as(SessaoResponseDTO.class);
		
		assertEquals(response.getDataHoraFechamento(), expected);
	}

	@Test
	public void cadastraSessaoComDataAberturaPassada() {
		SessaoRequestDTO sessao = new SessaoRequestDTO();
		sessao.setDataHoraAbertura(LocalDateTime.now().minusDays(7));

		given()
			.basePath(BASE_PATH)
			.accept(ContentType.JSON)
			.contentType(ContentType.JSON)
			.pathParam("idPauta", this.idPauta)
			.body(sessao)
		.when()
			.post()
		.then()
			.statusCode(HttpStatus.BAD_REQUEST.value());
	}

	@Test
	public void excluiSessaoExistente() {
		given()
			.basePath(BASE_PATH)
			.accept(ContentType.JSON)
			.contentType(ContentType.JSON)
			.pathParam("idPauta", 1)
			.pathParam("id", 1)
		.when()
			.delete("/{id}")
		.then()
			.statusCode(HttpStatus.OK.value());
	}

	@Test
	public void excluiSessaoInexistente() {
		Long id = 10L;

		given()
			.basePath(BASE_PATH)
			.accept(ContentType.JSON)
			.contentType(ContentType.JSON)
			.pathParam("idPauta", idPauta)
			.pathParam("id", id)
		.when()
			.delete("/{id}")
		.then()
			.statusCode(HttpStatus.NO_CONTENT.value());

		given()
			.basePath(BASE_PATH)
			.accept(ContentType.JSON)
			.pathParam("idPauta", idPauta)
			.pathParam("id", id)
		.when()
			.get("/{id}")
		.then()
			.statusCode(HttpStatus.NO_CONTENT.value());
	}
}
