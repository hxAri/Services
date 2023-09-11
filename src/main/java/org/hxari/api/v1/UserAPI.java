package org.hxari.api.v1;

import org.hxari.model.UserModel;
import org.hxari.payload.request.UserInfoRequest;
import org.hxari.payload.response.BodyResponse;
import org.hxari.payload.response.UserResponse;
import org.hxari.service.JwtService;
import org.hxari.service.UserDetailsServiceImplement;
import org.hxari.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping( path="/api/v1/user" )
public class UserAPI {

	@Autowired
	private JwtService jwtService;

	@Autowired
	private UserDetailsServiceImplement userDetailsService;

	@Autowired
	private UserService userService;
	
	@Operation( 
		method="GET",
		summary="API for get user info",
		description="API for get authenticated user info"
	)
	@RequestMapping( method=RequestMethod.GET )
	@PreAuthorize( value="hasAuthority('ROLE_USER')" )
	public ResponseEntity<BodyResponse<UserResponse<UserModel>>> index( HttpServletRequest request ) {
		UserModel user = this.jwtService.getUserModel( request.getHeader( HttpHeaders.AUTHORIZATION ) );
		return( new ResponseEntity<>( 
			new BodyResponse<>( "success", "ok", 200, 
				new UserResponse<>( user )
			),
			HttpStatus.OK
		));
	}

	@Operation( 
		method="PUT",
		summary="API for update user info",
		description="API for update authenticated user info",
		parameters={}
	)
	@RequestMapping( method=RequestMethod.PUT )
	@PreAuthorize( value="hasAuthority('ROLE_USER')" )
	public ResponseEntity<BodyResponse<UserResponse<UserModel>>> update( HttpServletRequest request, @RequestBody( required=true ) UserInfoRequest body ) {
		UserModel user = this.jwtService.getUserModel( request.getHeader( HttpHeaders.AUTHORIZATION ) );
		this.userDetailsService.save( user, body );
		return( new ResponseEntity<>( 
			new BodyResponse<>( "updated", "ok", 200, 
				new UserResponse<>(
					this.userService.findByUsername( user.getUsername() )
				)
			),
			HttpStatus.OK
		));
	}

	@Operation(
		method="DELETE",
		summary="API for delete user by id",
		description="API for detele user by id, this just for admin authority only",
		parameters={}
	)
	@RequestMapping( path="/{id}", method=RequestMethod.DELETE )
	@PreAuthorize( value="hasAuthority('ROLE_ADMIN')" )
	public ResponseEntity<BodyResponse<Void>> delete( @PathVariable Long id ) {
		this.userService.delete( id );
		return( new ResponseEntity<>(
			new BodyResponse<>( "deleted", "ok", HttpStatus.NO_CONTENT.value(), null ),
			HttpStatus.NO_CONTENT
		));
	}
}
