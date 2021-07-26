package br.com.sicredi.sicredisincronizacontas.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceBadRequestException extends RuntimeException{

	private static final long serialVersionUID = 458046004678622318L;

	public ResourceBadRequestException(String exception) {
		super(exception);
	}
}
