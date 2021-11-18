package br.com.sicredi.votacao.model;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Transient;
import org.springframework.lang.Nullable;

import br.com.sicredi.votacao.enumerator.SessaoStatus;

@Entity
public class Sessao {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@FutureOrPresent
	private LocalDateTime dataHoraAbertura;

	@Nullable
	@Future
	private LocalDateTime dataHoraFechamento;

	@Transient
	private transient SessaoStatus status;

	@ManyToOne(
		cascade = CascadeType.REMOVE,
		optional = false,
		fetch = FetchType.LAZY
	)
	private Pauta pauta;

	public Sessao() {}

	@PostLoad
	private void postLoad() {
		this.setupStatus();
	}

	@PrePersist
	private void prePersist() {
		this.setupDataHoraFechamento();
	}

	private void setupStatus() {
		this.status = SessaoStatus.FECHADA;

		LocalDateTime now = LocalDateTime.now();

		if (now.isAfter(this.getDataHoraAbertura()) && now.isBefore(this.getDataHoraFechamento())) {
			this.status = SessaoStatus.ABERTA;
		}
	}

	private void setupDataHoraFechamento() {
		if (this.getDataHoraFechamento() == null) {
			LocalDateTime fechamento = this.getDataHoraAbertura().plusMinutes(1L);
			
			this.setDataHoraFechamento(fechamento);
		}
	}
	
	public Long getId() {
		return this.id;
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
		if (
			(
				dataHoraAbertura != null
				&& dataHoraFechamento != null
			) && (
				dataHoraFechamento.isBefore(this.getDataHoraAbertura())
				|| dataHoraFechamento.isEqual(this.getDataHoraAbertura())
			)
		) {
			throw new IllegalArgumentException();
		}

		this.dataHoraFechamento = dataHoraFechamento;
	}

	public SessaoStatus getStatus() {
		this.setupStatus();

		return this.status;
	}

	public Pauta getPauta() {
		return this.pauta;
	}

	public void setPauta(Pauta pauta) {
		this.pauta = pauta;
	}

}
