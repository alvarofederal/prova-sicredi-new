package br.com.sicredi.sicredisincronizacontas.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ContaResultado implements Serializable {

	private static final long serialVersionUID = -6433314638709193615L;

	private String agencia;

	private String conta;

	private String saldo;

	private String status;

	private Boolean statusRetornoReceita;

	public ContaResultado() {

	}

	public ContaResultado(String agencia, String conta, String saldo, String status, Boolean statusRetornoReceita) {
		this.agencia = agencia;
		this.conta = conta;
		this.saldo = saldo;
		this.status = status;
		this.statusRetornoReceita = statusRetornoReceita;
	}
}
