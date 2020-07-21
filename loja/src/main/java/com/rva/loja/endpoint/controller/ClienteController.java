package com.rva.loja.endpoint.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rva.core.model.Cliente;
import com.rva.loja.endpoint.service.ClienteService;

import lombok.RequiredArgsConstructor;
//import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("v1/admin/loja")
@Slf4j
@RequiredArgsConstructor(onConstructor_= {@Autowired})
public class ClienteController {
	
	private final ClienteService clienteService;
	
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
	public ResponseEntity<Iterable<Cliente>> list(Pageable pageable) {
		return new ResponseEntity<Iterable<Cliente>>(this.clienteService.list(pageable), HttpStatus.OK);
	}
	
}
