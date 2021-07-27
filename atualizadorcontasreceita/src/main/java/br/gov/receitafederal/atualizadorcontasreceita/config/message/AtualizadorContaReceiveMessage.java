package br.gov.receitafederal.atualizadorcontasreceita.config.message;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import br.gov.receitafederal.atualizadorcontasreceita.entity.Conta;
import br.gov.receitafederal.atualizadorcontasreceita.entity.ContaResultado;
import br.gov.receitafederal.atualizadorcontasreceita.service.ReceitaService;

@Component
public class AtualizadorContaReceiveMessage {

	private static ReceitaService receitaService;

	@Autowired
	public AtualizadorContaReceiveMessage(ReceitaService receitaService) {
		this.receitaService = receitaService;
	}
	/**
	 * @param conta
	 * Metodo responsável por receber os dados da Exchange e responder ao Sincronizador da SICREDI se as contas estão Validadas e Sincronizadas com sucesso!
	 */
	@RabbitListener(queues = { "${sincronizador.rabbitmq.queue}" })
	public void receive(@Payload Conta conta) {
		try {
			// Metodo responsável por sincronizar as contas com a Receita Federal
			boolean flag = receitaService.atualizarConta(conta.getAgencia(), conta.getConta(),
					Double.parseDouble(conta.getSaldo()), conta.getStatus());

			ContaResultado resultado = new ContaResultado();
			resultado.setAgencia(conta.getAgencia());
			resultado.setConta(conta.getConta());
			resultado.setSaldo(conta.getSaldo());
			resultado.setStatus(conta.getStatus());
			resultado.setStatusRetornoReceita(flag);
			validacaoReceitaFederal(resultado);
		} catch (RuntimeException | InterruptedException e) {
		}
	}

	// Metodo responsável pela validação das contas junto a Receita Federal
	public static void validacaoReceitaFederal(ContaResultado conta) throws InterruptedException {
		// Metodo responsável pelo envio dos dados para a Exchange
		receitaService.sendMessage(conta);
	}
}
