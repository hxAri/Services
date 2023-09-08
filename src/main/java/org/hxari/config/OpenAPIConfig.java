package org.hxari.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
@OpenAPIDefinition
public class OpenAPIConfig {

	@Value( "${springdoc.swagger-ui.path}" )
	private String path;

	@Bean
	public OpenAPI apiInfo() {
		final var securitySchemeName = "bearerAuth";
		return( new OpenAPI()
			.addSecurityItem( new SecurityRequirement()
				.addList( securitySchemeName )
			)
			.components( new Components()
				.addSecuritySchemes( securitySchemeName, new SecurityScheme()
					.name( securitySchemeName )
					.type( SecurityScheme.Type.HTTP )
					.scheme( "bearer" )
					.bearerFormat( "JWT" )
				)
			)
			.info( new Info()
				.title( "Web Service Rest API with JWT" )
				.version( "1.0" )
			)
		);
	}
}
