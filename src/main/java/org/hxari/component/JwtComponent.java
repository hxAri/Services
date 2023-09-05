package org.hxari.component;

import java.security.Key;
import java.util.Date;

import org.hxari.service.UserDetailsServiceImplement.UserDetailsImplement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtComponent {

	private Logger logger = LoggerFactory.getLogger( JwtComponent.class );

	@Value( "${jwt.secret}" )
	private String jwtSecret;

	@Value( "${jwt.expirationMs}" )
	private int jwtExpirationMs;

	public String generateJwtToken( Authentication auth ) {
		UserDetailsImplement principal = ( UserDetailsImplement ) auth.getPrincipal();
		return( Jwts.builder()
			.setSubject( principal.getUsername() )
			.setIssuedAt( new Date() )
			.setExpiration( new Date( new Date().getTime() + this.jwtExpirationMs ) )
			.signWith( this.key(), SignatureAlgorithm.HS512 )
			.compact()
		);
	}

	private Key key() {
		return( Keys.hmacShaKeyFor( Decoders.BASE64.decode( this.jwtSecret ) ) );
	}

	public String getUserNameFromJwtToken( String token ) {
		return( Jwts.parserBuilder()
			.setSigningKey( this.key() )
			.build()
			.parseClaimsJws( token )
			.getBody()
			.getSubject()
		);
	}

	public boolean validateJwtToken( String token ) {
		try {
			Jwts.parserBuilder().setSigningKey( this.key() ).build().parse( token );
			return( true );
		}
		catch( MalformedJwtException e ) {
			logger.error( "Invalid JWT token: {}", e.getMessage() );
		}
		catch( ExpiredJwtException e ) {
			logger.error("JWT token is expired: {}", e.getMessage() );
		}
		catch( UnsupportedJwtException e ) {
			logger.error("JWT token is unsupported: {}", e.getMessage() );
		}
		catch( IllegalArgumentException e ) {
			logger.error( "JWT claims string is empty: {}", e.getMessage() );
		}
		return( false );
	}
}
