package br.com.sicredi.votacao.dto;

import javax.validation.constraints.NotBlank;

public class PautaRequestDTO {

	@NotBlank
	private String nome;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

}
