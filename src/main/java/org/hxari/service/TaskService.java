package org.hxari.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.NoSuchElementException;

import org.hxari.exception.TaskNotFoundException;
import org.hxari.model.TaskModel;
import org.hxari.model.UserModel;
import org.hxari.payload.request.TaskRequest;
import org.hxari.repository.TaskPageableRepository;
import org.hxari.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private TaskPageableRepository taskPageableRepository;

	public void delete( Long id ) {
		this.taskRepository.deleteById( id );
	}
 
	public TaskModel findById( Long id ) {
		try {
			return( this.taskRepository.findById( id ).get() );
		}
		catch( NoSuchElementException e ) {
			throw new TaskNotFoundException( "Task not found" );
		}
	}
	
	public List<TaskModel> findAllByOwnerAndCreatedBefore( UserModel owner, Timestamp created, Pageable pageable ) {
		return( this.taskPageableRepository.findAllByOwnerAndCreatedBefore( owner, created, pageable ).get() );
	}
	public List<TaskModel> findAllByOwnerAndUpdatedBefore( UserModel owner, Timestamp updated, Pageable pageable ) {
		return( this.taskPageableRepository.findAllByOwnerAndUpdatedBefore( owner, updated, pageable ).get() );
	}
	public List<TaskModel> findAllByOwner( UserModel owner, Pageable pageable ) {
		return( this.taskPageableRepository.findAllByOwner( owner, pageable ).get() );
	}

	public TaskModel save( TaskModel task ) {
		return( this.taskRepository.save( task ) );
	}

	public TaskModel save( TaskModel task, TaskRequest body ) {
		if( body.title() != null && body.title() != task.getTitle() )
			task.setTitle( body.title() );
		return( this.save( task ) );
	}
}
