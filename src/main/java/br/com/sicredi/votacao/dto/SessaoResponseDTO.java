package br.com.sicredi.votacao.dto;

import java.time.LocalDateTime;

import br.com.sicredi.votacao.enumerator.SessaoStatus;

public class SessaoResponseDTO {
	
	private Long id;
	private LocalDateTime dataHoraAbertura;
	private LocalDateTime dataHoraFechamento;
	private SessaoStatus status;

	public SessaoResponseDTO() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getDataHoraAbertura() {
		return this.dataHoraAbertura;
	}

	public void setDataHoraAbertura(LocalDateTime dataHoraAbertura) {
		this.dataHoraAbertura = dataHoraAbertura;
	}

	public LocalDateTime getDataHoraFechamento() {
		return this.dataHoraFechamento;
	}

	public void setDataHoraFechamento(LocalDateTime dataHoraFechamento) {
		this.dataHoraFechamento = dataHoraFechamento;
	}

	public SessaoStatus getStatus() {
		return this.status;
	}

	public void setStatus(SessaoStatus status) {
		this.status = status;
	}

}
