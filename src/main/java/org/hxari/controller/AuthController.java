package org.hxari.controller;

import org.hxari.component.AuthComponent;
import org.hxari.exception.AuthenticationException;
import org.hxari.exception.ServiceException;
import org.hxari.exception.UserException;
import org.hxari.exception.UserNotFoundException;
import org.hxari.model.User;
import org.hxari.request.UserRequest;
import org.hxari.response.BodyResponse;
import org.hxari.response.UserResponse;
import org.hxari.service.UserService;
import org.hxari.util.Security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping( path="/api/v1/auth" )
public class AuthController
{

	@Autowired
	private AuthComponent auth;

	@Autowired
	private UserService service;

	@RequestMapping( path="/logout", method=RequestMethod.POST )
	public ResponseEntity<BodyResponse<Void>> logout( HttpServletRequest request )
	{
		try
		{
			this.auth.filter( request );
			this.auth.unauthorizate( request );
			return( new ResponseEntity<>(
				new BodyResponse<>( "success", "ok", 
					HttpStatus.NO_CONTENT.value(),
					null
				),
				HttpStatus.NO_CONTENT
			));
		}
		catch( AuthenticationException e )
		{
			throw new ServiceException( "Service unavailable" );
		}
	}

	@RequestMapping( path="/signin", method=RequestMethod.POST )
	public ResponseEntity<BodyResponse<UserResponse<User>>> signin( HttpServletRequest request, HttpServletResponse response, @RequestBody UserRequest body ) throws Exception
	{
		this.auth.filter( request );
		User user = null;
		try
		{
			user = this.service.findByUname( body.username() );
		}
		catch( UserNotFoundException e )
		{
			user = this.service.findByEmail( body.username() );
		}
		if( Security.verify( user.getPassword(), body.password() ) )
		{
			this.auth.authenticate( user, response );
			return( new ResponseEntity<>(
				new BodyResponse<>( "success", "ok", HttpStatus.OK.value(), 
					new UserResponse<>(
						user
					)
				),
				HttpStatus.OK				
			));
		}
		throw new UserException( "Invalid user password" );
	}

	@RequestMapping( path="/signup", method=RequestMethod.POST )
	public ResponseEntity<BodyResponse<UserResponse<User>>> signup( HttpServletRequest request, HttpServletResponse response, @RequestBody UserRequest body )
	{
		this.auth.filter( request );
		try
		{
			User user = new User();
			user.setFullname( body.fullname() )
				.setUsername( body.username() )
				.setUsermail( body.usermail() )
				.setPassword( body.password() );
			this.service.save( user );
			this.auth.authenticate( user, response );
			return( new ResponseEntity<>(
				new BodyResponse<>( "success", "ok",
					HttpStatus.OK.value(),
					new UserResponse<>( user )
				),
				HttpStatus.OK
			));
		}
		catch( Throwable e )
		{
			throw new UserException( String.format( "%s: %s", e.getClass().getName(), e.getMessage() ) );
		}
	}

}
