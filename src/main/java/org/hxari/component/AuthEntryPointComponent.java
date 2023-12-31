package org.hxari.component;

import java.io.IOException;

import org.hxari.payload.response.BodyResponse;
import org.hxari.payload.response.ErrorResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthEntryPointComponent implements AuthenticationEntryPoint {

	@Override
	public void commence( HttpServletRequest request, HttpServletResponse response, AuthenticationException authException ) throws IOException, ServletException {
		response.setContentType( MediaType.APPLICATION_JSON_VALUE );
		response.setStatus( HttpServletResponse.SC_UNAUTHORIZED );
		final ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue( 
			response.getOutputStream(), 
			new BodyResponse<>( 
				authException.getMessage(), "unauthorized", 401, 
				new ErrorResponse( 
					request.getServletPath(), 
					authException.getClass().getName() 
				) 
			) 
		);
	}
}
