package org.hxari.payload.response;

import java.util.List;
import java.util.stream.Collectors;

import org.hxari.service.UserDetailsServiceImplement.UserDetailsImplement;

public class JwtResponse {

	private Long id;
	private String token;
	private String fullname;
	private String usermail;
	private String username;
	private List<String> roles;

	public JwtResponse( String token, UserDetailsImplement user ) {
		this.token = token;
		this.id = user.getId();
		this.fullname = user.getFullname();
		this.usermail = user.getUsermail();
		this.username = user.getUsername();
		this.roles = user
			.getAuthorities()
			.stream()
			.map( item -> item.getAuthority() )
			.collect( Collectors.toList() );
	}

	public Long getId() {
		return( this.id );
	}

	public String getToken() {
		return( this.token );
	}

	public String getFullname() {
		return( this.fullname );
	}

	public String getUsermail() {
		return( this.usermail );
	}

	public String getUsername() {
		return( this.username );
	}

	public List<String> getRoles() {
		return( this.roles );
	}
}
