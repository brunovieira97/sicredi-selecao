package br.com.sicredi.votacao.enumerator;

import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ValorVoto {
	SIM('S'),
	NAO('N');

	private Character value;

	private ValorVoto(Character value) {
		this.value = value;
	}

	@JsonValue
	public Character getValue() {
		return this.value;
	}

	@JsonCreator
    public static ValorVoto getValorVotoFromString(Character value) {
		if (value == null) {
			return null;
		}

		return Stream.of(ValorVoto.values())
			.filter(v -> v.getValue().equals(value))
			.findFirst()
			.orElseThrow(IllegalArgumentException::new);
    }

}
