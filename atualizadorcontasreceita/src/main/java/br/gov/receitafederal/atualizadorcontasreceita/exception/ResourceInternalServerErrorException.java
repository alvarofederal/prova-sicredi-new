package br.gov.receitafederal.atualizadorcontasreceita.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ResourceInternalServerErrorException extends RuntimeException{

	private static final long serialVersionUID = 458046004678622318L;

	public ResourceInternalServerErrorException(String exception) {
		super(exception);
	}
}
