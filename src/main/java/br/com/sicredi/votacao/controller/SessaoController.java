package br.com.sicredi.votacao.controller;

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
import br.com.sicredi.votacao.exception.ResourceNotFoundException;
import br.com.sicredi.votacao.service.SessaoService;

@RestController
@RequestMapping("/pautas/{idPauta}/sessoes")
@Validated
public class SessaoController {
	
	@Autowired
	private SessaoService sessaoService;

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<SessaoResponseDTO> list(@Valid @Positive @PathVariable Long idPauta) {
		return this.sessaoService.list(idPauta);
	}

	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public SessaoResponseDTO show(
		@Valid @Positive @PathVariable Long idPauta,
		@Valid @Positive @PathVariable Long id
	) throws ResourceNotFoundException {
		return this.sessaoService.show(idPauta, id);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public SessaoResponseDTO insert(
		@Valid @Positive @PathVariable Long idPauta,
		@Valid @RequestBody SessaoRequestDTO dto
	) throws ResourceNotFoundException {
		return this.sessaoService.create(idPauta, dto);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void delete(
		@Valid @Positive @PathVariable Long idPauta,
		@Valid @Positive @PathVariable Long id
	) throws ResourceNotFoundException {
		this.sessaoService.delete(idPauta, id);
	}
}
