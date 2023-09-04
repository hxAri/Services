package org.hxari.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.validator.constraints.Length;

@Entity
@Table( name="users" )
public class UserModel {

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

	@ManyToMany( fetch=FetchType.LAZY)
	@JoinTable( 
		name="user_roles", 
		joinColumns=@JoinColumn( name="user_id" ), 
		inverseJoinColumns=@JoinColumn( name="role_id" )
	)
	private Set<RoleModel> roles = new HashSet<>();

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
	private List<TaskModel> tasks;

	/*
	 * Construct method of class UserModel.
	 * 
	 * @access Public Initialize
	 * 
	 * @return Void
	 */
	public UserModel() {
		this.created = this.created != null ? this.created : Timestamp.valueOf( LocalDateTime.now() );
		this.updated = this.updated != null ? this.updated : Timestamp.valueOf( LocalDateTime.now() );
	}

	/*
	 * Construct overloading method of class UserModel.
	 * 
	 * @access Public Initialize
	 * 
	 * @params String username
	 * @params String password
	 * 
	 * @return Void
	 */
	public UserModel( String username, String password ) {
		this();
		this.setUsermail( usermail );
		this.setPassword( password );
	}

	/*
	 * Construct overloading method of class UserModel.
	 * 
	 * @access Public Initialize
	 * 
	 * @params String fullname
	 * @params String username
	 * @params String usermail
	 * @params String password
	 * @params Set<RoleModel> roles
	 * 
	 * @return Void
	 */
	public UserModel( String fullname, String username, String usermail, String password, Set<RoleModel> roles ) {
		this();
		this.setRoles( roles );
		this.setFullname( fullname );
		this.setUsermail( usermail );
		this.setUsermail( usermail );
		this.setPassword( password );
	}

	public Long getId() {
		return( this.id );
	}

	public String getFullname() {
		return( this.fullname );
	}

	public String getUsername() {
		return( this.username );
	}

	public String getUsermail() {
		return( this.usermail );
	}

	public String getPassword() {
		return( this.password );
	}

	public Set<RoleModel> getRoles() {
		return( roles );
	}

	public Timestamp getCreated() {
		return( this.created );
	}

	public Timestamp getUpdated() {
		return( this.updated );
	}

	// public List<Collaborator> getCollaborators() {
	// 	return( this.collaborators );
	// }

	// public List<Comment> getComments() {
	// 	return( this.comments );
	// }

	// public List<Order> getOrders() {
	// 	return( this.orders );
	// }

	public List<TaskModel> getTasks() {
		return( this.tasks );
	}

	public UserModel setFullname( String fullname ) {
		this.fullname = fullname;
		return( this );
	}

	public UserModel setUsername( String username ) {
		this.username = username;
		return( this );
	}

	public UserModel setUsermail( String usermail ) {
		this.usermail = usermail;
		return( this );
	}

	public UserModel setPassword( String password ) {
		this.password = password;
		return( this );
	}
	
	public UserModel setRoles( Set<RoleModel> roles ) {
		this.roles = roles;
		return( this );
	}

}
