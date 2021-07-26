package br.com.sicredi.sicredisincronizacontas.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.sicredi.sicredisincronizacontas.config.message.ContaSendMessage;
import br.com.sicredi.sicredisincronizacontas.entity.Conta;
import br.com.sicredi.sicredisincronizacontas.exception.ResourceInternalServerErrorException;

@Service
public class ReceitaService {

	private final ContaSendMessage contaSendMessage;

	@Autowired
	public ReceitaService(ContaSendMessage contaSendMessage) {
		this.contaSendMessage = contaSendMessage;
	}

	// Metodo responsável pelo envio dos dados para a Exchange da Mensageria - RabbiMQ
	public Conta sendMessage(Conta conta) {
		if (conta != null) {
			contaSendMessage.sendMessage(conta);
		} else {
			new ResourceInternalServerErrorException("Não foi possivel enviar os dados para Sincronização com a Receita Federal");
		}
		return conta;
	}
}
