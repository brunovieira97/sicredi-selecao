package br.com.sicredi.votacao.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SessaoFechadaException extends RuntimeException {

	public SessaoFechadaException() {
		super("A sessão está fechada e não aceita votos.");
	}
}
