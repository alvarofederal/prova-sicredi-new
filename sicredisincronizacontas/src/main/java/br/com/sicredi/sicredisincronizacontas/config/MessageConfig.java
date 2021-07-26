package br.com.sicredi.sicredisincronizacontas.config;

import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 
 * @author alvar
 * Classe responsável por fazer integração com a Exchenge para enviar as mensagens para o RabbitMQ
 *
 */
@Configuration
public class MessageConfig {

	@Value("${sincronizador.rabbitmq.exchange}")
	String exchange;
	
	@Bean
	public Exchange declareExchange() {
		return ExchangeBuilder.directExchange(exchange).durable(true).build();
	}
	
	@Bean
	public MessageConverter jsonMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}
}
