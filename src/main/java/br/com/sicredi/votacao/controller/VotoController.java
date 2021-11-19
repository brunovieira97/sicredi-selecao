package br.com.sicredi.votacao.controller;

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

@RestController
@RequestMapping("/pautas/{idPauta}/sessoes/{idSessao}/votos")
@Validated
public class VotoController {
	
	@Autowired
	private VotoService votoService;

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<VotoResponseDTO> list(
		@Valid @Positive @PathVariable Long idPauta,
		@Valid @Positive @PathVariable Long idSessao
	) {
		return this.votoService.list(idPauta, idSessao);
	}

	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public VotoResponseDTO show(
		@Valid @Positive @PathVariable Long idPauta,
		@Valid @Positive @PathVariable Long idSessao,
		@Valid @Positive @PathVariable Long id
	) throws ResourceNotFoundException {
		return this.votoService.show(idPauta, idSessao, id);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public VotoResponseDTO insert(
		@Valid @Positive @PathVariable Long idPauta,
		@Valid @Positive @PathVariable Long idSessao,
		@Valid @RequestBody VotoRequestDTO dto
	) throws ResourceNotFoundException {
		return this.votoService.create(idPauta, idSessao, dto);
	}
}
