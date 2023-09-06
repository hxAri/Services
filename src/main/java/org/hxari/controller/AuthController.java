package org.hxari.controller;

import java.util.HashSet;
import java.util.Set;

import org.hxari.component.JwtComponent;
import org.hxari.exception.ClientException;
import org.hxari.model.RoleModel;
import org.hxari.model.UserModel;
import org.hxari.model.RoleModel.Role;
import org.hxari.payload.request.SignInRequest;
import org.hxari.payload.request.SignUpRequest;
import org.hxari.payload.response.BodyResponse;
import org.hxari.payload.response.JwtResponse;
import org.hxari.payload.response.UserResponse;
import org.hxari.repository.RoleRepository;
import org.hxari.repository.UserRepository;
import org.hxari.service.UserDetailsServiceImplement.UserDetailsImplement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@CrossOrigin( maxAge=3600, origins="*" )
@RestController
@RequestMapping( path="/api/v1/auth" )
public class AuthController {

	@Autowired
	private AuthenticationManager authManager;
	
	@Autowired
	private JwtComponent jwtComponent;

	@Autowired
	private PasswordEncoder paswEncoder;

	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private UserRepository userRepository;

	@RequestMapping( path="/signin", method=RequestMethod.POST )
	public ResponseEntity<?> signin( @Valid @RequestBody SignInRequest body ) {
		Authentication auth = this.authManager.authenticate(
			new UsernamePasswordAuthenticationToken( 
				body.username(), 
				body.password() 
			)
		);
		SecurityContextHolder.getContext().setAuthentication( auth );
    	String jwt = this.jwtComponent.generateJwtToken( auth );
		UserDetailsImplement user = ( UserDetailsImplement ) auth.getPrincipal();    
		return( new ResponseEntity<>(
			new BodyResponse<>( "success", "ok",
				HttpStatus.OK.value(),
				new JwtResponse(
					jwt,
					user
				)
			),
			HttpStatus.OK
		));
	}

	@RequestMapping( path="/signup", method=RequestMethod.POST )
	public ResponseEntity<?> signup( @Valid @RequestBody SignUpRequest body ) {
		if( this.userRepository.existsByUsermail( body.usermail() ) ) 
			throw new ClientException( "Email Address is already use" );
		if( this.userRepository.existsByUsername( body.username() ) ) 
			throw new ClientException( "Username is already taked" );
		if( body.role().isEmpty() )
			throw new ClientException( "User role required" );
		Set<RoleModel> roles = new HashSet<>();
		switch( body.role().toLowerCase() ) {
			case "admin":
				throw new ClientException( "Can resgister as Admin" );
			case "moder":
				throw new ClientException( "Only Admin can set user as Moder" );
			case "user":
				roles.add( this.roleRepository.findByRole( Role.MODER ).get() );
				break;
			default:
				throw new ClientException( "Invalid user role" );
		}
		UserModel user = new UserModel(
			body.fullname(),
			body.usermail(),
			body.username(),
			this.paswEncoder.encode( 
				body.password() 
			),
			roles
		);
		this.userRepository.save( user );
		return( new ResponseEntity<>(
			new BodyResponse<>( "user registered", "ok",
				HttpStatus.OK.value(),
				new UserResponse<>(
					user
				)
			),
			HttpStatus.OK
		));
	}

}
