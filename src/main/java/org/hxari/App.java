package org.hxari;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;

@SpringBootApplication
@OpenAPIDefinition
public class App {
	public static void main( String[] args ) throws Exception {
		SpringApplication.run( App.class, args );
	}
}
