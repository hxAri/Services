package org.hxari.filter;

import java.io.IOException;

import org.hxari.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

	@Autowired
	private JwtService jwtService;

	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal( HttpServletRequest request, HttpServletResponse response, FilterChain filterChain ) throws ServletException, IOException {
		SecurityContext context = SecurityContextHolder.getContext();
		String content = request.getHeader( HttpHeaders.AUTHORIZATION );
		String username = null;
		String token = null;
		if( content != null ) {
			if( content.startsWith( "Bearer" ) ) {
				token = content.substring( 7 );
				try {
					username = this.jwtService.extractUsername( token );
				}
				catch( IllegalArgumentException e ) {
					System.out.println( "\033[1;31mIllegalArgumentException\033[1;38;5;214m: \033[1;37mUnable to get JWT Token\033[0m\n" );
				}
				catch( ExpiredJwtException e ) {
					System.out.println( "\033[1;31mExpiredJwtException\033[1;38;5;214m: \033[1;37mJWT Token has expired\033[0m\n" );
				}
				if( username != null ) {
					if( context.getAuthentication() == null ) {
						UserDetails user = this.userDetailsService.loadUserByUsername( username );
						if( jwtService.validateToken( token, user ) ) {
							System.out.println( "\033[1;32mValid\033[1;38;5;214m: \033[1;38;5;111m" + user.getAuthorities() + "\033[0m\n" );
							UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken( user, null, user.getAuthorities() );
							authenticationToken.setDetails( new WebAuthenticationDetailsSource().buildDetails( request ) );
							context.setAuthentication( authenticationToken );
							response.setHeader( HttpHeaders.AUTHORIZATION, String.format( "Bearer %s", token ) );
						}
						else {
							System.out.println( "\033[1;31mInvalid\033[1;38;5;214m: \033[1;38;5;111m" + user.getAuthorities() + "\033[0m\n" );
						}
					}
				}
			}
		}
		filterChain.doFilter( request, response );
	}
}
