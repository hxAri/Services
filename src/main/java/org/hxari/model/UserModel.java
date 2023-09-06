package org.hxari.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.hxari.payload.request.SignInRequest;
import org.hxari.payload.request.SignUpRequest;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table( name="users" )
public class UserModel {
	
	public enum Role {
		ADMIN,
		MODER,
		USER
	}

	@Id
    @GeneratedValue( strategy=GenerationType.IDENTITY )
    private Long id;

	@Enumerated( EnumType.STRING )
	@Column( length=20, name="role", nullable=false, columnDefinition="ROLE_USER" )
	private Role role;

	@Column( length=32, name="fullname", nullable=false )
	private String fullname;

	@Column( length=48, name="usermail", nullable=false, unique=true )
	private String usermail;

	@Column( length=30, name="username", nullable=false, unique=true )
	private String username;

	@Column( length=258, name="password", nullable=false )
	@JsonIgnore
	private String password;

	@Column( name="created", nullable = false, updatable=false )
	private Timestamp created;

	@Column( name="updated", nullable=false, updatable=true )
	private Timestamp updated;

	public UserModel() {
		this.created = this.created != null ? this.created : Timestamp.valueOf( LocalDateTime.now() );
		this.updated = this.updated != null ? this.updated : Timestamp.valueOf( LocalDateTime.now() );
	}

	public UserModel( SignInRequest signin ) {
		this();
		this.username = signin.username();
		this.password = signin.password();
	}

	public UserModel( SignInRequest signin, Role role ) {
		this();
		this.role = role;
		this.username = signin.username();
		this.password = signin.password();
	}

	public UserModel( SignUpRequest signup ) {
		this();
		this.fullname = signup.fullname();
		this.usermail = signup.usermail();
		this.username = signup.username();
		this.password = signup.password();
	}

	public UserModel( SignUpRequest signup, Role role ) {
		this();
		this.role = role;
		this.fullname = signup.fullname();
		this.usermail = signup.usermail();
		this.username = signup.username();
		this.password = signup.password();
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

	public Role getRole() {
		return( role );
	}

	public Timestamp getCreated() {
		return( this.created );
	}

	public Timestamp getUpdated() {
		return( this.updated );
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
	
	public UserModel setRole( Role role ) {
		this.role = role;
		return( this );
	}
	
}
