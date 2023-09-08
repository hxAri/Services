package org.hxari.payload.request;

public record UserInfoRequest(
	String fullname,
	String usermail,
	String username,
	String password
) {}
