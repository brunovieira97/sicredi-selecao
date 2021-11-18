package br.com.sicredi.votacao.integration;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;

import br.com.sicredi.votacao.dto.PautaRequestDTO;
import br.com.sicredi.votacao.dto.PautaResponseDTO;
import br.com.sicredi.votacao.dto.SessaoRequestDTO;
import br.com.sicredi.votacao.dto.SessaoResponseDTO;
import io.restassured.http.ContentType;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class SessaoIntegrationTest {
	
	private static final String PATH_PAUTA = "/pautas";
	private static final String PATH_SESSAO = "/{idPauta}/sessoes";
	private static final String BASE_PATH = PATH_PAUTA + PATH_SESSAO;

	@BeforeEach
	public void setup() {
		PautaRequestDTO pauta = new PautaRequestDTO();
		pauta.setNome("Teste");

		Long id = 
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

		given()
			.basePath(PATH_PAUTA)
			.accept(ContentType.JSON)
			.contentType(ContentType.JSON)
			.pathParam("id", id)
		.when()
			.get("/{id}")
		.then()
			.statusCode(HttpStatus.OK.value());

		this.cadastraSessaoDadosValidos();
	}

	@AfterEach
	public void cleanup() {
		// Remove sessões via cascade
		given()
			.basePath(PATH_PAUTA)
			.accept(ContentType.JSON)
		.when()
			.get()
		.then()
			.statusCode(HttpStatus.OK.value())
			.extract().body().jsonPath().getList("", PautaResponseDTO.class)
				.forEach(p -> {
					given()
						.basePath(PATH_PAUTA)
						.accept(ContentType.JSON)
						.contentType(ContentType.JSON)
						.pathParam("id", p.getId())
					.when()
						.delete("/{id}");
				});
	}

	private Long getIdPauta() {
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

	private Long getIdSessao(Long idPauta) {
		Long id = 
			given()
				.basePath(BASE_PATH)
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

	@Test
	public void listaSessoes() {
		Long idPauta = this.getIdPauta();

		List<SessaoResponseDTO> sessoes = 
			given()
				.basePath(BASE_PATH)
				.accept(ContentType.JSON)
				.pathParam("idPauta", idPauta)
			.when()
				.get()
			.then()
				.statusCode(HttpStatus.OK.value())
				.extract().body().jsonPath().getList("", SessaoResponseDTO.class);

		assertEquals(false, sessoes.isEmpty());
	}

	@Test
	public void listaSessaoPorIdExistente() {
		Long idPauta = this.getIdPauta();
		Long idSessao = this.getIdSessao(idPauta);

		SessaoResponseDTO sessao = 
			given()
				.basePath(BASE_PATH)
				.accept(ContentType.JSON)
				.pathParam("idPauta", idPauta)
				.pathParam("idSessao", idSessao)
			.when()
				.get("/{idSessao}")
			.then()
				.statusCode(HttpStatus.OK.value())
				.extract().body().as(SessaoResponseDTO.class);

		assertNotNull(sessao.getId());
		assertEquals(idSessao, sessao.getId());
	}

	@Test
	public void listaSessaoPorIdInexistente() {
		Long idPauta = this.getIdPauta();
		Long idSessao = 99999L;

		given()
			.basePath(BASE_PATH)
			.accept(ContentType.JSON)
			.pathParam("idPauta", idPauta)
			.pathParam("idSessao", idSessao)
		.when()
			.get("/{idSessao}")
		.then()
			.statusCode(HttpStatus.NO_CONTENT.value());
	}

	@Test
	public void cadastraSessaoDadosValidos() {
		Long idPauta = this.getIdPauta();

		SessaoRequestDTO sessao = new SessaoRequestDTO();
		sessao.setDataHoraAbertura(LocalDateTime.now().plusDays(1));
		sessao.setDataHoraFechamento(sessao.getDataHoraAbertura().plusDays(1));

		Long idSessao = 
			given()
				.basePath(BASE_PATH)
				.accept(ContentType.JSON)
				.contentType(ContentType.JSON)
				.pathParam("idPauta", idPauta)
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
			.pathParam("idSessao", idSessao)
		.when()
			.get("/{idSessao}")
		.then()
			.statusCode(HttpStatus.OK.value());
	}

	@Test
	public void cadastraSessaoSemDataAbertura() {
		Long idPauta = this.getIdPauta();

		SessaoRequestDTO sessao = new SessaoRequestDTO();
		sessao.setDataHoraAbertura(null);
		sessao.setDataHoraFechamento(LocalDateTime.now().plusDays(1));

		given()
			.basePath(BASE_PATH)
			.accept(ContentType.JSON)
			.contentType(ContentType.JSON)
			.pathParam("idPauta", idPauta)
			.body(sessao)
		.when()
			.post()
		.then()
			.statusCode(HttpStatus.BAD_REQUEST.value());
	}

	@Test
	public void cadastraSessaoSemDataFechamento() {
		Long idPauta = this.getIdPauta();

		SessaoRequestDTO sessao = new SessaoRequestDTO();
		sessao.setDataHoraAbertura(LocalDateTime.now().plusDays(1));
		
		LocalDateTime expected = sessao.getDataHoraAbertura().plusMinutes(1);

		Long idSessao = 
			given()
				.basePath(BASE_PATH)
				.accept(ContentType.JSON)
				.contentType(ContentType.JSON)
				.pathParam("idPauta", idPauta)
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
				.pathParam("idSessao", idSessao)
			.when()
				.get("/{idSessao}")
			.then()
				.statusCode(HttpStatus.OK.value())
				.extract().body().as(SessaoResponseDTO.class);
		
		assertEquals(response.getDataHoraFechamento(), expected);
	}

	@Test
	public void cadastraSessaoComDataAberturaPassada() {
		Long idPauta = this.getIdPauta();

		System.out.println("PAUTA: " + idPauta.toString());

		SessaoRequestDTO sessao = new SessaoRequestDTO();
		sessao.setDataHoraAbertura(LocalDateTime.now().minusDays(7));

		given()
			.basePath(BASE_PATH)
			.accept(ContentType.JSON)
			.contentType(ContentType.JSON)
			.pathParam("idPauta", idPauta)
			.body(sessao)
		.when()
			.post()
		.then()
			.statusCode(HttpStatus.BAD_REQUEST.value());
	}

	@Test
	public void excluiSessaoExistente() {
		Long idPauta = this.getIdPauta();
		Long idSessao = this.getIdSessao(idPauta);

		given()
			.basePath(BASE_PATH)
			.accept(ContentType.JSON)
			.contentType(ContentType.JSON)
			.pathParam("idPauta", idPauta)
			.pathParam("idSessao", idSessao)
		.when()
			.delete("/{idSessao}")
		.then()
			.statusCode(HttpStatus.OK.value());

		given()
			.basePath(BASE_PATH)
			.accept(ContentType.JSON)
			.contentType(ContentType.JSON)
			.pathParam("idPauta", idPauta)
			.pathParam("idSessao", idSessao)
		.when()
			.get("/{idSessao}")
		.then()
			.statusCode(HttpStatus.NO_CONTENT.value());
	}

	@Test
	public void excluiSessaoInexistente() {
		Long idPauta = this.getIdPauta();
		Long idSessao = 99999L;

		given()
			.basePath(BASE_PATH)
			.accept(ContentType.JSON)
			.contentType(ContentType.JSON)
			.pathParam("idPauta", idPauta)
			.pathParam("idSessao", idSessao)
		.when()
			.delete("/{idSessao}")
		.then()
			.statusCode(HttpStatus.NO_CONTENT.value());

		given()
			.basePath(BASE_PATH)
			.accept(ContentType.JSON)
			.pathParam("idPauta", idPauta)
			.pathParam("idSessao", idSessao)
		.when()
			.get("/{idSessao}")
		.then()
			.statusCode(HttpStatus.NO_CONTENT.value());
	}
}
