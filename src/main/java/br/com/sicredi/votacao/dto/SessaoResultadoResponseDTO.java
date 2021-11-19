package br.com.sicredi.votacao.dto;

public class SessaoResultadoResponseDTO {
	private Integer sim;
	private Integer nao;

	public SessaoResultadoResponseDTO() {}

	public Integer getSim() {
		return sim;
	}

	public void setSim(Integer sim) {
		this.sim = sim;
	}

	public Integer getNao() {
		return nao;
	}

	public void setNao(Integer nao) {
		this.nao = nao;
	}

	
}
