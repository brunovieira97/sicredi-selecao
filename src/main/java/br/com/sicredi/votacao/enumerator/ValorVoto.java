package br.com.sicredi.votacao.enumerator;

import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * ! Problema
 * 
 * Infelizmente, devido aos funcionamentos padrões da JPA, persistir a chave (S ou N)
 * dependeria da implementação de um converter, que não interagiu bem com a transformação
 * de/para JSON no meu primeiro teste.
 * 
 * Vou deixar assim, por enquanto, para buscar uma alternativa.
 * 
 * TODO: Salvar "S" ou "N" no banco
 */
public enum ValorVoto {
	NAO('N'),
	SIM('S');

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
