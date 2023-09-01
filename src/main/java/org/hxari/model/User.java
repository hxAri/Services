package org.hxari.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.validator.constraints.Length;

@Entity
@Table( name="users" )
public class User
{

	@Id
	@GeneratedValue( strategy=GenerationType.IDENTITY )
	private Long id;

	@Column( name="fullname", length=30, nullable=false )
	private String fullname;

	@Column( name="username", length=30, nullable=false, unique=true )
	@Length( min=3, message="Username length must be greater than 3 chars and lower than 30" )
	private String username;

	@Column( name="usermail", length=120, nullable=false, unique=true )
	private String usermail;

	@Column( name="password", length=386, nullable=false )
	@Length( min=8, message="Password length must be greater than 8 chars" )
	@JsonIgnore
	private String password;

	@Column( name="created", nullable = false, updatable=false )
	private Timestamp created;

	@Column( name="updated", nullable=false, updatable=true )
	private Timestamp updated;

	// @OneToMany( mappedBy="user", cascade=CascadeType.ALL, orphanRemoval=true )
	// @JsonIgnore
	// private List<Collaborator> collaborators;

	// @OneToMany( mappedBy="user", cascade=CascadeType.ALL, orphanRemoval=true )
	// @JsonIgnore
	// private List<Comment> comments;

	// @OneToMany( mappedBy="owner", cascade=CascadeType.ALL, orphanRemoval=true )
	// @JsonIgnore
	// private List<Order> orders;

	@OneToMany( mappedBy="owner", cascade=CascadeType.ALL, orphanRemoval=true )
	@JsonIgnore
	private List<Task> tasks;

	/*
	 * Construct method of class User.
	 * 
	 * @access Public Initialize
	 * 
	 * @return Void
	 */
	public User()
	{
		this.created = this.created != null ? this.created : Timestamp.valueOf( LocalDateTime.now() );
		this.updated = this.updated != null ? this.updated : Timestamp.valueOf( LocalDateTime.now() );
	}

	/*
	 * Construct overloading method of class User.
	 * 
	 * @access Public Initialize
	 * 
	 * @params String username
	 * @params String password
	 * 
	 * @return Void
	 */
	public User( String username, String password )
	{
		this();
		this.setUsermail( usermail )
			.setPassword( password );
	}

	/*
	 * Construct overloading method of class User.
	 * 
	 * @access Public Initialize
	 * 
	 * @params String fullname
	 * @params String username
	 * @params String usermail
	 * @params String password
	 * 
	 * @return Void
	 */
	public User( String fullname, String username, String usermail, String password )
	{
		this();
		this.setFullname( fullname )
			.setUsermail( usermail )
			.setUsermail( usermail )
			.setPassword( password );
	}

	public Long getId()
	{
		return( this.id );
	}

	public String getFullname()
	{
		return( this.fullname );
	}

	public String getUsername()
	{
		return( this.username );
	}

	public String getUsermail()
	{
		return( this.usermail );
	}

	public String getPassword()
	{
		return( this.password );
	}

	public Timestamp getCreated()
	{
		return( this.created );
	}

	public Timestamp getUpdated()
	{
		return( this.updated );
	}

	// public List<Collaborator> getCollaborators()
	// {
	// 	return( this.collaborators );
	// }

	// public List<Comment> getComments()
	// {
	// 	return( this.comments );
	// }

	// public List<Order> getOrders()
	// {
	// 	return( this.orders );
	// }

	public List<Task> getTasks()
	{
		return( this.tasks );
	}

	public User setFullname( String fullname )
	{
		this.fullname = fullname;
		return( this );
	}

	public User setUsername( String username )
	{
		this.username = username;
		return( this );
	}

	public User setUsermail( String usermail )
	{
		this.usermail = usermail;
		return( this );
	}

	public User setPassword( String password )
	{
		this.password = password;
		return( this );
	}

}
