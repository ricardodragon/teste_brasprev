package com.rva.core.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import lombok.*;


@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded =  true)
public class ApplicationUser implements AbstractEntity{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Long id;
	@NotNull(message = "The field 'username' is mandatory")
	@Column(nullable = false)
	private String username;
	@NotNull(message = "The field 'password' is mandatory")
	@Column(nullable = false)
	private String password;
	@NotNull(message = "The field 'role' is mandatory")
	@Column(nullable = false)
	private String role = "USER";
	
	public ApplicationUser(@NotNull ApplicationUser applicationUser) {		
		this.id = applicationUser.getId();
		this.username = applicationUser.getUsername();
		this.password = applicationUser.getPassword();
		this.role = applicationUser.getRole();
	}
	
}
