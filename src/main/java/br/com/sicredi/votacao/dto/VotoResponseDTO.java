package br.com.sicredi.votacao.dto;

import br.com.sicredi.votacao.enumerator.ValorVoto;

public class VotoResponseDTO {
	
	private Long id;
	private String cpfAssociado;
	private ValorVoto voto;

	public VotoResponseDTO() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCpfAssociado() {
		return cpfAssociado;
	}

	public void setCpfAssociado(String cpfAssociado) {
		this.cpfAssociado = cpfAssociado;
	}

	public ValorVoto getVoto() {
		return voto;
	}

	public void setVoto(ValorVoto voto) {
		this.voto = voto;
	}

}
