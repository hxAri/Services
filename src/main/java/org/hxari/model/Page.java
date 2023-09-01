package org.hxari.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.hxari.util.DateParser;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table( name="pages" )
public class Page
{

	@Id
	@GeneratedValue( strategy=GenerationType.IDENTITY )
	private Long id;

	@Column( name="ivp", length=32, nullable=false )
	private String ivp;

	@Column( name="token", length=128, nullable=false )
	private String token;

	@Column( name="secret", length=386, nullable=false )
	private String secret;

	@Column( name="padding", length=88, nullable=false )
	private String padding;

	@Column( name="created", nullable=false, updatable=false )
	private Timestamp created;

	@Column( name="expires", nullable=false, updatable=false )
	private Timestamp expires;

	/*
	 * Construct method of class Page.
	 * 
	 * @access Public Initialize
	 * 
	 * @return Void
	 */
	public Page()
	{
		this.expires = this.expires != null ? this.expires : Timestamp.valueOf( DateParser.localDateTime( "+4 minutes" ) );
		this.created = this.created != null ? this.created : Timestamp.valueOf( LocalDateTime.now() );
	}

	/*
	 * Construct overloading method of class Page.
	 * 
	 * @access Public Initialize
	 * 
	 * @params String ivp
	 * @params String token
	 * @params String secret
	 * @params String padding
	 * @params Timestamp expires
	 * 
	 * @return Void
	 */
	public Page( String ivp, String token, String secret, String padding, Timestamp expires )
	{
		this();
		this.setIvp( ivp )
			.setToken( token )
			.setSecret( secret )
			.setPadding( padding );
	}

	public Long getId()
	{
		return( this.id );
	}

	public String getIvp()
	{
		return( this.ivp );
	}

	public String getToken()
	{
		return( this.token );
	}

	public String getSecret()
	{
		return( this.secret );
	}

	public String getPadding()
	{
		return( this.padding );
	}

	public Timestamp getCreated()
	{
		return( this.created );
	}

	public Timestamp getExpires()
	{
		return( this.expires );
	}

	public Page setIvp( String ivp )
	{
		this.ivp = ivp;
		return( this );
	}

	public Page setToken( String token )
	{
		this.token = token;
		return( this );
	}

	public Page setSecret( String secret )
	{
		this.secret = secret;
		return( this );
	}

	public Page setPadding( String padding )
	{
		this.padding = padding;
		return( this );
	}

	public Page setExpires( String expires )
	{
		return( this.setExpires( DateParser.localDateTime( expires ) ) );
	}

	public Page setExpires( LocalDateTime expires )
	{
		return( this.setExpires( Timestamp.valueOf( expires ) ) );
	}

	public Page setExpires( Timestamp expires )
	{
		this.expires = expires;
		return( this );
	}

}
