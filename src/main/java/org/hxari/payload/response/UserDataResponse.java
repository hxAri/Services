package org.hxari.payload.response;

import java.time.LocalDateTime;
import java.util.Set;

import org.hxari.model.RoleModel.Role;

public record UserDataResponse( 
	String id, 
	Set<Role> roles, 
	String fullname,
	String username, 
	String usermail, 
	LocalDateTime created, 
	LocalDateTime updated 
) {
}
