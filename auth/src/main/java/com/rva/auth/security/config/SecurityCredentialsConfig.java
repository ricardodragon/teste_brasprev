package com.rva.auth.security.config;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import com.rva.auth.filter.JwtUsernameAndPasswordAuthenticationFilter;
import com.rva.core.properties.JwtConfiguration;

import lombok.RequiredArgsConstructor;

@EnableWebSecurity
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class SecurityCredentialsConfig extends WebSecurityConfigurerAdapter{
	
	private final UserDetailsService userDetailsService;
	private final JwtConfiguration jwtConfiguration;
	@Override
	protected void configure(HttpSecurity http) throws Exception{
		http
			.csrf().disable()
			.cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues())
			.and()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
				.exceptionHandling().authenticationEntryPoint((req, resp, e) -> resp.sendError(HttpServletResponse.SC_UNAUTHORIZED))
			.and()
				.addFilter(new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager(), jwtConfiguration))
			.authorizeRequests()
				.antMatchers(jwtConfiguration.getLoginUrl()).permitAll()
				.antMatchers("/loja/admin/**").hasRole("ADMIN")
				.anyRequest().authenticated();
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
