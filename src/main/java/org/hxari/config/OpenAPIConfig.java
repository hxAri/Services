package org.hxari.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenAPIConfig
{
	@Bean
	public OpenAPI usersMicroserviceOpenAPI()
	{
		return( new OpenAPI()
			.info( new Info()
				.title( "Service Rest API" )
				.description( "Web Service Rest API" )
				.version( "1.0.0" )
			)
		);
	}
}
