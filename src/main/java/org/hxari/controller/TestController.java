package org.hxari.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping( "/api/v1/test" )
public class TestController
{

	// @Autowired
	// private PageComponent page;

	// @Autowired
	// private TaskRepository task;

	// @Autowired
	// private UserService user;

	// @RequestMapping( path="/user/{uid}/tasks", method=RequestMethod.POST )
	// public ResponseEntity<BodyResponse<TasksResponse<List<Task>>>> tasks( @PathVariable Long uid, @RequestBody( required=false ) TaskRequest body ) throws Exception
	// {
	// 	List<Task> tasks = null;
	// 	Timestamp timestamp = null;
	// 	Page page = null;
	// 	int offset = 10;
	// 	User owner = this.user.find( uid );

	// 	// Check if next page is available.
	// 	if( body.next() != null )
	// 	{
	// 		page = this.page.find( body.next() );
	// 		String[] parts = page.getPadding().split( "/" );
	// 		Long expires = page.getExpires().getTime() / 1000;
	// 		Long current = Timestamp.valueOf( LocalDateTime.now() ).getTime() / 1000;

	// 		// Check if page is expired.
	// 		if( current >= expires ) throw new ClientException( "Page next request is expired" );
			
	// 		// Check if splited padding is valid.
	// 		if( parts.length == 4 )
	// 		{
	// 			switch( parts[1] )
	// 			{
	// 				case "fAllByOwnerWP":
	// 					uid = Long.parseLong( parts[2] );
	// 					break;
	// 				case "fAllByOwnerAndCreatedWP":
	// 					body = body.withCreated( parts[2] );
	// 					break;
	// 				case "fAllByOwnerAndUpdatedWP":
	// 					body = body.withUpdated( parts[2] );
	// 					break;
	// 				default:
	// 					System.out.print( "Next: " );
	// 					System.out.print( "\033[1;32m" );
	// 					System.out.println( page.getPadding() );
	// 					System.out.print( "\033[0m" );
	// 					throw new ClientException( "Invalid next request action" );
	// 			}
	// 			offset = Integer.parseInt( parts[3] );
	// 		}
	// 		else {
	// 			throw new ClientException( "Invalid next request" );
	// 		}
	// 	}

	// 	// If filter by creation time is available.
	// 	if( body.created() != null )
	// 	{
	// 		timestamp = Timestamp.valueOf( DateParser.localDateTime( body.created() ) );
	// 		tasks = this.task.findAllByOwnerAndCreatedOnWithPaddingLimit( owner.getId(), timestamp, 4, offset ).get();
	// 		page = this.pageBuilder( page, "Task", "fAllByOwnerAndCreatedWP", timestamp.toLocalDateTime().toString(), offset +4 );
	// 	}
	// 	else {

	// 		// If filter by update time is available.
	// 		if( body.updated() != null )
	// 		{
	// 			timestamp = Timestamp.valueOf( DateParser.localDateTime( body.updated() ) );
	// 			tasks = this.task.findAllByOwnerAndUpdatedOnWithPaddingLimit( owner.getId(), timestamp, 4, offset ).get();
	// 			page = this.pageBuilder( page, "Task", "fAllByOwnerAndUpdatedWP", timestamp.toLocalDateTime().toString(), offset +4 );
	// 		}
	// 		else {
	// 			tasks = this.task.findAllByOwnerWithPaddingLimit( owner.getId(), 4, offset ).get();
	// 			page = this.pageBuilder( page, "Task", "fAllByOwnerWP", owner.getId(), offset +4 );
	// 		}
	// 	}

	// 	// Check if next page is available.
	// 	if( tasks.size() >= 1 )
	// 	{
	// 		this.page.save( page );
	// 	}
	// 	else {
	// 		if( page.getId() != null )
	// 		{
	// 			this.page.delete( page );
	// 		}
	// 		page.setPadding( null );
	// 		page.setToken( null );
	// 	}
	// 	return( new ResponseEntity<>(
	// 		new BodyResponse<>( "success", "ok",
	// 			HttpStatus.OK.value(),
	// 			new TasksResponse<>(
	// 				page.getToken(),
	// 				tasks.size(),
	// 				tasks
	// 			)
	// 		),
	// 		HttpStatus.OK
	// 	));
	// }

	// /*
	//  * Overloading method of method pageBuilder.
	//  * 
	//  * @access Private
	//  * 
	//  * @params Page page
	//  * @params String model
	//  * @params String action
	//  * @params Long value
	//  * @params int offset
	//  * 
	//  * @return Page
	//  */
	// private Page pageBuilder( Page page, String model, String action, Long value, int offset ) throws Exception
	// {
	// 	return( this.pageBuilder( page, model, action, value + "", offset ) );
	// }

	// /*
	//  * Page model builder.
	//  * 
	//  * @access Private
	//  * 
	//  * @params Page page
	//  * @params String model
	//  * @params String action
	//  * @params String value
	//  * @params Long length
	//  * @params int offset
	//  * 
	//  * @return Page
	//  */
	// private Page pageBuilder( Page page, String model, String action, String value, int offset ) throws Exception
	// {
	// 	LocalDateTime expires = DateParser.localDateTime( "+32 minutes" );
	// 	String padding = model + "/";
	// 	padding += action + "/";
	// 	padding += value + "/";
	// 	padding += offset;

	// 	// Normalize page.
	// 	page = page != null ? page : new Page();

	// 	// Generate new random password and salt.
	// 	byte[] pasw = Security.salt( 64 );
	// 	byte[] salt = Security.salt( 128 );
		
	// 	// Create new Secret Key.
	// 	SecretKeySpec ksec = Message.ksec( ByteParser.bytesToChars( pasw ), salt );
	// 	IvParameterSpec ivp = Message.ivp();

	// 	// Extract iv and secret.
	// 	String ivps = Base64.getEncoder().encodeToString( ivp.getIV() );
	// 	String token = Hex.encode( Security.salt( 64 ) );
	// 	String secret = String.format( "%s:%s", Hex.encode( pasw ), Base64.getEncoder().encodeToString( salt ) );
		
	// 	// Encrypting padding.
	// 	padding = Message.encrypt( "AES/CBC/PKCS5Padding", padding, ksec, ivp );

	// 	// Update page infor.
	// 	page.setIvp( ivps )
	// 		.setToken( token )
	// 		.setSecret( secret )
	// 		.setExpires( expires )
	// 		.setPadding( padding );
		
	// 	return( page );
	// }

}
