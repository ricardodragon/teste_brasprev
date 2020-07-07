package com.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.model.Cliente;

public interface ClienteRepository extends PagingAndSortingRepository<Cliente, Long>{

}
