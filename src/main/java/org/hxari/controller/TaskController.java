package org.hxari.controller;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.NoSuchElementException;

import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.hxari.component.AuthComponent;
import org.hxari.component.PageComponent;
import org.hxari.exception.ClientException;
import org.hxari.exception.TaskNotFoundException;
import org.hxari.model.Page;
import org.hxari.model.Task;
import org.hxari.model.User;
import org.hxari.repository.TaskRepository;
import org.hxari.request.TaskRequest;
import org.hxari.response.BodyResponse;
import org.hxari.response.TaskResponse;
import org.hxari.response.TasksResponse;
import org.hxari.service.UserService;
import org.hxari.util.ByteParser;
import org.hxari.util.DateParser;
import org.hxari.util.Hex;
import org.hxari.util.Security;
import org.hxari.util.Security.Message;
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
@RequestMapping( "/api/v1/user/{uid}/tasks" )
class TaskController
{

	@Autowired
	private AuthComponent auth;

	@Autowired
	private PageComponent page;

	@Autowired
	private TaskRepository task;

	@Autowired
	private UserService user;

	@RequestMapping( method=RequestMethod.POST )
	public ResponseEntity<BodyResponse<TasksResponse<List<Task>>>> tasks( 
		HttpServletRequest request, 
		HttpServletResponse response, 
		@CookieValue( name="csrftoken", defaultValue="" ) String csrftoken, 
		@PathVariable( name="uid" ) Long uid, 
		@RequestBody( required=false ) TaskRequest body
	) throws Exception
	{
		this.auth.filter( request );
		this.auth.validate( csrftoken, uid );
		this.auth.authenticate( request, response );
		List<Task> tasks = null;
		Timestamp timestamp = null;
		Page page = null;
		int offset = 4;
		User owner = this.user.find( uid );

		// Check if next page is available.
		if( body.next() != null )
		{
			page = this.page.find( body.next() );
			String[] parts = page.getPadding().split( "/" );
			Long expires = page.getExpires().getTime() / 1000;
			Long current = Timestamp.valueOf( LocalDateTime.now() ).getTime() / 1000;

			// Check if page is expired.
			if( current >= expires ) throw new ClientException( "Page next request is expired" );
			
			// Check if splited padding is valid.
			if( parts.length == 4 )
			{
				switch( parts[1] )
				{
					case "fAllByOwnerWP":
						uid = Long.parseLong( parts[2] );
						break;
					case "fAllByOwnerAndCreatedWP":
						body = body.withCreated( parts[2] );
						break;
					case "fAllByOwnerAndUpdatedWP":
						body = body.withUpdated( parts[2] );
						break;
					default:
						System.out.print( "Next: " );
						System.out.print( "\033[1;32m" );
						System.out.println( page.getPadding() );
						System.out.print( "\033[0m" );
						throw new ClientException( "Invalid next request action" );
				}
				offset = Integer.parseInt( parts[3] );
			}
			else {
				System.out.println( page.getPadding() );
				throw new ClientException( "Invalid next request" );
			}
		}

		// If filter by creation time is available.
		if( body.created() != null )
		{
			timestamp = Timestamp.valueOf( DateParser.localDateTime( body.created() ) );
			tasks = this.task.findAllByOwnerAndCreatedOnWithPaddingLimit( owner.getId(), timestamp, 4, offset ).get();
			page = this.pageBuilder( page, "Task", "fAllByOwnerAndCreatedWP", timestamp.toLocalDateTime().toString(), offset +4 );
		}
		else {

			// If filter by update time is available.
			if( body.updated() != null )
			{
				timestamp = Timestamp.valueOf( DateParser.localDateTime( body.updated() ) );
				tasks = this.task.findAllByOwnerAndUpdatedOnWithPaddingLimit( owner.getId(), timestamp, 4, offset ).get();
				page = this.pageBuilder( page, "Task", "fAllByOwnerAndUpdatedWP", timestamp.toLocalDateTime().toString(), offset +4 );
			}
			else {
				tasks = this.task.findAllByOwnerWithPaddingLimit( owner.getId(), 4, offset ).get();
				page = this.pageBuilder( page, "Task", "fAllByOwnerWP", owner.getId(), offset +4 );
			}
		}

		// Check if next page is available.
		if( tasks.size() >= 1 )
		{
			this.page.save( page );
		}
		else {
			if( page.getId() != null )
			{
				this.page.delete( page );
			}
			page.setPadding( null );
			page.setToken( null );
		}
		return( new ResponseEntity<>(
			new BodyResponse<>( "success", "ok",
				HttpStatus.OK.value(),
				new TasksResponse<>(
					page.getToken(),
					tasks.size(),
					tasks
				)
			),
			HttpStatus.OK
		));
	}

	@RequestMapping( path="/create", method=RequestMethod.POST )
	public ResponseEntity<BodyResponse<TaskResponse<Task>>> create( 
		HttpServletRequest request, 
		HttpServletResponse response, 
		@CookieValue( name="csrftoken", defaultValue="" ) String csrftoken, 
		@PathVariable( name="uid" ) Long uid, 
		@RequestBody Task body 
	)
	{
		Task task = null;
		this.auth.filter( request );
		this.auth.validate( csrftoken, uid );
		this.auth.authenticate( request, response );
		this.task.save(
			task = new Task(
				body.getTitle(),
				this.user.find( uid )
			)
		);
		return( new ResponseEntity<>(
			new BodyResponse<>( "success", "ok", 
				HttpStatus.OK.value(), 
				new TaskResponse<>(
					task
				)
			),
			HttpStatus.OK
		));
	}

