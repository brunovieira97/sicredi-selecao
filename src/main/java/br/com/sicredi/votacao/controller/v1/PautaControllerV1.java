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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.sicredi.votacao.dto.PautaRequestDTO;
import br.com.sicredi.votacao.dto.PautaResponseDTO;
import br.com.sicredi.votacao.exception.ResourceNotFoundException;
import br.com.sicredi.votacao.service.PautaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/v1/pautas")
@Validated
@ApiResponses({
	@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content()),
	@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content())
})
public class PautaControllerV1 {
	
	@Autowired
	private PautaService pautaService;

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	@Operation(
		summary = "Listar Pautas",
		description = "Lista todas as pautas cadastradas."
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Sucesso"),
	})
	public List<PautaResponseDTO> list() {
		return this.pautaService.list();
	}
	
	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	@Operation(
		summary = "Listar Pauta por ID",
		description = "Busca a pauta com o ID especificado."
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Sucesso"),
		@ApiResponse(responseCode = "204", description = "Pauta com este ID não existe", content = @Content())
	})
	public PautaResponseDTO show(
		@Parameter(name = "id", description = "ID da pauta a ser retornada", required = true)
		@PathVariable @Valid @Positive Long id
	) throws ResourceNotFoundException {
		return this.pautaService.show(id);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(
		summary = "Cadastrar Pauta",
		description = "Cadastra uma nova pauta."
	)
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "Pauta criada"),
	})
	public PautaResponseDTO insert(
		@io.swagger.v3.oas.annotations.parameters.RequestBody(
			description = "DTO que representa a pauta a ser persistida."
		)
		@Parameter(name = "pauta", required = true)
		@RequestBody @Valid PautaRequestDTO pauta
	) throws ResourceNotFoundException {
		return this.pautaService.create(pauta);
	}

	@PutMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	@Operation(
		summary = "Atualizar Pauta",
		description = "Atualiza uma pauta existente, com base no ID especificado."
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Pauta atualizada"),
		@ApiResponse(responseCode = "204", description = "Pauta com este ID não existe"),
	})
	public PautaResponseDTO update(
		@Parameter(name = "id", description = "ID da pauta a ser atualizada")
		@PathVariable @Valid @Positive Long id,
		@io.swagger.v3.oas.annotations.parameters.RequestBody(
			description = "DTO que representa o objeto de Pauta a persistir (com campos inalterados também)."
		)
		@Parameter(name = "dto", required = true)
		@RequestBody @Valid PautaRequestDTO pauta
	) throws ResourceNotFoundException {
		return this.pautaService.update(id, pauta);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	@Operation(
		summary = "Excluir Pauta",
		description = "Remove uma pauta existente, com base no ID especificado."
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Pauta excluída"),
		@ApiResponse(responseCode = "204", description = "Pauta com este ID não existe"),
	})
	public void delete(
		@Parameter(name = "id", description = "ID da pauta a ser excluída")
		@PathVariable @Valid @Positive Long id
	) throws ResourceNotFoundException {
		this.pautaService.delete(id);
	}
}
