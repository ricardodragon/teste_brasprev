package com.rva.auth.test;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class AuthApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void crypt() {
		System.out.println("Senha : "+new BCryptPasswordEncoder().encode("Fuego@90"));
	}
}
