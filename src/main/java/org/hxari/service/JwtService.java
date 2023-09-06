package org.hxari.service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	@Value( "${jwt.expirationMs}" )
	protected int EXPIRATION_MS;
	
	@Value( "${jwt.secret}" )
	protected String SECRET;

	public String extractUsername( String token ) {
		return( this.extractClaim( token, Claims::getSubject ) );
	}

	public Date extractExpiration( String token ) {
		return( this.extractClaim( token, Claims::getExpiration ) );
	}

	public <T> T extractClaim( String token, Function<Claims, T> resolver ) {
		final Claims claims = this.extractAllClaims( token );
		return( resolver.apply( claims ) );
	}

	private Claims extractAllClaims( String token ) {
		return( Jwts
			.parserBuilder()
			.setSigningKey( this.getSignKey() )
			.build()
			.parseClaimsJws( token )
			.getBody()
		);
	}

	public String generateToken( String username ) {
		Map<String, Object> claims = new HashMap<>();
		return( this.generateClaimsToken( username, claims ) );
	}

	private String generateClaimsToken( String username, Map<String, Object> claims ) {
		return( Jwts
			.builder()
			.setClaims( claims )
			.setSubject( username )
			.setIssuedAt( new Date( System.currentTimeMillis() ) )
			.setExpiration( new Date( System.currentTimeMillis() + this.EXPIRATION_MS ) )
			.signWith( this.getSignKey(), SignatureAlgorithm.HS512 )
			.compact()
		);
	}

	private Key getSignKey() {
		byte[] keyBytes = Decoders.BASE64.decode( this.SECRET );
		return Keys.hmacShaKeyFor( keyBytes );
	}

	private Boolean isTokenExpired( String token ) {
		return( this.extractExpiration( token ).before( new Date() ) );
	}

	public Boolean validateToken( String token, UserDetails user ) {
		final String username = extractUsername( token );
		return( username.equals( user.getUsername() ) && this.isTokenExpired( token ) );
	}

}
