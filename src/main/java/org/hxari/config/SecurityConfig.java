package org.hxari.config;

import org.hxari.component.AccessDeniedHandlerComponent;
import org.hxari.component.AuthEntryPointComponent;
import org.hxari.filter.JwtAuthFilter;
import org.hxari.service.UserDetailsServiceImplement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
// import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
// import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

	@Autowired
	private AccessDeniedHandlerComponent accessDeniedHandler;

	@Autowired
  	private AuthEntryPointComponent authenticationEntryPoint;
	
	@Autowired
	private JwtAuthFilter jwtAuthFilter;

	@Bean
	public AuthenticationManager authenticationManager( AuthenticationConfiguration ac ) throws Exception {
		return( ac.getAuthenticationManager() );
	}

	@Bean AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService( this.userDetailsService() );
		authenticationProvider.setPasswordEncoder( this.passwordEncoder() );
		return( authenticationProvider );
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return( new BCryptPasswordEncoder() );
	}

	// @Bean
	// public RoleHierarchy roleHierarchy() {
	// 	RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
	// 	String hierarchy = "ROLE_ADMIN > ROLE_MODER > ROLE_MODER > ROLE_USER";
	// 	roleHierarchy.setHierarchy(hierarchy);
	// 	return roleHierarchy;
	// }

	@Bean
	public SecurityFilterChain securityFilterChain( HttpSecurity http ) throws Exception {
		return( http
			.csrf( csrf -> csrf.disable() )
			.authorizeHttpRequests( auth -> auth
				.requestMatchers( "/api/v1/auth" ).permitAll()
				.requestMatchers( "/api/v1/auth/signin" ).permitAll()
				.requestMatchers( "/api/v1/auth/signup" ).permitAll()
				.requestMatchers( "/docs" ).permitAll()
				.requestMatchers( "/docs/swagger-ui/**" ).permitAll()
				.requestMatchers( "/swagger-resources/*" ).permitAll()
				.requestMatchers( "/swagger-ui/**" ).permitAll()
				.requestMatchers( "/v3/api-docs/**" ).permitAll()
			)
			.authorizeHttpRequests( auth -> auth
				.requestMatchers( "/api/v1/task/**" ).permitAll()
				.requestMatchers( "/api/v1/user/**" ).permitAll() //.hasRole( "USER" )
				.requestMatchers( "/api/v1/moder/**" ).permitAll() //.hasRole( "MODER" )
				.requestMatchers( "/api/v1/admin/**" ).permitAll() //.hasRole( "ADMIN" )
				.anyRequest()
				.authenticated()
			)
			.exceptionHandling( exception -> exception
				.accessDeniedHandler( this.accessDeniedHandler )
				.authenticationEntryPoint( this.authenticationEntryPoint )
			)
			.authenticationProvider( this.authenticationProvider() )
			.addFilterBefore( jwtAuthFilter, UsernamePasswordAuthenticationFilter.class )
			.sessionManagement( session -> session.sessionCreationPolicy( SessionCreationPolicy.STATELESS ) )
			.build()
		);
	}

	@Bean
	public UserDetailsService userDetailsService() {
		return( new UserDetailsServiceImplement() );
	}

	// @Bean
	// public DefaultWebSecurityExpressionHandler webSecurityExpressionHandler() {
	// 	DefaultWebSecurityExpressionHandler expressionHandler = new DefaultWebSecurityExpressionHandler();
	// 	expressionHandler.setRoleHierarchy( this.roleHierarchy() );
	// 	return( expressionHandler );
	// }

}
