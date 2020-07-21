package com.rva.token.config;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfiguration;

import com.rva.core.properties.JwtConfiguration;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor(onConstructor_ = { @Autowired })
public class SecurityTokenConfig extends WebSecurityConfigurerAdapter {

	private final JwtConfiguration jwtConfiguration;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues())
				.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
				.exceptionHandling()
				.authenticationEntryPoint((req, resp, e) -> resp.sendError(HttpServletResponse.SC_UNAUTHORIZED)).and()
				//.addFilter(new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager(), jwtConfiguration))				
				.authorizeRequests().antMatchers(jwtConfiguration.getLoginUrl()).permitAll()
				.antMatchers("/loja/admin/**").hasRole("ADMIN").anyRequest().authenticated();
	}

}
