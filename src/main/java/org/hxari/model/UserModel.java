package org.hxari.model;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Set;

import org.hxari.model.RoleModel.Role;
import org.hxari.payload.request.UserInfoRequest;

import com.fasterxml.jackson.annotation.JsonFormat;

public class UserModel {

	private Set<Role> roles;
	private String fullname;
	private String username;
	private String usermail;
	private String password;

	@JsonFormat( pattern="yyyy-MM-dd'T'HH:mm:ss" )
	private LocalDateTime created;

	@JsonFormat( pattern="yyyy-MM-dd'T'HH:mm:ss" )
	private LocalDateTime updated;

	public UserModel() {
		this.created = this.created != null ? this.created : LocalDateTime.now( ZoneId.of( "Asia/Jakarta" ) );
		this.updated = this.updated != null ? this.updated : LocalDateTime.now( ZoneId.of( "Asia/Jakarta" ) );
	}

	public UserModel( Set<Role> roles ) {
		this();
		this.roles = roles;
	}

	public UserModel( Set<Role> roles, UserInfoRequest body ) {
		this( roles );
		this.fullname = body.fullname();
		this.username = body.username();
		this.usermail = body.usermail();
		this.password = body.password();
	}

	public UserModel( Set<Role> roles, String fullname, String username, String usermail, String password ) {
		this();
		this.roles = roles;
		this.fullname = fullname;
		this.username = username;
		this.usermail = usermail;
		this.password = password;
	}

	public UserModel( Set<Role> roles, String fullname, String username, String usermail, String password, LocalDateTime created, LocalDateTime updated ) {
		this.roles = roles;
		this.fullname = fullname;
		this.username = username;
		this.usermail = usermail;
		this.password = password;
		this.created = created;
		this.updated = updated;
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

	public Set<Role> getRoles() {
		return( roles );
	}

	public LocalDateTime getCreated() {
		return( this.created );
	}

	public LocalDateTime getUpdated() {
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
	
	public UserModel setRoles( Set<Role> roles ) {
		this.roles = roles;
		return( this );
	}

	public UserModel setUpdated() {
		return( this.setUpdated( LocalDateTime.now( ZoneId.of( "Asia/Jakarta" ) ) ) );
	}

	public UserModel setUpdated( LocalDateTime updated ) {
		this.updated = updated;
		return( this );
	}

}
