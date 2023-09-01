package org.hxari.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table( name="tokens" )
public class Token
{

	@Id
	@GeneratedValue( strategy=GenerationType.IDENTITY )
	private Long id;

	@Column( name="uuids", length=258, nullable=false )
	private String uuids;

	@Column( name="token", length=258, nullable=false )
	private String token;

	@ManyToOne( fetch=FetchType.LAZY )
	@JoinColumn( name="user_id", nullable=false )
	private User user;

	@Column( name="created", nullable = false, updatable=false )
	private Timestamp created;

	@Column( name="updated", nullable=false, updatable=true )
	private Timestamp updated;

	/*
	 * Construct method of class Token.
	 * 
	 * @access Public Initialize
	 * 
	 * @return Void
	 */
	public Token()
	{
		this.created = this.created != null ? this.created : Timestamp.valueOf( LocalDateTime.now() );
		this.updated = this.updated != null ? this.updated : Timestamp.valueOf( LocalDateTime.now() );
	}

	/*
	 * Construct overloading method of class Token.
	 * 
	 * @access Public Initialize
	 * 
	 * @params String uuids
	 * @params String token
	 * 
	 * @return Void
	 */
	public Token( String uuids, String token )
	{
		this();
		this.setUUIDS( uuids )
			.setToken( token );
	}

	/*
	 * Construct overloading method of class Token.
	 * 
	 * @access Public Initialize
	 * 
	 * @params String uuids
	 * @params String token
	 * @params User user
	 * 
	 * @return Void
	 */
	public Token( String uuids, String token, User user )
	{
		this();
		this.setUUIDS( uuids )
			.setToken( token )
			.setUser( user );
	}

	public Long getId()
	{
		return( this.id );
	}

	public String getUUIDS()
	{
		return( this.uuids );
	}

	public String getToken()
	{
		return( this.token );
	}

	public User getUser()
	{
		return( this.user );
	}

	public Token setUUIDS( String uuids )
	{
		this.uuids = uuids;
		return( this );
	}

	public Token setToken( String token )
	{
		this.token = token;
		return( this );
	}

	public Token setUser( User user )
	{
		this.user = user;
		return( this );
	}

	public String toString()
	{
		return( String.format( "%d:%s:%s", this.user.getId(), this.uuids, this.token ) );
	}

}
