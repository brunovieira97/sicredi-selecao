package br.com.sicredi.votacao.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.br.CPF;

import br.com.sicredi.votacao.enumerator.ValorVoto;

public class VotoRequestDTO {
	
	@CPF
	@NotBlank
	private String cpfAssociado;

	@NotNull
	private ValorVoto voto;

	public VotoRequestDTO() {}

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
