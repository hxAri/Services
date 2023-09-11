package org.hxari.api.v1;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import org.hxari.component.PageComponent;
import org.hxari.exception.ClientException;
import org.hxari.exception.UserNotFoundException;
import org.hxari.model.PageModel;
import org.hxari.model.TaskModel;
import org.hxari.model.UserModel;
import org.hxari.payload.request.TaskRequest;
import org.hxari.payload.response.BodyResponse;
import org.hxari.payload.response.TaskItemsResponse;
import org.hxari.payload.response.TaskResponse;
import org.hxari.service.JwtService;
import org.hxari.service.TaskService;
import org.hxari.service.UserService;
import org.hxari.util.DateUtil;
import org.hxari.util.HexUtil;
import org.hxari.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping( path="/api/v1/task" )
public class TaskAPI {

	@Autowired
	private JwtService jwtService;

	@Value( "${service.fetch.next.page-expires}" )
	private String expires;

	@Value( "${service.fetch.limit.count}" )
	private Integer limit;

	private List<String> filters;

	@Autowired
	private PageComponent pageComponent;

	@Autowired
	private TaskService taskService;

	@Autowired
	private UserService userService;

	public TaskAPI( @Value( "${service.fetch.filter.task}" ) String filter ) {
		this.filters = Arrays.asList( filter.split( "\\|" ) );
	}
	
	@Operation(
		method="POST",
		summary="API for get tasks",
		description="API for get task based on filter",
		parameters={
			@Parameter( name="count", description="Count of task items fetch" ), 
			@Parameter( name="filter", description="Get task items by creation, and updated time or by owner e.g fetchByCreatedTime|fetchByUpdatedTime|fetchByOwner" ), 
			@Parameter( name="offset", description="Offset of page of number of tasks" ), 
			@Parameter( name="next", description="Next token page request, when this usage count and offset value doesn't usage" ),
			@Parameter( name="value", description="Value of filter e.g -4 years, 123456" )
		}
	)
	@RequestMapping( method=RequestMethod.POST )
	public ResponseEntity<BodyResponse<TaskItemsResponse<TaskModel>>> all( 
		HttpServletRequest request, 
		@RequestParam( name="count", required=false ) Integer count, 
		@RequestParam( name="filter", required=false ) String filter, 
		@RequestParam( name="offset", required=false ) Integer offset, 
		@RequestParam( name="next", required=false ) String next, 
		@RequestParam( name="value", required=false ) String value 
	) throws Exception {
		
		List<TaskModel> tasks = null;
		Timestamp timestampBegin = null;
		Timestamp timestampEnd = null;
		PageModel page = null;
		
		if( next != null ) {
			page = pageComponent.find( next );
			if( page.isNotExpired() ) {
				if( page.getFilter().equals( "fetchByCreatedTime" ) ||
					page.getFilter().equals( "fetchByUpdatedTime" ) ) {
					value = page.getValue();
				}
				else if( page.getFilter().equals( "fetchByOwner" ) ) {
					value = page.getOwner().getId().toString();
				}
				else {
					throw new ClientException( "Invalid filter request page" );
				}
				filter = page.getFilter();
				offset = page.getOffset();
				count = page.getCounts();
			}
			else {
				throw new ClientException( "Next request page is expired" );
			}
		}
		if( count == null ) {
			count = 4;
		}
		if( count > limit ) {
			throw new ClientException( "Total size is too big" );
		}
		if( offset == null ) {
			offset = 0;
		}
		if( filter == null ) {
			filter = this.filters.get( 0 );
		}
		if( this.filters.contains( filter ) ) {
			System.out.println( "Offset: \033[1;32m" + offset + "\033[0m" );
			System.out.println( "Count: \033[1;32m" + count + "\033[0m" );
			if( filter.equals( "fetchByCreatedTime" ) ||
				filter.equals( "fetchByUpdatedTime" ) ) {
				timestampBegin = Timestamp.valueOf( DateUtil.parse( value ) );
				timestampEnd = Timestamp.valueOf( DateUtil.newYearAfter( value ) );
			}
			if( page == null ) {
				page = new PageModel();
				page.setSizes( this.taskService.count() );
			}
			UserModel owner = this.getOwner( request, filter, value );
			Pageable pageable = null;
			switch( filter ) {
				case "fetchByCreatedTime":
					pageable = PageRequest.of( offset, count ).withSort( Direction.DESC, "created" );
					tasks = this.taskService.findAllByOwnerAndCreatedAfterAndCreatedBefore( owner, timestampBegin, timestampEnd, pageable );
					break;
				case "fetchByUpdatedTime":
					pageable = PageRequest.of( offset, count ).withSort( Direction.DESC, "updated" );
					tasks = this.taskService.findAllByOwnerAndUpdatedAfter( owner, timestampBegin, /** timestampEnd, */ pageable );
					break;
				case "fetchByOwner":
				default:
					pageable = PageRequest.of( offset, count ).withSort( Direction.DESC, "id" );
					tasks = this.taskService.findAllByOwner( owner, pageable );
					value = owner.getId().toString();
					break;
			}
			System.out.println( "Timestamp" );
			System.out.println( "  Begin: \033[1;32m" + timestampBegin + "\033[0m" );
			System.out.println( "  End: \033[1;32m" + timestampEnd + "\033[0m" );
			if( tasks.size() >= 1 ) {
				if( page.getId() == null ) {
					page.setCounts( count );
				}
				this.pageComponent.save(
					page.setExpires( Timestamp.valueOf( DateUtil.parse( this.expires ) ) )
						.setFilter( filter )
						.setOffset( offset +1 )
						.setOwner( owner )
						.setToken( HexUtil.encode( SecurityUtil.salt( 32 ) ) )
						.setValue( value )
				);
			}
			else {
				if( page.getId() != null ) {
					this.pageComponent.delete( page );
				}
			}
			return( new ResponseEntity<>(
				new BodyResponse<>( "success", "ok", 200,
					new TaskItemsResponse<>(
						page.getToken(), 
						tasks.size(), 
						tasks
					)
				), 
				HttpStatus.OK 
			));
		}
		System.out.println( "\033[1;32m" + filter + "\033[0m" );
		System.out.println( "\033[1;32m" + filters + "\033[0m" );
		throw new ClientException( "Invalid request filter" );
	}

