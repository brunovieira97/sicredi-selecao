package br.com.sicredi.votacao.integration;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;

import br.com.sicredi.votacao.dto.PautaRequestDTO;
import br.com.sicredi.votacao.dto.PautaResponseDTO;
import br.com.sicredi.votacao.dto.SessaoRequestDTO;
import br.com.sicredi.votacao.dto.SessaoResponseDTO;
import br.com.sicredi.votacao.dto.VotoRequestDTO;
import br.com.sicredi.votacao.dto.VotoResponseDTO;
import br.com.sicredi.votacao.enumerator.ValorVoto;
import br.com.sicredi.votacao.integration.util.IntegrationTestUtils;
import io.restassured.http.ContentType;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class VotoIntegrationTest {
	private static final String PATH_PAUTA = "/pautas";
	private static final String PATH_SESSAO = PATH_PAUTA + "/{idPauta}/sessoes";
	private static final String PATH_VOTO = "/{idSessao}/votos";
	private static final String BASE_PATH = PATH_SESSAO + PATH_VOTO;

	private static final String CPF_VALIDO = "76235917031";
	private static final String CPF_INVALIDO = "11111111111";
	
	@BeforeEach
	public void setup(TestInfo info) {
		PautaRequestDTO pauta = new PautaRequestDTO();
		pauta.setNome("Teste");

		Long idPauta = 
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
			.pathParam("idPauta", idPauta)
		.when()
			.get("/{idPauta}")
		.then()
			.statusCode(HttpStatus.OK.value());

		SessaoRequestDTO sessao = new SessaoRequestDTO();
		sessao.setDataHoraAbertura(LocalDateTime.now());
		sessao.setDataHoraFechamento(LocalDateTime.now().plusDays(1));

		Long idSessao = 
			given()
				.basePath(PATH_SESSAO)
				.accept(ContentType.JSON)
				.contentType(ContentType.JSON)
				.body(sessao)
				.pathParam("idPauta", idPauta)
			.when()
				.post()
			.then()
				.statusCode(HttpStatus.CREATED.value())
				.extract().body().as(SessaoResponseDTO.class).getId();

		given()
			.basePath(PATH_SESSAO)
			.accept(ContentType.JSON)
			.contentType(ContentType.JSON)
			.pathParam("idPauta", idPauta)
			.pathParam("idSessao", idSessao)
		.when()
			.get("/{idSessao}")
		.then()
			.statusCode(HttpStatus.OK.value());

		if (info.getDisplayName().equals("cadastraVotoCpfInvalido"))
			return;
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

	@Test
	public void listaVotos() {
		this.cadastraVotoDadosValidos();

		Long idPauta = IntegrationTestUtils.getIdPauta();
		Long idSessao = IntegrationTestUtils.getIdSessao(idPauta);

		List<VotoResponseDTO> sessoes = 
			given()
				.basePath(BASE_PATH)
				.accept(ContentType.JSON)
				.pathParam("idPauta", idPauta)
				.pathParam("idSessao", idSessao)
			.when()
				.get()
			.then()
				.statusCode(HttpStatus.OK.value())
				.extract().body().jsonPath().getList("", VotoResponseDTO.class);

		assertEquals(false, sessoes.isEmpty());
	}

	@Test
	public void cadastraVotoDadosValidos() {
		Long idPauta = IntegrationTestUtils.getIdPauta();
		Long idSessao = IntegrationTestUtils.getIdSessao(idPauta);

		VotoRequestDTO voto = new VotoRequestDTO();
		voto.setCpfAssociado(CPF_VALIDO);
		voto.setVoto(ValorVoto.SIM);

		Long id = 
			given()
				.basePath(BASE_PATH)
				.accept(ContentType.JSON)
				.contentType(ContentType.JSON)
				.pathParam("idPauta", idPauta)
				.pathParam("idSessao", idSessao)
				.body(voto)
			.when()
				.post()
			.then()
				.statusCode(HttpStatus.CREATED.value())
				.extract().body().as(VotoResponseDTO.class).getId();
		
		VotoResponseDTO response = 
			given()
				.basePath(BASE_PATH)
				.accept(ContentType.JSON)
				.contentType(ContentType.JSON)
				.pathParam("idPauta", idPauta)
				.pathParam("idSessao", idSessao)
				.pathParam("idVoto", id)
			.when()
				.get("/{idVoto}")
			.then()
				.statusCode(HttpStatus.OK.value())
				.extract().body().as(VotoResponseDTO.class);
		
		assertEquals(voto.getCpfAssociado(), response.getCpfAssociado());
		assertEquals(voto.getVoto(), response.getVoto());
	}

	@Test
	public void cadastraVotoCpfInvalido() {
		Long idPauta = IntegrationTestUtils.getIdPauta();
		Long idSessao = IntegrationTestUtils.getIdSessao(idPauta);

		VotoRequestDTO voto = new VotoRequestDTO();
		voto.setCpfAssociado(CPF_INVALIDO);
		voto.setVoto(ValorVoto.SIM);

		given()
			.basePath(BASE_PATH)
			.accept(ContentType.JSON)
			.contentType(ContentType.JSON)
			.pathParam("idPauta", idPauta)
			.pathParam("idSessao", idSessao)
			.body(voto)
		.when()
			.post()
		.then()
			.statusCode(HttpStatus.BAD_REQUEST.value());
	}

	@Test
	public void cadastraVotoSemCpf() {
		Long idPauta = IntegrationTestUtils.getIdPauta();
		Long idSessao = IntegrationTestUtils.getIdSessao(idPauta);

		VotoRequestDTO voto = new VotoRequestDTO();
		voto.setVoto(ValorVoto.SIM);

		given()
			.basePath(BASE_PATH)
			.accept(ContentType.JSON)
			.contentType(ContentType.JSON)
			.pathParam("idPauta", idPauta)
			.pathParam("idSessao", idSessao)
			.body(voto)
		.when()
			.post()
		.then()
			.statusCode(HttpStatus.BAD_REQUEST.value());
	}

	@Test
	public void cadastraVotoSemValor() {
		Long idPauta = IntegrationTestUtils.getIdPauta();
		Long idSessao = IntegrationTestUtils.getIdSessao(idPauta);

		VotoRequestDTO voto = new VotoRequestDTO();
		voto.setCpfAssociado(CPF_VALIDO);

		given()
			.basePath(BASE_PATH)
			.accept(ContentType.JSON)
			.contentType(ContentType.JSON)
			.pathParam("idPauta", idPauta)
			.pathParam("idSessao", idSessao)
			.body(voto)
		.when()
			.post()
		.then()
			.statusCode(HttpStatus.BAD_REQUEST.value());
	}

	@Test
	public void cadastraVotoEmSessaoFechada() {
		Long idPauta = IntegrationTestUtils.getIdPauta();
		
		SessaoRequestDTO sessao = new SessaoRequestDTO();
		sessao.setDataHoraAbertura(LocalDateTime.now().plusDays(1));

		Long idSessao = 
			given()
				.basePath(PATH_SESSAO)
				.accept(ContentType.JSON)
				.contentType(ContentType.JSON)
				.body(sessao)
				.pathParam("idPauta", idPauta)
			.when()
				.post()
			.then()
				.statusCode(HttpStatus.CREATED.value())
				.extract().body().as(SessaoResponseDTO.class).getId();


		VotoRequestDTO voto = new VotoRequestDTO();
		voto.setCpfAssociado(CPF_INVALIDO);
		voto.setVoto(ValorVoto.SIM);

		given()
			.basePath(BASE_PATH)
			.accept(ContentType.JSON)
			.contentType(ContentType.JSON)
			.pathParam("idPauta", idPauta)
			.pathParam("idSessao", idSessao)
			.body(voto)
		.when()
			.post()
		.then()
			.statusCode(HttpStatus.BAD_REQUEST.value());
	}

	@Test
	public void cadastraMultiplosVotosComMesmoCpf() {
		Long idPauta = IntegrationTestUtils.getIdPauta();
		Long idSessao = IntegrationTestUtils.getIdSessao(idPauta);

		VotoRequestDTO voto = new VotoRequestDTO();
		voto.setCpfAssociado(CPF_VALIDO);
		voto.setVoto(ValorVoto.SIM);

		given()
			.basePath(BASE_PATH)
			.accept(ContentType.JSON)
			.contentType(ContentType.JSON)
			.pathParam("idPauta", idPauta)
			.pathParam("idSessao", idSessao)
			.body(voto)
		.when()
			.post()
		.then()
			.statusCode(HttpStatus.CREATED.value());
	
		given()
			.basePath(BASE_PATH)
			.accept(ContentType.JSON)
			.contentType(ContentType.JSON)
			.pathParam("idPauta", idPauta)
			.pathParam("idSessao", idSessao)
			.body(voto)
		.when()
			.post()
		.then()
			.statusCode(HttpStatus.BAD_REQUEST.value());
	}
}
