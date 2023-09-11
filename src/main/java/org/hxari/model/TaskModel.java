package org.hxari.model;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table( name="tasks" )
public class TaskModel {

	@Id
	@GeneratedValue( strategy=GenerationType.IDENTITY )
	private Long id;

	@Column( length=64, name="title", nullable=false )
	private String title;

	@ManyToOne( fetch=FetchType.EAGER )
	@JoinColumn( name="owner", nullable=false )
	private UserModel owner;

	@Column( name="created", nullable=false, updatable=false )
	private Timestamp created;

	@Column( name="updated", nullable=false, updatable=true )
	private Timestamp updated;

	public TaskModel() {
	}

	public TaskModel( String title ) {
		this.title = title;
	}

	public TaskModel( UserModel owner ) {
		this.owner = owner;
	}

	public TaskModel( String title, UserModel owner ) {
		this.title = title;
		this.owner = owner;
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

	@PrePersist
	public void onInsert() {
		this.created = Timestamp.from( ZonedDateTime.now( ZoneId.of( "Asia/Jakarta" ) ).toInstant() );
		this.updated = this.created;
	}

	@PreUpdate
	public void onUpdate() {
		this.updated = Timestamp.from( ZonedDateTime.now( ZoneId.of( "Asia/Kolkata" ) ).toInstant() );
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
