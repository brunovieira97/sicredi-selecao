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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.sicredi.votacao.dto.PautaRequestDTO;
import br.com.sicredi.votacao.dto.PautaResponseDTO;
import br.com.sicredi.votacao.exception.ResourceNotFoundException;
import br.com.sicredi.votacao.service.PautaService;

@RestController
@RequestMapping("/pautas")
@Validated
public class PautaController {
	
	@Autowired
	private PautaService pautaService;

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<PautaResponseDTO> list() {
		return this.pautaService.list();
	}
	
	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public PautaResponseDTO show(@PathVariable @Valid @Positive Long id) throws ResourceNotFoundException {
		return this.pautaService.show(id);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public PautaResponseDTO insert(@RequestBody @Valid PautaRequestDTO pauta) throws ResourceNotFoundException {
		return this.pautaService.create(pauta);
	}

	@PutMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public PautaResponseDTO update(@PathVariable @Valid @Positive Long id, @RequestBody @Valid PautaRequestDTO pauta) throws ResourceNotFoundException {
		return this.pautaService.update(id, pauta);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void delete(@PathVariable @Valid @Positive Long id) throws ResourceNotFoundException {
		this.pautaService.delete(id);
	}
}
