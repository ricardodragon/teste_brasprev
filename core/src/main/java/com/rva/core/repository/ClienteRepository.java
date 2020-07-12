package com.rva.core.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.rva.core.model.Cliente;

public interface ClienteRepository extends PagingAndSortingRepository<Cliente, Long>{

}