	@Operation(
		method="GET",
		summary="API for get task by id",
		description="API for get task by id"
	)
	@RequestMapping( path="/{id}", method=RequestMethod.GET )
	public ResponseEntity<BodyResponse<TaskResponse<TaskModel>>> get( @PathVariable Long id ) {
		return( new ResponseEntity<>(
			new BodyResponse<>( "success", "ok", 200,
				new TaskResponse<>( this.taskService.findById( id ) )
			),
			HttpStatus.OK
		));
	}

	@Operation(
		method="DELETE",
		summary="API for delete task by task id",
		description="API for delete task by id"
	)
	@RequestMapping( path="/{id}", method=RequestMethod.DELETE )
	public ResponseEntity<BodyResponse<Void>> delete( @PathVariable Long id ) {
		this.taskService.delete( id );
		return( new ResponseEntity<>(
			new BodyResponse<>( "deleted", "ok", 204, null ),
			HttpStatus.NO_CONTENT
		));
	}

	@Operation(
		method="PUT",
		summary="API for update task info by task id",
		description="API for update task info by task id"
	)
	@RequestMapping( path="/{id}", method=RequestMethod.PUT )
	public ResponseEntity<BodyResponse<TaskResponse<TaskModel>>> update( @PathVariable Long id, @RequestBody( required=true ) TaskRequest body ) {
		TaskModel task = this.taskService.save( this.taskService.findById( id ), body );
		return( new ResponseEntity<>(
			new BodyResponse<>( "updated", "ok", 200,
				new TaskResponse<>( task )
			),
			HttpStatus.OK
		));
	}

	@Operation(
		method="POST",
		summary="API for create new task",
		description="API for create new task"
	)
	@RequestMapping( path="/insert", method=RequestMethod.POST )
	public ResponseEntity<BodyResponse<TaskResponse<TaskModel>>> insert( HttpServletRequest request, @RequestBody( required=true ) TaskRequest body ) throws Exception {
		TaskModel task = this.taskService.save( new TaskModel( body.title(), this.getOwner( request ) ) );
		return( new ResponseEntity<>(
			new BodyResponse<>( "created", "ok", 200,
				new TaskResponse<>( task )
			),
			HttpStatus.OK
		));
	}

	private UserModel getOwner( HttpServletRequest request, String filter, String value ) throws Exception {
		if( filter.endsWith( "Owner" ) ) {
			if( value != null ) {
				System.out.println( "\033[1;32m" + value + "\033[0m" );
				try {
					return( this.userService.findById( Long.parseLong( value ) ) );
				}
				catch( NumberFormatException | UserNotFoundException e ) {
					throw new ClientException( "Invalid request body" );
				}
			}
		}
		return( this.getOwner( request ) );
	}

	private UserModel getOwner( HttpServletRequest request ) throws Exception {
		return( this.jwtService.getUserModel( request.getHeader( HttpHeaders.AUTHORIZATION ) ) );
	}
}
