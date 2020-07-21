package com.rva.auth.security.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.rva.auth.filter.JwtUsernameAndPasswordAuthenticationFilter;
import com.rva.core.properties.JwtConfiguration;
import com.rva.token.config.SecurityTokenConfig;
import com.rva.token.creator.TokenCreator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableWebSecurity
public class SecurityCredentialsConfig extends SecurityTokenConfig {
	private final JwtConfiguration jwtConfiguration;
	private final UserDetailsService userDetailsService;
	private final TokenCreator tokenCreator;

	public SecurityCredentialsConfig(JwtConfiguration jwtConfiguration, @Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService, TokenCreator tokenCreator) {
		super(jwtConfiguration);
		this.userDetailsService = userDetailsService;
		this.tokenCreator = tokenCreator;
		this.jwtConfiguration = jwtConfiguration;		
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		System.out.println("\n\nTeste\n\n");
		log.info("Configurando http");
		http//.csrf().disable().cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues())
				//.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
				//.exceptionHandling()
				//.authenticationEntryPoint((req, resp, e) -> resp.sendError(HttpServletResponse.SC_UNAUTHORIZED)).and()
				.addFilter(new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager(), jwtConfiguration, tokenCreator));				
				//.authorizeRequests().antMatchers(jwtConfiguration.getLoginUrl()).permitAll()
				//.antMatchers("/loja/admin/**").hasRole("ADMIN").anyRequest().authenticated();
		super.configure(http);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder builder) throws Exception {
		// TODO Auto-generated method stub
		builder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
