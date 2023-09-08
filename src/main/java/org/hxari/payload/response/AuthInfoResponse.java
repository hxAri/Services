package org.hxari.payload.response;

public record AuthInfoResponse( 
	Boolean authenticated, 
	String authority, 
	String username
) {}
