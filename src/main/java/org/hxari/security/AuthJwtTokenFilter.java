package org.hxari.security;

import java.io.IOException;

import org.hxari.component.JwtComponent;
import org.hxari.service.UserDetailsServiceImplement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AuthJwtTokenFilter extends OncePerRequestFilter {

	private Logger logger = LoggerFactory.getLogger( AuthJwtTokenFilter.class );

	@Autowired
	private JwtComponent jwtc;

	@Autowired
	private UserDetailsServiceImplement udsi;

	@Override
	protected void doFilterInternal( HttpServletRequest request, HttpServletResponse response, FilterChain filterChain ) throws ServletException, IOException {
		try {
			String jwt = this.parseJwt( request );
			if( jwt != null && this.jwtc.validateJwtToken( jwt ) ) {
				String username = this.jwtc.getUserNameFromJwtToken( jwt );
				UserDetails user = this.udsi.loadUserByUsername( username );
				UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken( user, user.getAuthorities() );
				auth.setDetails(
					new WebAuthenticationDetailsSource()
						.buildDetails( request )
				);
				SecurityContextHolder
					.getContext()
					.setAuthentication( auth );
			}
		}
		catch( Exception e ) {
			this.logger.error( "Error, failed to set authentication: {}", e );
		}
		filterChain.doFilter( request, response );
	}

	private String parseJwt( HttpServletRequest request ) {
		String headerAuthorization = request.getHeader( HttpHeaders.AUTHORIZATION );
		if( headerAuthorization != null && StringUtils.hasText( headerAuthorization ) ) {
			return( headerAuthorization.substring( 7 ) );
		}
		return( null );
	}
}
