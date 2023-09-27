package org.hxari.api.v1;

import org.hxari.payload.response.BodyResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping( path="/api/v1/test" )
public class TestAPI {

	// @Autowired
	// private UserService service;

	@RequestMapping( method=RequestMethod.GET )
	public ResponseEntity<BodyResponse<Void>> index() throws Exception {
		// service.create( new UserModel( Set.of( Role.USER ) ) 
		// 	.setFullname( "User" )
		// 	.setUsername( "user" )
		// 	.setUsermail( "user@user.io" )
		// 	.setPassword( "user" )
		// );
		return new ResponseEntity<>(
			new BodyResponse<>( 
				"success", 
				"ok", 
				200, 
				null 
			),
			HttpStatus.OK
		);
	}
}
