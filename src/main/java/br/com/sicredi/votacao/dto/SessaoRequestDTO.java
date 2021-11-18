package br.com.sicredi.votacao.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;

import org.springframework.lang.Nullable;

public class SessaoRequestDTO {
	
	@NotNull
	@FutureOrPresent
	private LocalDateTime dataHoraAbertura;

	@Nullable
	@Future
	private LocalDateTime dataHoraFechamento;

	public SessaoRequestDTO() {}

	public LocalDateTime getDataHoraAbertura() {
		return dataHoraAbertura;
	}

	public void setDataHoraAbertura(LocalDateTime dataHoraAbertura) {
		this.dataHoraAbertura = dataHoraAbertura;
	}

	public LocalDateTime getDataHoraFechamento() {
		return dataHoraFechamento;
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

}
