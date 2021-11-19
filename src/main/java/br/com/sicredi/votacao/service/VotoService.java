package br.com.sicredi.votacao.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import br.com.sicredi.votacao.dto.VotoRequestDTO;
import br.com.sicredi.votacao.dto.VotoResponseDTO;
import br.com.sicredi.votacao.enumerator.SessaoStatus;
import br.com.sicredi.votacao.exception.ResourceNotFoundException;
import br.com.sicredi.votacao.exception.SessaoFechadaException;
import br.com.sicredi.votacao.exception.VotoJaRegistradoException;
import br.com.sicredi.votacao.model.Sessao;
import br.com.sicredi.votacao.model.Voto;
import br.com.sicredi.votacao.repository.SessaoRepository;
import br.com.sicredi.votacao.repository.VotoRepository;

@Service
@Validated
public class VotoService {
	
	@Autowired
	private VotoRepository votoRepository;

	@Autowired
	private SessaoRepository sessaoRepository;

	@Autowired
	private ModelMapper modelMapper;

	public List<VotoResponseDTO> list(@Valid @Positive Long idPauta, @Valid @Positive Long idSessao) {
		List<Voto> votos = this.votoRepository.findBySessaoPautaIdAndSessaoId(idPauta, idSessao);

		List<VotoResponseDTO> dtos = votos
			.stream()
			.map(v -> this.modelMapper.map(v, VotoResponseDTO.class))
			.collect(Collectors.toList());

		return dtos;
	}

	public VotoResponseDTO show(
		@Valid @Positive Long idPauta,
		@Valid @Positive Long idSessao,
		@Valid @Positive Long id
	) throws ResourceNotFoundException {
		Voto voto = this.votoRepository
			.findBySessaoPautaIdAndSessaoIdAndId(idPauta, idSessao, id)
			.orElseThrow(ResourceNotFoundException::new);

		return this.modelMapper.map(voto, VotoResponseDTO.class);
	}

	public VotoResponseDTO create(
		@Valid @Positive Long idPauta,
		@Valid @Positive Long idSessao,
		@Valid VotoRequestDTO dto
	) throws ResourceNotFoundException {
		if (
			this.votoRepository
			.findBySessaoPautaIdAndSessaoIdAndCpfAssociado(idPauta, idSessao, dto.getCpfAssociado())
			.isPresent()
		) {
			throw new VotoJaRegistradoException();
		}

		Voto voto = this.modelMapper.map(dto, Voto.class);

		Sessao sessao = this.sessaoRepository
			.findByIdAndPautaId(idSessao, idPauta)
			.orElseThrow(ResourceNotFoundException::new);

		if (sessao.getStatus().equals(SessaoStatus.FECHADA)) {
			throw new SessaoFechadaException();
		}

		voto.setSessao(sessao);

		Long id = this.votoRepository.save(voto).getId();

		return this.show(idPauta, idSessao, id);
	}

}
