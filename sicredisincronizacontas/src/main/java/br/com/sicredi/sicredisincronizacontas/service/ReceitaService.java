package br.com.sicredi.sicredisincronizacontas.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.sicredi.sicredisincronizacontas.config.message.SicronizadorContaReceiveMessage;
import br.com.sicredi.sicredisincronizacontas.config.message.SincronizadorContaSendMessage;
import br.com.sicredi.sicredisincronizacontas.entity.Conta;
import br.com.sicredi.sicredisincronizacontas.entity.ContaResultado;
import br.com.sicredi.sicredisincronizacontas.exception.ResourceInternalServerErrorException;

@Service
public class ReceitaService {
	
	private static Logger logger = LoggerFactory.getLogger(ReceitaService.class);

	private final SincronizadorContaSendMessage contaSendMessage;
	
	private final SicronizadorContaReceiveMessage contaReceiveMessage;

	@Autowired
	public ReceitaService(SincronizadorContaSendMessage contaSendMessage, SicronizadorContaReceiveMessage contaReceiveMessage) {
		this.contaSendMessage = contaSendMessage;
		this.contaReceiveMessage = contaReceiveMessage;
	}

	// Metodo responsável pelo envio dos dados para a Exchange da Mensageria - RabbiMQ
	public Conta sendMessage(Conta conta) {
		if (conta != null) {
			contaSendMessage.sendMessage(conta);
		} else {
			logger.info("Não foi possivel enviar os dados para Sincronização com a Receita Federal!");
			new ResourceInternalServerErrorException("Não foi possivel enviar os dados para Sincronização com a Receita Federal!");
		}
		return conta;
	}
	
	// Metodo responsável pelo envio dos dados para a Exchange da Mensageria - RabbiMQ
	public ContaResultado receiveMessage(ContaResultado conta) {
		if (conta != null) {
			contaReceiveMessage.receive(conta);
		} else {
			logger.info("Não foi possivel receber os dados para sincronização com a Receita Federal!");
			new ResourceInternalServerErrorException("Não foi possivel enviar os dados para sincronização com a Receita Federal!");
		}
		return conta;
	}
}
