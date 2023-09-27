package org.hxari.config;

import org.springframework.context.annotation.Bean;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;

@Configuration
public class ElasticsearchConfig {

	@Value( "${service.elastic.host}" )
	private String host;

	@Value( "${service.elastic.cluster.uuid}" )
	private String uuid;

	@Bean
	public ElasticsearchClient esClient() {
		return new ElasticsearchClient( this.restClientTransport() );
	}

	public RestClient restClient() {
		return RestClient
			.builder( HttpHost.create( this.host ) )
			.setDefaultHeaders(
				new Header[]{
					new BasicHeader( HttpHeaders.AUTHORIZATION, String.format( "ApiKey %s", this.uuid ) )
				}
			)
			.build();
	}

	public ElasticsearchTransport restClientTransport() {
		return new RestClientTransport( this.restClient(), 
			new JacksonJsonpMapper( new ObjectMapper()
				.registerModule( 
					new JavaTimeModule() 
				) 
			)
		);
	}

}
