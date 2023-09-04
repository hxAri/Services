package org.hxari.config;

import org.hxari.component.AuthEntryPointJwtComponent;
import org.hxari.security.AuthJwtTokenFilter;
import org.hxari.service.UserDetailsServiceImplement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
// @EnableWebSecurity
public class SecurityConfig { // extends WebSecurityConfiguration {

	@Autowired
	private UserDetailsServiceImplement userDetails;

	@Autowired
  	private AuthEntryPointJwtComponent unAuthorizedHandler;

	@Bean
	public AuthJwtTokenFilter authJwtTokenFilter() {
		return( new AuthJwtTokenFilter() );
	}

	@Bean
	public AuthenticationManager authenticationManager( AuthenticationConfiguration ac ) throws Exception {
		return( ac.getAuthenticationManager() );
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider dap = new DaoAuthenticationProvider();
		dap.setPasswordEncoder( this.passwordEncoder() );
		dap.setUserDetailsService( this.userDetails );
		return( dap );
	}

	// @Override
	// public void configure( AuthenticationManagerBuilder authenticationManagerBuilder ) throws Exception {}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return( new BCryptPasswordEncoder() );
	}

	@Bean
	public SecurityFilterChain securityFilterChain( HttpSecurity http ) throws Exception {
		http.csrf( csrf -> csrf.disable() );
		http.exceptionHandling( exception -> exception.authenticationEntryPoint( this.unAuthorizedHandler ) );
		http.sessionManagement( session -> session.sessionCreationPolicy( SessionCreationPolicy.STATELESS ) );
		http.authorizeHttpRequests( auth -> 
			auth.requestMatchers( "/api/auth/**", "" ).permitAll()
				.requestMatchers( "/api/test/**", "" ).permitAll()
				.anyRequest()
				.authenticated()
		);
		http.authenticationProvider( this.authenticationProvider() );
		http.addFilterBefore( this.authJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class );
		return( http.build() );
	}

}
