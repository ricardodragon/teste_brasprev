package com.rva.core.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.rva.core.model.ApplicationUser;

public interface ApplicationUserRepository extends PagingAndSortingRepository<ApplicationUser, Long>{
	ApplicationUser findByUsername(String username);
}
