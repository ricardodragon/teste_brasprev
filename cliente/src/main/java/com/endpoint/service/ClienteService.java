package com.endpoint.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.core.model.Cliente;
import com.core.repository.ClienteRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class ClienteService {
	private final ClienteRepository clienteRepository;
	
	public Iterable<Cliente> list(Pageable pageable) {		
		return clienteRepository.findAll();
	}
}
