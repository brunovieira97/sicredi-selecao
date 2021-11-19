package br.com.sicredi.votacao.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AssociadoInaptoException extends RuntimeException {
	
	public AssociadoInaptoException() {
		super("Associado inapto para votar.");
	}
}
