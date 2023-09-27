package org.hxari.service;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.List;

import org.hxari.exception.ClientException;
import org.hxari.exception.UserException;
import org.hxari.exception.UserNotFoundException;
import org.hxari.model.UserModel;
import org.hxari.payload.hit.ElasticHit;
import org.hxari.payload.request.UserInfoRequest;
import org.hxari.payload.request.UserUpdateRequest;
import org.hxari.util.HexUtil;
import org.hxari.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch._types.Refresh;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.HitsMetadata;
import co.elastic.clients.elasticsearch.core.search.TotalHits;

@Service
public class UserService {

	@Autowired
	private PasswordEncoder paswEncoder;

	@Autowired
	private ElasticsearchClient client;

	@Value( "${spring.security.password.cost}" )
	private Integer cost;

	@Value( "${service.elastic.index.user}" )
	private String index;

	/** Insert new user document */
	public void create( UserModel user ) throws IOException, NoSuchAlgorithmException, NoSuchProviderException {
		this.create( HexUtil.encode( SecurityUtil.salt( 6 ) ), user );
	}

	/** Insert new user document with id */
	public void create( String id, UserModel user ) throws IOException {
		if( this.existsByUsername( user.getUsername() ) ) {
			throw new UserException( "Username already exists" );
		}
		if( this.existsByUsermail( user.getUsermail() ) ) {
			throw new UserException( "Email Address in aleady use" );
		}
		if( user.getPassword() == null ) throw new UserException( "Password can't be empty" );
		if( user.getPassword().length() < this.cost ) {
			user.setPassword( this
				.paswEncoder
				.encode( 
					user.getPassword() 
				)
			);
		}
		this.client.create( request -> request.index( this.index ).id( id ).document( user ).refresh( Refresh.True ) );
	}

	/** Return if username is exists */
	public boolean existsByUsername( String username ) throws IOException {
		try {
			return this.getByStr( "username", username ).source().getUsername().equalsIgnoreCase( username );
		}
		catch( ElasticsearchException|UserNotFoundException e ) {
			return false;
		}
	}

	/** Return if usermail is exists */
	public boolean existsByUsermail( String usermail ) throws IOException {
		try {
			return this.getByStr( "usermail", usermail ).source().getUsermail().equalsIgnoreCase( usermail );
		}
		catch( ElasticsearchException|UserNotFoundException e ) {
			return false;
		}
	}

	/** Delete user by id */
	public void delete( String id ) throws IOException {
		this.client.delete( delete -> delete.index( this.index ).id( id ) );
		this.client.indices().refresh();
	}

	/** Get user by id */
	public ElasticHit<UserModel> getById( String id ) throws IOException {
		this.client.indices().refresh();
		try {
			GetResponse<UserModel> response = this.client.get( request -> request.index( this.index ).id( id ), UserModel.class );
			if( response.found() ) {
				return new ElasticHit<>( 
					response.id(), 
					response.index(), 
					response.source() 
				);
			}
			throw new UserNotFoundException( "User not found" );
		}
		catch( IOException e ) {
			throw e;
		}
	}

	/** Get user by field match */
	public ElasticHit<UserModel> getByStr( String field, String value ) throws IOException {
		try {
			SearchResponse<UserModel> response = this.client.search( search -> search
				.index( this.index )
				.query( query -> query 
					.bool( bool -> bool
						.must( must -> must
							.match( match -> match
								.field( field )
								.query( value )
							)
						)
					)
				),
				UserModel.class 
			);
			HitsMetadata<UserModel> meta = response.hits();
			TotalHits total = meta.total();
			if( total != null ) {
				if( total.value() >= 1 ) {
					List<Hit<UserModel>> hits = meta.hits();
					Hit<UserModel> hit = hits.get( 0 );
					return new ElasticHit<UserModel>(
						hit.id(),
						hit.index(),
						hit.source()
					);
				}
			}
			throw new UserNotFoundException( "User not found" );
		}
		catch( IOException e ) {
			throw e;
		}
	}

	/** Get user by username */
	public ElasticHit<UserModel> getByUsername( String username ) throws IOException {
		return this.getByStr( "username", username );
	}

	/** Get user by usermail */
	public ElasticHit<UserModel> getByUsermail( String usermail ) throws IOException {
		return this.getByStr( "usermail", usermail );
	}

	/** Return if index is exists */
	public boolean isIndexExists() throws IOException {
		try {
			return client
				.indices()
				.exists( indice -> indice.index( this.index ) )
				.value();
		}
		catch( IOException e ) {
			return false;
		}
	}

	public void update( ElasticHit<UserModel> hit, UserUpdateRequest body ) throws IOException {
		this.update( hit, new UserInfoRequest( body.fullname(), body.username(), body.usermail(), null ) );
	}
	
	/** Update user info */
	public void update( ElasticHit<UserModel> hit, UserInfoRequest body ) throws IOException
	{
		if( body.fullname() != null && hit.source().getFullname() != body.fullname() )
			hit.source().setFullname( body.fullname() );//.setUpdated();
		if( body.password() != null && hit.source().getPassword() != body.password() )
			hit.source().setPassword( this.paswEncoder.encode( body.password() ) );//.setUpdated();
		if( body.usermail() != null && hit.source().getUsermail() != body.usermail() ) {
			if( this.existsByUsermail( body.usermail() ) ) {
				throw new ClientException( "Email Address is already in use" );
			}
			hit.source().setUsermail( body.usermail() );//.setUpdated();
		}
		if( body.username() != null && hit.source().getUsername() != body.username() ) {
			if( this.existsByUsername( body.username() ) ) {
				throw new ClientException( "Username is already exists" );
			}
			hit.source().setUsername( body.username() );//.setUpdated();
		}
		this.client.update( update -> update.index( this.index ).id( hit.id() ).doc( hit.source() ).refresh( Refresh.True ), UserModel.class );
	}
}
