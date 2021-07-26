package br.gov.receitafederal.atualizadorcontasreceita.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class SicronizadorExceptionHandler extends ResponseEntityExceptionHandler{

	@ExceptionHandler(ResourceBadRequestException.class)
	public final ResponseEntity<ExceptionResponse> handlerBadRequestException(HttpServletRequest req, Exception ex){
		logger.error("Request: " + req.getRequestURL() + " raised " + ex);
		return new ResponseEntity<ExceptionResponse>(HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ResourceInternalServerErrorException.class)
	public final ResponseEntity<ExceptionResponse> handlerInternalServerErrorException(HttpServletRequest req, Exception ex){
		logger.error("Request: " + req.getRequestURL() + " raised " + ex);
		return new ResponseEntity<ExceptionResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(ResourceTimeOutException.class)
	public final ResponseEntity<ExceptionResponse> handlerTimeOutException(HttpServletRequest req, Exception ex){
	    logger.error("Request: " + req.getRequestURL() + " raised " + ex);
		return new ResponseEntity<ExceptionResponse>(HttpStatus.REQUEST_TIMEOUT);
	}
}
