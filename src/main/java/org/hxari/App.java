package org.hxari;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpHeaders;

@SpringBootApplication
public class App {

	public static void main( String[] args ) {
		// System.out.println( HttpHeaders.AUTHORIZATION );
		SpringApplication.run( App.class, args );
	}

}
