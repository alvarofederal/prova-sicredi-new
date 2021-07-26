package br.com.sicredi.sicredisincronizacontas;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

import br.com.sicredi.sicredisincronizacontas.entity.Conta;
import br.com.sicredi.sicredisincronizacontas.exception.ResourceForbiddenException;
import br.com.sicredi.sicredisincronizacontas.service.ReceitaService;
import br.com.sicredi.sicredisincronizacontas.utils.Utils;

//Configuração de inicialização do Microserviço de Envio de Dados para a Receita Federal 
//e configuração para a dispensa de utilização de banco de dados
@SpringBootApplication
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class,
		DataSourceTransactionManagerAutoConfiguration.class, HibernateJpaAutoConfiguration.class })
public class SicredisincronizacontasApplication {

	private static Logger log = LoggerFactory.getLogger(SicredisincronizacontasApplication.class);

	private static ReceitaService receitaService;

	@Autowired
	public SicredisincronizacontasApplication(ReceitaService receitaService) {
		this.receitaService = receitaService;
	}

	public static void main(String[] args) {
		log.info("Inicializando aplicação...");
		SpringApplication.run(SicredisincronizacontasApplication.class, args);

		// Metodo responsável por verificar se é dia util ou final de semana
		if (!Utils.checarFDS()) {
			log.info("Final de Semana - Não autorizada a execução da Sincronização com a Receita Federal!");
			new ResourceForbiddenException(
					"Final de Semana - Não autorizada a execução da Sincronização com a Receita Federal!");
		} else {
			// Nome do arquivo recebido pela retaguarda do SICREDI
			String csvFileName = "arquivo_receita.csv";
			try {
				for (Conta conta : leitorArquivoCSV(csvFileName)) {
					validacaoReceitaFederal(conta);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (RuntimeException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		log.info("Finalizando aplicação...");
	}

	// Metodo responsável pela validação das contas junto a Receita Federal
	public static void validacaoReceitaFederal(Conta conta) throws InterruptedException {
		// Metodo responsável pelo envio dos dados para a Exchange
		receitaService.sendMessage(conta);
		log.info("Enviando a Conta " + conta.getConta() + " à Receita Federal! Aguarde o retorno do Status!");
	}

	// Metodo responsável pela leitura de um arquivo CSV instalado na raiz do projeto
	static List<Conta> leitorArquivoCSV(String csvFileName) throws IOException {
		List<Conta> listaContas = new ArrayList<Conta>();
		log.info("Iniciando a leitura do arquivo .csv...");
		CSVFormat format = CSVFormat.DEFAULT.withDelimiter(';').withFirstRecordAsHeader();
		try (Reader reader = Files.newBufferedReader(Paths.get(csvFileName));
				CSVParser linhas = new CSVParser(reader, format);) {
			for (CSVRecord linha : linhas) {
				String[] campos = linha.get(0).split(";");
				Conta conta = new Conta();
				conta.setAgencia(campos[0]);
				conta.setConta(Utils.formatadorConta(campos[1]));
				conta.setSaldo(Utils.formatadorSaldo(campos[2]));
				conta.setStatus(campos[3]);
				listaContas.add(conta);
			}
		}
		log.info("Finalizando a leitura do arquivo .csv...");
		return listaContas;
	}

}
