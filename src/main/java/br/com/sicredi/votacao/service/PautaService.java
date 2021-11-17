package br.com.sicredi.votacao.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import br.com.sicredi.votacao.dto.PautaRequestDTO;
import br.com.sicredi.votacao.dto.PautaResponseDTO;
import br.com.sicredi.votacao.exception.ResourceNotFoundException;
import br.com.sicredi.votacao.model.Pauta;
import br.com.sicredi.votacao.repository.PautaRepository;

@Service
@Validated
public class PautaService {
	
	@Autowired
	private PautaRepository pautaRepository;

	@Autowired
	private ModelMapper modelMapper;

	public PautaResponseDTO create(@Valid PautaRequestDTO dto) throws ResourceNotFoundException {
		Pauta pauta = this.modelMapper.map(dto, Pauta.class);

		Long id = this.pautaRepository.save(pauta).getId();
		
		return this.show(id);
	}

	public PautaResponseDTO update(@Valid @Positive Long id, @Valid PautaRequestDTO dto) throws ResourceNotFoundException  {
		Pauta original = this.pautaRepository
			.findById(id)
			.orElseThrow(ResourceNotFoundException::new);
		
		this.modelMapper.map(dto, original);

		this.pautaRepository.save(original);

		return this.show(id);
	}

	public List<PautaResponseDTO> list() {
		List<Pauta> pautas = this.pautaRepository.findAll();

		List<PautaResponseDTO> dtos = pautas
			.stream()
			.map(p -> this.modelMapper.map(p, PautaResponseDTO.class))
			.collect(Collectors.toList());

		return dtos;
	}

	public PautaResponseDTO show(@Valid @Positive Long id) throws ResourceNotFoundException {
		Pauta pauta = this.pautaRepository
			.findById(id)
			.orElseThrow(ResourceNotFoundException::new);
		
		return this.modelMapper.map(pauta, PautaResponseDTO.class);
	}

	public void delete(@Valid @Positive Long id) throws ResourceNotFoundException {
		Pauta pauta = this.pautaRepository
			.findById(id)
			.orElseThrow(ResourceNotFoundException::new);

		this.pautaRepository.delete(pauta);
	}
}
