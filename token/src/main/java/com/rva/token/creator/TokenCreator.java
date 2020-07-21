package com.rva.token.creator;

import static java.util.stream.Collectors.toList;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.rva.core.model.ApplicationUser;
import com.rva.core.properties.JwtConfiguration;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class TokenCreator {
	private final JwtConfiguration jwtConfiguration;
	@SneakyThrows
	public SignedJWT createSignedJWT(Authentication authentication){
		
		log.info("Start create the signed JWT");
		
		ApplicationUser applicationUser = (ApplicationUser) authentication.getPrincipal();
		JWTClaimsSet jwtClaimsSet = createJWTClaimsSet(authentication, applicationUser);
		KeyPair keyPair = generateKeyPair();
		
		log.info("Building JWK from RSA Keys");
		
		JWK jwk = new RSAKey.Builder((RSAPublicKey) keyPair.getPublic()).keyID(UUID.randomUUID().toString()).build();
		SignedJWT jwt = new SignedJWT(new JWSHeader.Builder(JWSAlgorithm.RS256)
				.jwk(jwk)
				.build(), jwtClaimsSet);
		
		log.info("Signing the token with private RSA key");
		
		RSASSASigner signer = new RSASSASigner(keyPair.getPrivate());			
		jwt.sign(signer);
		
		log.info("Serialized token '{}'", jwt.serialize());
		
		return jwt;
	}
	
	private JWTClaimsSet createJWTClaimsSet(Authentication authentication,ApplicationUser applicationUser) {
		log.info("Creating JWTClaimSet Object for '{}'", applicationUser);
		return new JWTClaimsSet.Builder()
				.subject(applicationUser.getUsername())
				.claim("authorities", 
						authentication.getAuthorities()
							.stream()
							.map(GrantedAuthority::getAuthority)
							.collect(toList()))
				.issuer("http://loja.com")
				.issueTime(new Date())
				.expirationTime(new Date(System.currentTimeMillis() + (jwtConfiguration.getExpiration() * 1000)))
				.build();
	}
	
	@SneakyThrows
	private KeyPair generateKeyPair() {
		log.info("Generate RSA 2048 bits Keys");
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		keyPairGenerator.initialize(2048);
		return keyPairGenerator.genKeyPair();			
	}
	
	public String encryptToken(SignedJWT signedJWT) throws JOSEException {
		
		log.info("Starting encypt method");
		
		DirectEncrypter directEncryter = new DirectEncrypter(this.jwtConfiguration.getKey().getBytes());
		
		JWEObject jweObject = new JWEObject(new JWEHeader.Builder(JWEAlgorithm.DIR, EncryptionMethod.A128CBC_HS256)
				.contentType("JWT")
				.build(), new Payload(signedJWT));
		log.info("Encypiting token with system's private key");
		
		jweObject.encrypt(directEncryter);
		
		log.info("Token encripted");
		
		return jweObject.serialize();
	}

}
