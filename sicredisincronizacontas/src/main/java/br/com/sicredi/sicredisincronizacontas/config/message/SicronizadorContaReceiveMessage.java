package br.com.sicredi.sicredisincronizacontas.config.message;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.opencsv.CSVWriter;

import br.com.sicredi.sicredisincronizacontas.entity.ContaResultado;
import br.com.sicredi.sicredisincronizacontas.utils.Utils;

/**
 * @author alvar
 * Classe responsável por receber respostas da Receita Federal 
 *
 */
@Component
public class SicronizadorContaReceiveMessage implements Serializable{

	private static final long serialVersionUID = -7816339100542042830L;

	private static Logger logger = LoggerFactory.getLogger(SicronizadorContaReceiveMessage.class);
	
	private  List<ContaResultado> contas = new ArrayList<ContaResultado>();
	
	private ContaResultado conta = new ContaResultado();

	// Metodo responsável por receber os dados da Exchange, responde ao Sincronizador da SICREDI o status das Contas! 
	@RabbitListener(queues = {"${atualizador.rabbitmq.queue}"})
	public void receive(@Payload ContaResultado conta) {
		this.conta = conta;
		contas.add(this.conta);
		if (contas != null) {
			geradorCSVRespostaReceitaFederal(contas);
		}
	}
	
	// Metodo responsável por receber a lista de Contas, retornadas pela Receita Federal é Gerar o Arquivo CSV de resposta
	public void geradorCSVRespostaReceitaFederal(List<ContaResultado> contasInstanciadas) {
		List<String[]> linhas = new ArrayList<>();
		for (ContaResultado linha : contasInstanciadas) {
			linhas.add(new String[] {linha.getAgencia(),Utils.formatadorConta(linha.getConta()),linha.getSaldo(),linha.getStatus(),Utils.resultado(linha.getStatusRetornoReceita())});
		}
		String[] cabecalho = { "agencia", "conta", "saldo", "status", "resultado" };
		try {
			FileWriter escrever = new FileWriter(new File("arquivo_receita_resposta.csv"));
		
			@SuppressWarnings("resource")
			CSVWriter csvWriter = new CSVWriter(escrever,';',
					CSVWriter.NO_QUOTE_CHARACTER,
					CSVWriter.DEFAULT_ESCAPE_CHARACTER,
					CSVWriter.DEFAULT_LINE_END);
			csvWriter.writeNext(cabecalho);
			csvWriter.writeAll(linhas);
			csvWriter.flush();
			escrever.close();
		} catch (RuntimeException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
}
