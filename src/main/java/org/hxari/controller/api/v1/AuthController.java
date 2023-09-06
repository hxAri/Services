package org.hxari.controller.api.v1;

import javax.naming.AuthenticationException;

import org.hxari.model.UserModel;
import org.hxari.model.UserModel.Role;
import org.hxari.payload.request.SignInRequest;
import org.hxari.payload.request.SignUpRequest;
import org.hxari.payload.response.BodyResponse;
import org.hxari.service.JwtService;
import org.hxari.service.UserDetailsServiceImplement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

	@Value( "${jwt.secret}" )
	protected String SECRET;

	@Autowired
	private UserDetailsServiceImplement userDetailsService;
	
	@RequestMapping
	public ResponseEntity<?> index() {
		System.out.print( "\033[1;37m: \033[1;32m" );
		System.out.print( this.SECRET );
		System.out.println( "\033[0m" );
		return( new ResponseEntity<>( 
			new BodyResponse<>( "success", "ok", 200, null ), 
			HttpStatus.OK
		));
	}

	@RequestMapping( path="/signin", method=RequestMethod.POST )
	public ResponseEntity<?> signin( @Valid @RequestBody SignInRequest body ) {
		Authentication authentication = this.authenticationManager.authenticate( 
			new UsernamePasswordAuthenticationToken( 
				body.username(), 
				body.password() 
			) 
		);
		if( authentication.isAuthenticated() ) {
			return( new ResponseEntity<>(
				new BodyResponse<>( "success", "ok", 200,
					jwtService.generateToken( body.username() )
				), 
				HttpStatus.OK
			));
		}
        throw new UsernameNotFoundException( "invalid user request" );
	}

	@RequestMapping( path="/signup", method=RequestMethod.POST )
	public ResponseEntity<?> signup( @Valid @RequestBody SignUpRequest body ) throws AuthenticationException {
		UserModel user = new UserModel( body );
		user.setRole( Role.ADMIN );
		this.userDetailsService.save( user );
		Authentication authentication = this.authenticationManager.authenticate( 
			new UsernamePasswordAuthenticationToken( 
				body.username(), 
				body.password() 
			) 
		);
		if( authentication.isAuthenticated() ) {
			return( new ResponseEntity<>(
				new BodyResponse<>( "success", "ok", 200,
					jwtService.generateToken( user.getUsername() )
				), 
				HttpStatus.OK
			));
		}
		throw new AuthenticationException( "Failed to authenticate user" );
	}
}
