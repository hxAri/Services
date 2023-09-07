package org.hxari.controller.api.v1;

import java.util.Set;

import org.hxari.exception.AuthenticationException;
import org.hxari.exception.ClientException;
import org.hxari.model.RoleModel;
import org.hxari.model.UserModel;
import org.hxari.model.UserModel.Role;
import org.hxari.payload.request.SignInRequest;
import org.hxari.payload.request.SignUpRequest;
import org.hxari.payload.response.AuthResponse;
import org.hxari.payload.response.BodyResponse;
import org.hxari.repository.UserRepository;
import org.hxari.service.JwtService;
import org.hxari.service.UserDetailsServiceImplement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping( path="/api/v1/auth" )
public class AuthController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private UserDetailsServiceImplement userDetailsService;

	@Autowired
	private UserRepository userRepository;

	private ResponseEntity<?> builder( String username, String password ) throws AuthenticationException {
		Authentication authentication = this.authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(
				username, password
			)
		);
		if( authentication.isAuthenticated() ) {
			UserDetails user = this.userDetailsService.loadUserByUsername( username );
			return( new ResponseEntity<>(
				new BodyResponse<>( "success", "ok", 200,
					new AuthResponse<>(
						user, this.jwtService.generateToken( username )
					)
				), 
				HttpStatus.OK
			));
		}
		throw new AuthenticationException( "Failed to authenticate user" );
	}
	
	@RequestMapping( method=RequestMethod.GET )
	public ResponseEntity<?> index() {
		return( new ResponseEntity<>( 
			new BodyResponse<>( "success", "ok", 200, null ), 
			HttpStatus.OK
		));
	}

	@RequestMapping( path="/signin", method=RequestMethod.POST )
	public ResponseEntity<?> signin( @Valid @RequestBody SignInRequest body ) throws AuthenticationException {
		return( this.builder(
			body.username(), 
			body.password()
		));
	}

	@RequestMapping( path="/signup", method=RequestMethod.POST )
	public ResponseEntity<?> signup( @Valid @RequestBody SignUpRequest body ) throws AuthenticationException {
		if( this.userRepository.existsByUsermail( body.usermail() ) )
			throw new ClientException( "Email Address is already use" );
		if( this.userRepository.existsByUsername( body.username() ) )
			throw new ClientException( "Username is already use" );
		this.userDetailsService.save( new UserModel( body, Set.of( new RoleModel( Role.USER ) ) ) );
		return( this.builder(
			body.username(), 
			body.password()
		));
	}
}
