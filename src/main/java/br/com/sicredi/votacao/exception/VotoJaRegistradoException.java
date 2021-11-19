package br.com.sicredi.votacao.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class VotoJaRegistradoException extends RuntimeException {
	
	public VotoJaRegistradoException() {
		super("O voto recebido já foi registrado. Só é permitido um voto para cada associado (CPF) em uma sessão.");
	}
}
