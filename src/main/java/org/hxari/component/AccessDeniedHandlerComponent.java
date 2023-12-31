package org.hxari.component;

import java.io.IOException;

import org.hxari.payload.response.BodyResponse;
import org.hxari.payload.response.ErrorResponse;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AccessDeniedHandlerComponent implements AccessDeniedHandler {

	@Override
	public void handle( HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException ) throws IOException, ServletException {
		response.setContentType( MediaType.APPLICATION_JSON_VALUE );
		response.setStatus( HttpServletResponse.SC_FORBIDDEN );
		final ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue( 
			response.getOutputStream(), 
			new BodyResponse<>( 
				accessDeniedException.getMessage(), "forbidden", 403, 
				new ErrorResponse( 
					request.getServletPath(), 
					accessDeniedException.getClass().getName() 
				) 
			) 
		);
	}
}
