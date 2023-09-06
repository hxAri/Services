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
		if( content != null && content.startsWith( "Bearer" ) ) {
			token = content.substring( 7 );
			System.out.print( "\033[1;37m: \033[1;32m" );
			System.out.print( content );
			System.out.println( "\033[0m" );
			username = this.jwtService.extractUsername( token );
			if( username != null && context.getAuthentication() == null ) {
				UserDetails user = this.userDetailsService.loadUserByUsername( username );
				System.out.print( "\033[1;37m: \033[1;32m" );
				System.out.println( "\033[0m" );
				if( this.jwtService.validateToken( token, user ) ) {
					UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken( user, null, user.getAuthorities() );
					authenticationToken.setDetails( new WebAuthenticationDetailsSource().buildDetails( request ) );
					context.setAuthentication( authenticationToken );
				}
			}
		}
		filterChain.doFilter( request, response );
	}

}
