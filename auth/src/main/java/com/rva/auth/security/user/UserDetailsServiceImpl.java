package com.rva.auth.security.user;

import java.util.Collection;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.rva.core.model.ApplicationUser;
import com.rva.core.repository.ApplicationUserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class UserDetailsServiceImpl implements UserDetailsService{
	
	private final ApplicationUserRepository applicationUserRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		ApplicationUser applicationUser = applicationUserRepository.findByUsername(username);
		if(applicationUser==null)
			throw new UsernameNotFoundException(String.format("User '%s' not found", username));
		return new CustomUserDetails(applicationUser);
	}
	
	public static final class CustomUserDetails extends ApplicationUser implements UserDetails {
		
		public CustomUserDetails(@NotNull ApplicationUser applicationUser) {
			super(applicationUser);
		}
		
		public Collection<? extends GrantedAuthority> getAuthorities() {
			return AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_"+this.getRole());
		}
		
		@Override
		public boolean isAccountNonExpired() {
			// TODO Auto-generated method stub
			return true;
		}
		
		@Override
		public boolean isAccountNonLocked() {
			// TODO Auto-generated method stub
			return true;
		}
		
		@Override
		public boolean isCredentialsNonExpired() {
			// TODO Auto-generated method stub
			return true;
		}
		
		@Override
		public boolean isEnabled() {
			// TODO Auto-generated method stub
			return true;
		}
	}
}