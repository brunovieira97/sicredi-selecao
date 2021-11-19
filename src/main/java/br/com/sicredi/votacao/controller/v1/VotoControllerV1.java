package br.com.sicredi.votacao.controller.v1;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.sicredi.votacao.dto.VotoRequestDTO;
import br.com.sicredi.votacao.dto.VotoResponseDTO;
import br.com.sicredi.votacao.exception.ResourceNotFoundException;
import br.com.sicredi.votacao.service.VotoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/v1/pautas/{idPauta}/sessoes/{idSessao}/votos")
@Validated
@ApiResponses({
	@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content()),
	@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content())
})
public class VotoControllerV1 {
	
	@Autowired
	private VotoService votoService;

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	@Operation(
		summary = "Listar Votos da Sessão",
		description = "Lista todos os votos da sessão especificada."
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Sucesso"),
	})
	public List<VotoResponseDTO> list(
		@Parameter(name = "idPauta", description = "ID da pauta da sessão")
		@Valid @Positive @PathVariable Long idPauta,
		@Parameter(name = "idSessao", description = "ID da sessão")
		@Valid @Positive @PathVariable Long idSessao
	) throws ResourceNotFoundException {
		return this.votoService.list(idPauta, idSessao);
	}

	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	@Operation(
		summary = "Listar Voto por ID",
		description = "Busca o voto com o ID da sessão e pauta especificados."
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Sucesso"),
		@ApiResponse(responseCode = "204", description = "Voto com este ID não existe", content = @Content())
	})
	public VotoResponseDTO show(
		@Parameter(name = "idPauta", description = "ID da pauta da sessão", required = true)
		@Valid @Positive @PathVariable Long idPauta,
		@Parameter(name = "idSessao", description = "ID da sessão", required = true)
		@Valid @Positive @PathVariable Long idSessao,
		@Parameter(name = "id", description = "ID do voto", required = true)
		@Valid @Positive @PathVariable Long id
	) throws ResourceNotFoundException {
		return this.votoService.show(idPauta, idSessao, id);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(
		summary = "Cadastrar Voto",
		description = "Cadastra um voto para a sessão especificada."
	)
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "Voto criado"),
	})
	public VotoResponseDTO insert(
		@Parameter(name = "idPauta", description = "ID da pauta da sessão")
		@Valid @Positive @PathVariable Long idPauta,
		@Parameter(name = "idSessao", description = "ID da sessão")
		@Valid @Positive @PathVariable Long idSessao,
		@io.swagger.v3.oas.annotations.parameters.RequestBody(
			description = "DTO que representa o voto a ser persistido."
		)
		@Parameter(name = "dto", required = true)
		@Valid @RequestBody VotoRequestDTO dto
	) throws ResourceNotFoundException {
		return this.votoService.create(idPauta, idSessao, dto);
	}
}
