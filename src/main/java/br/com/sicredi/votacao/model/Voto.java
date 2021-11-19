package br.com.sicredi.votacao.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.br.CPF;

import br.com.sicredi.votacao.enumerator.ValorVoto;

@Entity
@Table(uniqueConstraints = {
	@UniqueConstraint(columnNames = {
		"cpfAssociado",
		"sessao_id"
	})
})
public class Voto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@CPF
	@NotBlank
	private String cpfAssociado;
	
	@NotNull
	private ValorVoto valorVoto;

	@ManyToOne(
		cascade = CascadeType.REMOVE,
		fetch = FetchType.LAZY,
		optional = false
	)
	private Sessao sessao;

	public Voto() {}

	public Long getId() {
		return this.id;
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

	public ValorVoto getValorVoto() {
		return valorVoto;
	}

	public void setValorVoto(ValorVoto valorVoto) {
		this.valorVoto = valorVoto;
	}

	public Sessao getSessao() {
		return sessao;
	}

	public void setSessao(Sessao sessao) {
		this.sessao = sessao;
	}

}
