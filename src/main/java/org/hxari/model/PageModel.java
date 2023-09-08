package org.hxari.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.hxari.util.DateParserUtil;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table( name="pages" )
public class PageModel
{

	@Id
	@GeneratedValue( strategy=GenerationType.IDENTITY )
	private Long id;

	@Column( name="offset" )
	private Integer offset;

	@Column( name="token", length=128, nullable=false )
	private String token;

	@ManyToOne
	@JsonIgnore
	@JoinColumn( name="owner", nullable=false )
	private UserModel owner;

	@Column( name="filter", length=32, nullable=false )
	private String filter;

	@Column( name="created", nullable=false, updatable=false )
	private Timestamp created;

	@Column( name="expires", nullable=false, updatable=true )
	private Timestamp expires;

	@Column( name="updated", nullable=false, updatable=false )
	private Timestamp updated;

	public PageModel() {
	}

	public PageModel( Integer offset, String token, UserModel owner, String filter ) {
		this.offset = offset;
		this.token = token;
		this.owner = owner;
		this.filter = filter;
	}

	public PageModel( Integer offset, String token, UserModel owner, String filter, Timestamp expires ) {
		this.offset = offset;
		this.token = token;
		this.owner = owner;
		this.filter = filter;
		this.expires = expires;
	}

	public Long getId() {
		return( this.id );
	}

	public Integer getOffset() {
		return( this.offset );
	}

	public String getToken() {
		return( this.token );
	}

	public String getFilter() {
		return( this.filter );
	}

	public UserModel getOwner() {
		return( this.owner );
	}

	public Timestamp getCreated() {
		return( this.created );
	}

	public Timestamp getExpires() {
		return( this.expires );
	}

	public Timestamp getUpdated() {
		return( this.updated );
	}

	@PrePersist
	public void onInsert() {
		this.created = Timestamp.from( ZonedDateTime.now( ZoneId.of( "Asia/Jakarta" ) ).toInstant() );
		this.updated = this.created;
	}

	@PreUpdate
	public void onUpdate() {
		this.updated = Timestamp.from( ZonedDateTime.now( ZoneId.of( "Asia/Kolkata" ) ).toInstant() );
	}

	public PageModel setOffset( Integer offset ) {
		this.offset = offset;
		return( this );
	}

	public PageModel setToken( String token ) {
		this.token = token;
		return( this );
	}

	public PageModel setFilter( String filter ) {
		this.filter = filter;
		return( this );
	}

	public PageModel setOwner( UserModel owner ) {
		this.owner = owner;
		return( this );
	}

	public PageModel setExpires( String expires ) {
		return( this.setExpires( DateParserUtil.localDateTime( expires ) ) );
	}

	public PageModel setExpires( LocalDateTime expires ) {
		return( this.setExpires( Timestamp.valueOf( expires ) ) );
	}

	public PageModel setExpires( Timestamp expires ) {
		this.expires = expires;
		return( this );
	}

}
