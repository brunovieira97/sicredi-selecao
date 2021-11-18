package br.com.sicredi.votacao.enumerator;

import com.fasterxml.jackson.annotation.JsonValue;

public enum SessaoStatus {
	ABERTA('A'),
	FECHADA('F');

	private Character value;

	private SessaoStatus(Character value) {
		this.value = value;
	}

	@JsonValue
	public Character getValue() {
		return this.value;
	}

}
