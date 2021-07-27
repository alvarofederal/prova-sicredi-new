package br.gov.receitafederal.atualizadorcontasreceita.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.receitafederal.atualizadorcontasreceita.config.message.AtualizadorContaSendMessage;
import br.gov.receitafederal.atualizadorcontasreceita.entity.Conta;
import br.gov.receitafederal.atualizadorcontasreceita.entity.ContaResultado;
import br.gov.receitafederal.atualizadorcontasreceita.exception.ResourceInternalServerErrorException;
import br.gov.receitafederal.atualizadorcontasreceita.exception.ResourceTimeOutException;

/**
 * @author gabriel_stabel<gabriel_stabel@sicredi.com.br>
 */
@Service
public class ReceitaService {

	private static Logger logger = LoggerFactory.getLogger(ReceitaService.class);
	
	private final AtualizadorContaSendMessage contaSendMessage;

	@Autowired
	public ReceitaService(AtualizadorContaSendMessage contaSendMessage) {
		this.contaSendMessage = contaSendMessage;
	}

	// Esta é a implementação interna do "servico" do banco central. Veja o código
	// fonte abaixo para ver os formatos esperados pelo Banco Central neste cenário.
	public boolean atualizarConta(String agencia, String conta, double saldo, String status)
			throws RuntimeException, InterruptedException {

		// Formato agencia: 0000
		if (agencia == null || agencia.length() != 4) {
			logger.info("A Agencia " + agencia + " informada e invalida!");
			return false;
		}

		// Formato conta: 000000
		if (conta == null || conta.length() != 6) {
			logger.info("A Conta " + conta + " informada e invalida!");
			return false;
		}

		// Tipos de status validos:
		List tipos = new ArrayList();
		tipos.add("A");
		tipos.add("I");
		tipos.add("B");
		tipos.add("P");

		if (status == null || !tipos.contains(status)) {
			logger.info("O Status " + status + " da Conta " + conta + " e desconhecido");
			return false;
		} 

		// Simula tempo de resposta do serviço (entre 1 e 5 segundos)
		long wait = Math.round(Math.random() * 4000) + 1000;
		Thread.sleep(wait);

		// Simula cenario de erro no serviço (0,1% de erro)
		long randomError = Math.round(Math.random() * 1000);
		if (randomError == 500) {
			logger.info("Timeout Server - Error 508");
			new ResourceTimeOutException("Timeout Server");
			return false;
		}

		logger.info("Enviando a Conta " + conta + " a Receita Federal! Aguarde o retorno do Status!");
		return true;
	}

	// Metodo responsável pelo envio dos dados para a Exchange da Mensageria - RabbiMQ
	public ContaResultado sendMessage(ContaResultado conta) {
		if (conta != null) {
			contaSendMessage.sendMessage(conta);
		} else {
			logger.info("Nao foi possivel enviar os dados para Sincronizacao com a Receita Federal!");
			new ResourceInternalServerErrorException("Nao foi possivel enviar os dados para Sincronizacao com a Receita Federal!");
		}
		return conta;
	}
}
