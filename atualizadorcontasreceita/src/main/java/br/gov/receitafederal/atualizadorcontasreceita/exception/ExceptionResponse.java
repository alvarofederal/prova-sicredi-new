package br.gov.receitafederal.atualizadorcontasreceita.exception;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionResponse implements Serializable{
	private static final long serialVersionUID = -7517048350200374156L;

	private Date timestamp;
	private String messege;
	private String details;
}
