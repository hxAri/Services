package org.hxari.payload;

public record UserPayload<Role>( 
	Role role, 
	String fullname, 
	String username, 
	String usermail, 
	String password 
) {}
