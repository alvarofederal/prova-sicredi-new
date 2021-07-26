package br.gov.receitafederal.atualizadorcontasreceita.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import br.gov.receitafederal.atualizadorcontasreceita.exception.ResourceInternalServerErrorException;
import br.gov.receitafederal.atualizadorcontasreceita.exception.ResourceTimeOutException;

/**
 * @author gabriel_stabel<gabriel_stabel@sicredi.com.br>
 */
@Service
public class ReceitaService {

	private static Logger log = LoggerFactory.getLogger(ReceitaService.class);

	// Esta é a implementação interna do "servico" do banco central. Veja o código
	// fonte abaixo para ver os formatos esperados pelo Banco Central neste cenário.
	public boolean atualizarConta(String agencia, String conta, double saldo, String status)
			throws RuntimeException, InterruptedException {

		// Formato agencia: 0000
		if (agencia == null || agencia.length() != 4) {
			log.info("A Agência " + agencia + " informada é inválida! - Error 500");
			return false;
		}

		// Formato conta: 000000
		if (conta == null || conta.length() != 6) {
			log.info("A Conta " + conta + " informada é inválida! - Error 500");
			new ResourceInternalServerErrorException("A Conta " + conta + " informada está com formato inválido!");
			return false;
		}

		// Tipos de status validos:
		List tipos = new ArrayList();
		tipos.add("A");
		tipos.add("I");
		tipos.add("B");
		tipos.add("P");

		if (status == null || !tipos.contains(status)) {
			log.info("O Status " + status + " da Conta " + conta + " é desconhecido! - Error 500");
			new ResourceInternalServerErrorException("O Status " + status + " da Conta " + conta + " é desconhecido!");
			return false;
		} 

		// Simula tempo de resposta do serviço (entre 1 e 5 segundos)
		long wait = Math.round(Math.random() * 4000) + 1000;
		Thread.sleep(wait);

		// Simula cenario de erro no serviço (0,1% de erro)
		long randomError = Math.round(Math.random() * 1000);
		if (randomError == 500) {
			log.info("Timeout Server - Error 508");
			new ResourceTimeOutException("Timeout Server - Error 508");
			return false;
		}

		log.info("A Conta " + conta + " esta sincronizada com sucesso na Receita Federal!");
		return true;
	}
}
