package br.com.sicredi.sicredisincronizacontas.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * 
 * @author alvar
 * Classe responsável por metodos que auxiliam o bom funcionamento e a formatação de dados da aplicação. 
 */
public class Utils {
	
	public static  String formatadorConta(String conta) {
		String contaFormatada = conta.replace("-", "");
		return contaFormatada;
	}
	
	public static  String formatadorSaldo(String saldo) {
		String saldoString = String.valueOf(saldo); 
		String saldoFormatado = saldoString.replace(",", ".");
		return saldoFormatado;
	}

	public static Boolean checarFDS() {
		Calendar data = Calendar.getInstance();
		data.setTime(new Date());

		// Verificação de Final de Semana - Se for Domingo
		if (data.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
			data.add(Calendar.DATE, 1);
			System.out.println("Hoje é Domingo. Volte na Amanhã");
			return false;
		}

		// Verificação de Final de Semana - Se for Sábado
		else if (data.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
			data.add(Calendar.DATE, 2);
			System.out.println("Hoje é Sábado. Volte na Segunda-Feira");
			return false;
		} else {
			return true;
		}
	}
}
