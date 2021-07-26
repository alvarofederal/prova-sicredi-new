package br.gov.receitafederal.atualizadorcontasreceita.config.message;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import br.gov.receitafederal.atualizadorcontasreceita.entity.Conta;
import br.gov.receitafederal.atualizadorcontasreceita.service.ReceitaService;

@Component
public class ContaReceiveMessage {
	
	// Metodo responsável por receber os dados da Exchange e responder ao Sincronizador da SICREDI se as contas estão Validadas e Sincronizadas com sucesso! 
	@RabbitListener(queues = {"${sincronizador.rabbitmq.queue}"})
	public void receive(@Payload Conta conta) {
		ReceitaService receitaService = new ReceitaService();
		try {
			// Metodo responsável por sincronizar as contas com a Receita Federal
			receitaService.atualizarConta(conta.getAgencia(), conta.getConta(), Double.parseDouble(conta.getSaldo()), conta.getStatus());
		} catch (RuntimeException | InterruptedException e) {
			
		}
	}
}
