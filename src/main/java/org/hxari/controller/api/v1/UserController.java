package org.hxari.controller.api.v1;

import org.hxari.payload.response.BodyResponse;
import org.hxari.payload.response.UserResponse;
import org.hxari.service.JwtService;
import org.hxari.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping( path="/api/v1/user" )
public class UserController {
	
	@Autowired
	private JwtService jwtService;

	@Autowired
	private UserService userService;

	@RequestMapping( method=RequestMethod.GET )
	@PreAuthorize( value="hasAuthority('ROLE_USER')" )
	public ResponseEntity<?> index( @RequestHeader( "Authorization" ) String bearer ) {
		SecurityContext context = SecurityContextHolder.getContext();
		Authentication authentication = context.getAuthentication();
		return( new ResponseEntity<>( 
			new BodyResponse<>( "success", "ok", 200, 
				authentication.getPrincipal()
			),
			HttpStatus.OK
		));
	}
}
