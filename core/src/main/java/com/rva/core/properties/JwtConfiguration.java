package com.rva.core.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Configuration
@ConfigurationProperties(prefix = "jwt.config")
@Getter
@Setter
@ToString
public class JwtConfiguration {
	private String loginUrl = "/login/**";
	@NestedConfigurationProperty
	private Header header = new Header();
	private int expiration = 3600;
	private String  key = "H+MbPeShVmYq3t6w9z$C&F)J@NcRfTjW";
	private String type = "encripited";
		
	@Getter
	@Setter
	public class Header { 
		private String name = "Authorization";
		private String prefix = "Bearer ";
	}
}
