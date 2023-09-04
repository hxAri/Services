package org.hxari.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table( name="tasks" )
public class TaskModel {

	@Id
	@GeneratedValue( strategy=GenerationType.IDENTITY )
	private Long id;

	@Column( length=100, nullable=false )
	private String title;

	@ManyToOne
	@JoinColumn( name="owner_id", nullable=false )
	private UserModel owner;

	@Column( name="created", nullable = false, updatable=false )
	private Timestamp created;

	@Column( name="updated", nullable=false, updatable=true )
	private Timestamp updated;

	/*
	 * Construct method of class Task.
	 * 
	 * @access Public Initialize
	 * 
	 * @return Void
	 */
	public TaskModel() {
		this.created = this.created != null ? this.created : Timestamp.valueOf( LocalDateTime.now() );
		this.updated = this.updated != null ? this.updated : Timestamp.valueOf( LocalDateTime.now() );
	}

	/*
	 * Construct overloading method of class Task.
	 * 
	 * @access Public Initialize
	 * 
	 * @params String title
	 * @params User owner
	 * 
	 * @return Void
	 */
	public TaskModel( String title, UserModel owner ) {
		this();
		this.setTitle( title );
		this.setOwner( owner );
	}

	public Long getId() {
		return( this.id );
	}

	public String getTitle() {
		return( this.title );
	}

	public UserModel getOwner() {
		return( this.owner );
	}

	public Timestamp getCreated() {
		return( this.created );
	}

	public Timestamp getUpdated() {
		return( this.updated );
	}

	public TaskModel setTitle( String title ) {
		this.title = title;
		return( this );
	}

	public TaskModel setOwner( UserModel owner ) {
		this.owner = owner;
		return( this );
	}

}
