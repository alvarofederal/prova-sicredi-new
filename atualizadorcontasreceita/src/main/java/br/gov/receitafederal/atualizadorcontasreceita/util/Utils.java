package br.gov.receitafederal.atualizadorcontasreceita.util;

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

	public static String resultado(Boolean retorno) {
		return retorno ? "Valida" : "Invalida";
	}
}
