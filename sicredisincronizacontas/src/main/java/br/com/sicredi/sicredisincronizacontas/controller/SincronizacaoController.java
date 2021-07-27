package br.com.sicredi.sicredisincronizacontas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.sicredi.sicredisincronizacontas.entity.Conta;
import br.com.sicredi.sicredisincronizacontas.service.ReceitaService;

@RestController
@RequestMapping("/sincronizacao")
public class SincronizacaoController {
	
	private final ReceitaService receitaService;
	
	@Autowired
	public SincronizacaoController(ReceitaService receitaService) {
		this.receitaService = receitaService;
	}

	// Metodo que simula a criação de uma API para salvar em Banco de Dados
	@PostMapping(produces = {"application/json","application/xml","application/x-yaml"},
				 consumes = {"application/json","application/xml","application/x-yaml"})
	public Conta create(@RequestBody Conta conta) {
		// Metodo preparado para utilizacao de mensageria pelo uso de uma API
		receitaService.sendMessage(conta);
		return conta;
	}

}
