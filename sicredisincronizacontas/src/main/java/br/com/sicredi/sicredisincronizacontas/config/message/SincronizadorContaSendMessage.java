package br.com.sicredi.sicredisincronizacontas.config.message;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import br.com.sicredi.sicredisincronizacontas.entity.Conta;

// Componente responsável pelos envios dos dados para a Exchange
@Component
public class SincronizadorContaSendMessage {
	
	// Configuração de acesso a Exchange
	@Value("${sincronizador.rabbitmq.exchange}")
	String exchange;
	
	// Configuração de integração da Exchange a Queue de dados 
	@Value("${sincronizador.rabbitmq.routingkey}")
	String routingkey;
	
	public final RabbitTemplate rabbitTemplate;

	@Autowired
	public SincronizadorContaSendMessage(RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}
	
	// Metodo responsável por envia as mensagens para a Exchange
	public void sendMessage(Conta conta) {
		rabbitTemplate.convertAndSend(exchange, routingkey, conta);
	}
	
}
