package br.com.sicredi.votacao.controller.v1;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.sicredi.votacao.dto.SessaoRequestDTO;
import br.com.sicredi.votacao.dto.SessaoResponseDTO;
import br.com.sicredi.votacao.dto.SessaoResultadoResponseDTO;
import br.com.sicredi.votacao.exception.ResourceNotFoundException;
import br.com.sicredi.votacao.service.SessaoService;
import br.com.sicredi.votacao.service.VotoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/v1/pautas/{idPauta}/sessoes")
@Validated
@ApiResponses({
	@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content()),
	@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content())
})
public class SessaoControllerV1 {
	
	@Autowired
	private SessaoService sessaoService;

	@Autowired
	private VotoService votoService;

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	@Operation(
		summary = "Listar Sessões",
		description = "Lista todas as sessões da pauta especificada."
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Sucesso"),
	})
	public List<SessaoResponseDTO> list(
		@Parameter(name = "idPauta", description = "ID da pauta da sessão")
		@Valid @Positive @PathVariable Long idPauta
	) {
		return this.sessaoService.list(idPauta);
	}

	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	@Operation(
		summary = "Listar Sessão por ID",
		description = "Busca a sessão com o ID e pauta especificados."
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Sucesso"),
		@ApiResponse(responseCode = "204", description = "Pauta com este ID não existe", content = @Content())
	})
	public SessaoResponseDTO show(
		@Parameter(name = "idPauta", description = "ID da pauta da sessão")
		@Valid @Positive @PathVariable Long idPauta,
		@Parameter(name = "id", description = "ID da sessão")
		@Valid @Positive @PathVariable Long id
	) throws ResourceNotFoundException {
		return this.sessaoService.show(idPauta, id);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(
		summary = "Cadastrar Sessão",
		description = "Cadastra uma nova sessão para a pauta especificada."
	)
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "Sessão criada"),
	})
	public SessaoResponseDTO insert(
		@Parameter(name = "idPauta", description = "ID da pauta da sessão")
		@Valid @Positive @PathVariable Long idPauta,
		@io.swagger.v3.oas.annotations.parameters.RequestBody(
			description = "DTO que representa a sessão a ser persistida."
		)
		@Parameter(name = "dto", required = true)
		@Valid @RequestBody SessaoRequestDTO dto
	) throws ResourceNotFoundException {
		return this.sessaoService.create(idPauta, dto);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	@Operation(
		summary = "Excluir Sessão",
		description = "Remove uma sessão existente, com base no ID e pauta especificados."
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Pauta excluída"),
		@ApiResponse(responseCode = "204", description = "Pauta com este ID não existe"),
	})
	public void delete(
		@Parameter(name = "idPauta", description = "ID da pauta da sessão")
		@Valid @Positive @PathVariable Long idPauta,
		@Parameter(name = "id", description = "ID da sessão")
		@Valid @Positive @PathVariable Long id
	) throws ResourceNotFoundException {
		this.sessaoService.delete(idPauta, id);
	}

	@GetMapping("/{id}/resultado")
	@ResponseStatus(HttpStatus.OK)
	@Operation(
		summary = "Verificar Resultado",
		description = "Retorna um resumo da sessão especificada pelo ID, com a contagem de votos \"Sim\" e \"Não\"."
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Sucesso"),
	})
	public SessaoResultadoResponseDTO verificarResultado(
		@Parameter(name = "idPauta", description = "ID da pauta da sessão")
		@Valid @Positive @PathVariable Long idPauta,
		@Parameter(name = "id", description = "ID da sessão")
		@Valid @Positive @PathVariable Long id
	) throws ResourceNotFoundException {
		return this.votoService.resultado(idPauta, id);
	}
}
