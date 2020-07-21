package com.rva.auth.filter;

import static java.util.Collections.emptyList;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jwt.SignedJWT;
import com.rva.core.model.ApplicationUser;
import com.rva.core.properties.JwtConfiguration;
import com.rva.token.creator.TokenCreator;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor(onConstructor_= {@Autowired})
public class JwtUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
		
		private final AuthenticationManager authenticationManager;
		private final JwtConfiguration jwtConfiguration;
		private final TokenCreator tokenCreator;
		
		@Override
		@SneakyThrows
		public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
				throws AuthenticationException {
			System.out.println("\n\nTeste\n\n");
			log.info("AttemptAuthentication. . .");
			
			ApplicationUser applicationUser =  new ObjectMapper().readValue(request.getInputStream(), ApplicationUser.class);
			if(applicationUser==null)
				throw new UsernameNotFoundException("Unable to retrieve the username or password.");
			
			log.info("AttemptAuthentication2. . .");
			UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(applicationUser.getUsername(), applicationUser.getPassword(), emptyList());
			
			token.setDetails(applicationUser);
			
			return authenticationManager.authenticate(token);
		}
		
		@Override
		@SneakyThrows
		protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult){
			log.info("Athentication was successful for user '{}', generating JWE token",authResult.getDetails());
			
			SignedJWT jwt = tokenCreator.createSignedJWT(authResult);
			
			String encryptedToken = tokenCreator.encryptToken(jwt);
			
			log.info("Token generated successfully, adding to response header");
			
			response.addHeader("Access-Control-Expose-Headers", "XRSF-TOKEN, "+this.jwtConfiguration.getHeader().getName());
			
			response.addHeader(this.jwtConfiguration.getHeader().getName(), this.jwtConfiguration.getHeader().getPrefix()+encryptedToken);
			
		}
}