	@RequestMapping( path="/{id}", method=RequestMethod.DELETE )
	public ResponseEntity<BodyResponse<Void>> delete( 
		HttpServletRequest request, 
		HttpServletResponse response, 
		@CookieValue( name="csrftoken", defaultValue="" ) String csrftoken, 
		@PathVariable( name="uid" ) Long uid, 
		@PathVariable( name="id" ) Long id 
	)
	{
		this.auth.filter( request );
		this.auth.validate( csrftoken, uid );
		this.auth.authenticate( request, response );
		try
		{
			this.task.deleteById( id );
			return( new ResponseEntity<>(
				new BodyResponse<>( "success", "ok", 
					HttpStatus.NO_CONTENT.value(), 
					null
				),
				HttpStatus.NO_CONTENT
			));
		}
		catch( NoSuchElementException e )
		{
			throw new TaskNotFoundException( "Task not found" );
		}
	}

	@RequestMapping( path="/{id}", method=RequestMethod.GET )
	public ResponseEntity<BodyResponse<TaskResponse<Task>>> single( 
		HttpServletRequest request, 
		HttpServletResponse response, 
		@CookieValue( name="csrftoken", defaultValue="" ) String csrftoken, 
		@PathVariable( name="uid" ) Long uid, 
		@PathVariable( name="id" ) Long id 
	)
	{
		this.auth.filter( request );
		this.auth.validate( csrftoken, uid );
		this.auth.authenticate( request, response );
		try
		{
			return( new ResponseEntity<>(
				new BodyResponse<>( "success", "ok", 
					HttpStatus.OK.value(), 
					new TaskResponse<>(
						this.task.findById( uid ).get()
					)
				),
				HttpStatus.OK
			));
		}
		catch( NoSuchElementException e )
		{
			throw new TaskNotFoundException( "Task not found" );
		}
	}

	@RequestMapping( path="/{id}", method=RequestMethod.PUT )
	public ResponseEntity<BodyResponse<TaskResponse<Task>>> update( 
		HttpServletRequest request, 
		HttpServletResponse response, 
		@CookieValue( name="csrftoken", defaultValue="" ) String csrftoken, 
		@PathVariable( name="uid" ) Long uid, 
		@PathVariable( name="id" ) Long id, 
		@RequestBody Task body 
	)
	{
		this.auth.filter( request );
		this.auth.validate( csrftoken, uid );
		this.auth.authenticate( request, response );
		try
		{
			Task task = this.task.findById( id ).get();
			this.task.save(
				task.setTitle( 
					body.getTitle() != null ? 
						body.getTitle() : 
						task.getTitle()
				)
			);
			return( new ResponseEntity<>(
				new BodyResponse<>( "success", "ok", 
					HttpStatus.OK.value(), 
					new TaskResponse<>(
						task
					)
				),
				HttpStatus.OK
			));
		}
		catch( NoSuchElementException e )
		{
			throw new TaskNotFoundException( "Task not found" );
		}
	}

	/*
	 * Overloading method of method pageBuilder.
	 * 
	 * @access Private
	 * 
	 * @params Page page
	 * @params String model
	 * @params String action
	 * @params Long value
	 * @params int offset
	 * 
	 * @return Page
	 */
	private Page pageBuilder( Page page, String model, String action, Long value, int offset ) throws Exception
	{
		return( this.pageBuilder( page, model, action, value + "", offset ) );
	}

	/*
	 * Page model builder.
	 * 
	 * @access Private
	 * 
	 * @params Page page
	 * @params String model
	 * @params String action
	 * @params String value
	 * @params Long length
	 * @params int offset
	 * 
	 * @return Page
	 */
	private Page pageBuilder( Page page, String model, String action, String value, int offset ) throws Exception
	{
		LocalDateTime expires = DateParser.localDateTime( "+6 minutes" );
		String padding = model + "/";
		padding += action + "/";
		padding += value + "/";
		padding += offset;

		// Normalize page.
		page = page != null ? page : new Page();

		// Generate new random password and salt.
		byte[] pasw = Security.salt( 64 );
		byte[] salt = Security.salt( 128 );
		
		// Create new Secret Key.
		SecretKeySpec ksec = Message.ksec( ByteParser.bytesToChars( pasw ), salt );
		IvParameterSpec ivp = Message.ivp();

		// Extract iv and secret.
		String ivps = Base64.getEncoder().encodeToString( ivp.getIV() );
		String token = Hex.encode( Security.salt( 64 ) );
		String secret = String.format( "%s:%s", Hex.encode( pasw ), Base64.getEncoder().encodeToString( salt ) );
		
		// Encrypting padding.
		padding = Message.encrypt( "AES/CBC/PKCS5Padding", padding, ksec, ivp );

		// Update page infor.
		page.setIvp( ivps )
			.setToken( token )
			.setSecret( secret )
			.setExpires( expires )
			.setPadding( padding );
		
		return( page );
	}

}
