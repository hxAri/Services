package org.hxari.payload.request;

public record SignUpRequest( 
	String fullname, 
	String usermail, 
	String username, 
	String password, 
	String role 
) {}
