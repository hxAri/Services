package org.hxari.controller;

import org.hxari.component.AuthComponent;
import org.hxari.model.User;
import org.hxari.request.UserRequest;
import org.hxari.response.BodyResponse;
import org.hxari.response.UserResponse;
import org.hxari.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping( "/api/v1/user" )
class UserController
{

	@Autowired
	private AuthComponent auth;
	
	@Autowired
	private UserService service;

	@RequestMapping( path="/{id}", method=RequestMethod.DELETE )
	public ResponseEntity<BodyResponse<Void>> delete( HttpServletRequest request, HttpServletResponse response, @CookieValue( name="csrftoken", defaultValue="" ) String csrftoken, @PathVariable( name="id" ) Long id )
	{
		this.auth.filter( request );
		this.auth.validate( csrftoken, id );
		this.service.delete( id );
		return( new ResponseEntity<>(
			new BodyResponse<>( "deleted", "ok", 
				HttpStatus.NO_CONTENT.value(), 
				null
			),
			HttpStatus.NO_CONTENT
		));
	}

	@RequestMapping( path="/{id}", method=RequestMethod.GET )
	public ResponseEntity<BodyResponse<UserResponse<User>>> display( HttpServletRequest request, HttpServletResponse response, @CookieValue( name="csrftoken", defaultValue="" ) String csrftoken, @PathVariable( name="id" ) Long id )
	{
		this.auth.filter( request );
		this.auth.validate( csrftoken, id );
		this.auth.authenticate( request, response );
		return( new ResponseEntity<>(
			new BodyResponse<>(
				"success", 
				"ok", 
				HttpStatus.OK.value(), 
				new UserResponse<>(
					this.service.find( id )
				)
			), 
			HttpStatus.OK
		));
	}

	@RequestMapping( path="/{id}", method=RequestMethod.PUT )
	public ResponseEntity<BodyResponse<UserResponse<User>>> update( HttpServletRequest request, HttpServletResponse response, @CookieValue( name="csrftoken", defaultValue="" ) String csrftoken, @PathVariable( name="id" ) Long id, @RequestBody UserRequest body ) throws Throwable
	{
		this.auth.filter( request );
		this.auth.validate( csrftoken, id );
		this.auth.authenticate( request, response );
		User user = this.service.find( id );
		user.setFullname( body.fullname() != null ? body.fullname() : user.getFullname() );
		user.setUsermail( body.usermail() != null ? body.usermail() : user.getUsermail() );
		user.setUsername( body.username() != null ? body.username() : user.getUsername() );
		user.setPassword( body.password() != null ? body.password() : user.getPassword() );
		this.service.save( user );
		return( new ResponseEntity<>(
			new BodyResponse<>(
				"success", 
				"ok", 
				HttpStatus.OK.value(), 
				new UserResponse<>(
					user
				)
			), 
			HttpStatus.OK
		));
	}

}
