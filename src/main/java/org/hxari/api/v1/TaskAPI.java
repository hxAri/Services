package org.hxari.api.v1;

import java.sql.Timestamp;
import java.util.InputMismatchException;
import java.util.List;

import org.hxari.component.PageComponent;
import org.hxari.exception.ClientException;
import org.hxari.model.TaskModel;
import org.hxari.model.UserModel;
import org.hxari.payload.request.TaskFetchParameterRequest;
import org.hxari.payload.request.TaskRequest;
import org.hxari.payload.response.BodyResponse;
import org.hxari.payload.response.TaskItemsResponse;
import org.hxari.payload.response.TaskResponse;
import org.hxari.service.JwtService;
import org.hxari.service.TaskService;
import org.hxari.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
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
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping( path="/api/v1/task" )
public class TaskAPI {

	@Autowired
	private JwtService jwtService;

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
		this.filters = List.of( filter.split( "|" ) );
	}
	
	@Operation(
		method="POST",
		summary="API for get tasks",
		description="API for get task based on filter",
		parameters={
			// Integer count, 
			// String filter,  
			// Integer offset, 
			// String next,
			// String value
		}
	)
	@RequestMapping( method=RequestMethod.POST )
	public ResponseEntity<BodyResponse<TaskItemsResponse<TaskModel>>> all( HttpServletRequest request, @RequestParam( required=false ) TaskFetchParameterRequest params ) throws Exception {
		
		List<TaskModel> tasks = null;
		Timestamp timestamp = null;
		Pageable pageable = null;

		if( params == null )
			params = new TaskFetchParameterRequest( 4, null, 0, null, null );
		if( params.next() != null ) {
		}
		if( params.count() == null ) {
			params = params.withCount( 4 );
		}
		if( params.offset() == null ) {
			params = params.withOffset( 0 );
		}
		if( params.filter() == null ) {
			params = params.withFilter( this.filters.get( 0 ) );
		}
		if( filters.indexOf( params.filter() ) >= 0 ) {
			UserModel owner = this.getOwner( request, params );
			switch( params.filter() ) {
				case "fetchByOwner":
					tasks = this.taskService.findAllByOwner( owner, pageable );
					break;
				case "fetchByCreatedTime":
					tasks = this.taskService.findAllByOwnerAndCreatedBefore( owner, timestamp, pageable );
					break;
				case "fetchByUpdatedTime":
					tasks = this.taskService.findAllByOwnerAndUpdatedBefore( owner, timestamp, pageable );
					break;
				default:
					throw new ClientException( "Invalid request filter" );
			}
			return( new ResponseEntity<>(
				new BodyResponse<>( "success", "ok", 200,
					new TaskItemsResponse<>(
						tasks.size(), null, tasks
					)
				), 
				HttpStatus.OK 
			));
		}
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
		summary="API for delete task by id",
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

	@Operation()
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

	private UserModel getOwner( HttpServletRequest request, TaskFetchParameterRequest body ) throws Exception {
		if( body.filter().contains( "Owner" ) ) {
			if( body.value() != null ) {
				try {
					return( this.userService.findById( Long.parseLong( body.value() ) ) );
				}
				catch( InputMismatchException e ) {
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
