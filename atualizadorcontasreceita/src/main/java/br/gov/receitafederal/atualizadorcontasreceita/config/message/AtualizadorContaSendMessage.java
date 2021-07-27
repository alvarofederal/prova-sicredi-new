package br.gov.receitafederal.atualizadorcontasreceita.config.message;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import br.gov.receitafederal.atualizadorcontasreceita.entity.ContaResultado;

// Componente responsável pelos envios dos dados para a Exchange
@Component
public class AtualizadorContaSendMessage {
	
	// Configuração de acesso a Exchange da Receita Federal
	@Value("${atualizador.rabbitmq.exchange}")
	String exchange;
	
	// Configuração de integração da Exchange a Queue de dados 
	@Value("${atualizador.rabbitmq.routingkey}")
	String routingkey;
	
	public final RabbitTemplate rabbitTemplate;

	@Autowired
	public AtualizadorContaSendMessage(RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}
	
	// Metodo responsável por envia as mensagens para a Exchange
	public void sendMessage(ContaResultado conta) {
		rabbitTemplate.convertAndSend(exchange, routingkey, conta);
	}
	
}
