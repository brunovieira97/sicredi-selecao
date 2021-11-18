package br.com.sicredi.votacao.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import br.com.sicredi.votacao.dto.SessaoRequestDTO;
import br.com.sicredi.votacao.dto.SessaoResponseDTO;
import br.com.sicredi.votacao.exception.ResourceNotFoundException;
import br.com.sicredi.votacao.model.Pauta;
import br.com.sicredi.votacao.model.Sessao;
import br.com.sicredi.votacao.repository.PautaRepository;
import br.com.sicredi.votacao.repository.SessaoRepository;

@Service
@Validated
public class SessaoService {
	
	@Autowired
	private SessaoRepository sessaoRepository;

	@Autowired
	private PautaRepository pautaRepository;

	@Autowired
	private ModelMapper modelMapper;

	public List<SessaoResponseDTO> list(@Valid @Positive Long idPauta) {
		List<Sessao> sessoes = this.sessaoRepository.findByPautaId(idPauta);

		List<SessaoResponseDTO> dtos = sessoes
			.stream()
			.map(s -> this.modelMapper.map(s, SessaoResponseDTO.class))
			.collect(Collectors.toList());

		return dtos;
	}

	public SessaoResponseDTO show(@Valid @Positive Long idPauta, @Valid @Positive Long id) throws ResourceNotFoundException {
		Sessao sessao = this.sessaoRepository
			.findByIdAndPautaId(id, idPauta)
			.orElseThrow(ResourceNotFoundException::new);
		
		return this.modelMapper.map(sessao, SessaoResponseDTO.class);
	}

	public SessaoResponseDTO create(@Valid @Positive Long idPauta, @Valid SessaoRequestDTO dto) throws ResourceNotFoundException {
		Sessao sessao = this.modelMapper.map(dto, Sessao.class);
		
		Pauta pauta = this.pautaRepository
			.findById(idPauta)
			.orElseThrow(IllegalArgumentException::new);

		sessao.setPauta(pauta);

		Long id = this.sessaoRepository.save(sessao).getId();
		
		return this.show(pauta.getId(), id);
	}

	public void delete(@Valid @Positive Long idPauta, @Valid @Positive Long id) throws ResourceNotFoundException {
		Sessao sessao = this.sessaoRepository
			.findByIdAndPautaId(id, idPauta)
			.orElseThrow(ResourceNotFoundException::new);

		this.sessaoRepository.delete(sessao);
	}
}
