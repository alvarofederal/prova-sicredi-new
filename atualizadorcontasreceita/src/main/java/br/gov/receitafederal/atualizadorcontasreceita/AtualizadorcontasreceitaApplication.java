package br.gov.receitafederal.atualizadorcontasreceita;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

// Configuração de inicialização do Microserviço de Sincronização da Receita Federal 
// e configuração para a dispensa de utilização de banco de dados 
@SpringBootApplication
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class,
		DataSourceTransactionManagerAutoConfiguration.class, HibernateJpaAutoConfiguration.class })
public class AtualizadorcontasreceitaApplication {

	private static Logger logger = LoggerFactory.getLogger(AtualizadorcontasreceitaApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(AtualizadorcontasreceitaApplication.class, args);
	}
	
}
