package com.core.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.core.model.Cliente;

public interface ClienteRepository extends PagingAndSortingRepository<Cliente, Long>{

}
