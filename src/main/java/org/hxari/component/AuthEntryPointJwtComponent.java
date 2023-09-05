package org.hxari.component;

import java.io.IOException;
import java.util.HashMap;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthEntryPointJwtComponent implements AuthenticationEntryPoint {

	@Override
	public void commence( HttpServletRequest request, HttpServletResponse response, AuthenticationException ae ) throws ServletException, IOException {
		response.setContentType( MediaType.APPLICATION_JSON_VALUE );
		response.setStatus( HttpServletResponse.SC_UNAUTHORIZED );
		final HashMap<String, Object> body = new HashMap<>();
		body.put( "message", ae.getMessage() );
		body.put( "status", "fail" );
		body.put( "error", "unauthorized" );
		body.put( "path", request.getServletPath() );
		body.put( "ctx", ae.getClass().getName() );
		final ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue( response.getOutputStream(), body );
	}

}
