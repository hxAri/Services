package org.hxari.controller.api.v1;

import org.hxari.payload.response.BodyResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping( path="/api/v1/user" )
public class UserController {
	
	@RequestMapping( method=RequestMethod.GET )
	// @PreAuthorize( value="hasAuthority('USER_ROLE')" )
	public ResponseEntity<?> index() {
		SecurityContext context = SecurityContextHolder.getContext();
		Authentication authentication = context.getAuthentication();
		return( new ResponseEntity<>( 
			new BodyResponse<>( "success", "ok", 200, authentication.getAuthorities() ),
			HttpStatus.OK
		));
	}
}
