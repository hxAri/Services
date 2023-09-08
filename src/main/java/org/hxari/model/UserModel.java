package org.hxari.model;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

import org.hxari.payload.request.SignInRequest;
import org.hxari.payload.request.UserInfoRequest;

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
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table( name="users" )
public class UserModel {
	
	public enum Role {

		ADMIN( "ROLE_ADMIN" ),
		USER( "ROLE_USER" );

		private String value;

		private Role( String value ) {
			this.value = value;
		}

		public String getValue() {
			return( this.value );
		}

		public String value() {
			return( this.value );
		}
	}

	@Id
	@GeneratedValue( strategy=GenerationType.IDENTITY )
	private Long id;

	@ManyToMany( cascade=CascadeType.ALL, fetch=FetchType.EAGER )
	@JoinTable(
		name = "users_roles",
		joinColumns = @JoinColumn( name="user_id" ),
		inverseJoinColumns = @JoinColumn(name="role_id" )
	)
	@JsonIgnore
	private Set<RoleModel> roles = new HashSet<>();

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
	}

	public UserModel( SignInRequest signin ) {
		this.username = signin.username();
		this.password = signin.password();
	}

	public UserModel( SignInRequest signin, Set<RoleModel> roles ) {
		this.roles = roles;
		this.username = signin.username();
		this.password = signin.password();
	}

	public UserModel( UserInfoRequest body ) {
		this.fullname = body.fullname();
		this.usermail = body.usermail();
		this.username = body.username();
		this.password = body.password();
	}

	public UserModel( UserInfoRequest body, Set<RoleModel> roles ) {
		this.roles = roles;
		this.fullname = body.fullname();
		this.usermail = body.usermail();
		this.username = body.username();
		this.password = body.password();
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

	@PrePersist
	public void onInsert() {
		this.created = Timestamp.from( ZonedDateTime.now( ZoneId.of( "Asia/Jakarta" ) ).toInstant() );
		this.updated = this.created;
	}

	@PreUpdate
	public void onUpdate() {
		this.updated = Timestamp.from( ZonedDateTime.now( ZoneId.of( "Asia/Kolkata" ) ).toInstant() );
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
