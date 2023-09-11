package org.hxari.api.v1;

import java.util.Set;

import org.hxari.exception.AuthenticationException;
import org.hxari.exception.ClientException;
import org.hxari.model.RoleModel;
import org.hxari.model.UserModel;
import org.hxari.model.UserModel.Role;
import org.hxari.payload.request.SignInRequest;
import org.hxari.payload.request.UserInfoRequest;
import org.hxari.payload.response.AuthInfoResponse;
import org.hxari.payload.response.AuthResponse;
import org.hxari.payload.response.BodyResponse;
import org.hxari.repository.UserRepository;
import org.hxari.service.JwtService;
import org.hxari.service.UserDetailsServiceImplement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping( path="/api/v1/auth" )
public class AuthAPI {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private UserDetailsServiceImplement userDetailsService;

	@Autowired
	private UserRepository userRepository;

	private ResponseEntity<BodyResponse<AuthResponse<UserDetails/**, String*/>>> builder( HttpServletResponse response, String username, String password ) throws AuthenticationException {
		Authentication authentication = this.authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(
				username, password
			)
		);
		if( authentication.isAuthenticated() ) {
			UserDetails user = this.userDetailsService.loadUserByUsername( username );
			String token = this.jwtService.generateToken( username );
			response.setHeader( HttpHeaders.AUTHORIZATION, String.format( "Bearer %s", token ) );
			return( new ResponseEntity<>(
				new BodyResponse<>( "success", "ok", 200,
					new AuthResponse<>( user )
				), 
				HttpStatus.OK
			));
		}
		throw new AuthenticationException( "Failed to authenticate user" );
	}
	
	@Operation(
		method="GET",
		summary="API for check current authenticated role",
		description="API for check current authenticated role"
	)
	@RequestMapping( method=RequestMethod.GET )
	public ResponseEntity<BodyResponse<AuthInfoResponse>> index( HttpServletRequest request ) {
		SecurityContext context = SecurityContextHolder.getContext();
		Authentication authentication = context.getAuthentication();
		String bearer = request.getHeader( HttpHeaders.AUTHORIZATION );
		String username = null;
		String authority = authentication.getAuthorities().toArray()[0].toString();
		boolean authenticated = authentication.isAuthenticated();
		if( bearer != null ) {
			username = this.jwtService.extractUsername( bearer );
		}
		return( new ResponseEntity<>( 
			new BodyResponse<>( "success", "ok", 200,
				new AuthInfoResponse(
					authenticated,
					authority,
					username
				)
			), 
			HttpStatus.OK
		));
	}

	@Operation(
		method="POST",
		summary="SignIn for authenticate request",
		description="SignIn for authenticate request based on role",
		parameters={}
	)
	@RequestMapping( path="/signin", method=RequestMethod.POST )
	public ResponseEntity<BodyResponse<AuthResponse<UserDetails/**, String*/>>> signin( @RequestBody( required=true ) SignInRequest body, HttpServletResponse response ) throws AuthenticationException {
		return( this.builder( response,
			body.username(), 
			body.password()
		));
	}

	@Operation(
		method="POST",
		summary="Register new user",
		description="Register new role user",
		parameters={}
	)
	@RequestMapping( path="/signup", method=RequestMethod.POST )
	public ResponseEntity<BodyResponse<AuthResponse<UserDetails/**, String*/>>> signup( @RequestBody( required=true ) UserInfoRequest body, HttpServletResponse response ) throws AuthenticationException {
		if( this.userRepository.existsByUsermail( body.usermail() ) )
			throw new ClientException( "Email Address is already use" );
		if( this.userRepository.existsByUsername( body.username() ) )
			throw new ClientException( "Username is already use" );
		this.userDetailsService.save( new UserModel( body, Set.of( new RoleModel( Role.USER ) ) ) );
		return( this.builder( response,
			body.username(), 
			body.password()
		));
	}
}
