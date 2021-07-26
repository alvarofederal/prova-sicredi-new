package br.gov.receitafederal.atualizadorcontasreceita.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Conta implements Serializable{

	private static final long serialVersionUID = -2734339588602251080L;

	private String agencia;
	
	private String conta;
	
	private String saldo;
	
	private String status;
	
	private Boolean statusRetornoReceita;
	
    public Conta() {

    }
 
    public Conta(String agencia, String conta, String saldo, String status, Boolean statusRetornoReceita) {
        this.agencia = agencia;
        this.conta = conta;
        this.saldo = saldo;
        this.status = status;
        this.statusRetornoReceita = statusRetornoReceita;
    }
}
