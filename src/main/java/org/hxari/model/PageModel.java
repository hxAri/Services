package org.hxari.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.hxari.util.DateUtil;

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

	@Column( name="offsets" )
	private Integer offsets;

	@Column( name="token", length=128, nullable=false )
	private String token;

	@ManyToOne
	@JsonIgnore
	@JoinColumn( name="owner", nullable=false )
	private UserModel owner;

	@Column( name="counts", nullable=false )
	private Integer counts;

	@Column( name="sizes", nullable=false )
	private Long sizes;

	@Column( name="filter", length=32, nullable=false )
	private String filter;

	@Column( name="value", length=32, nullable=false )
	private String values;

	@Column( name="created", nullable=false, updatable=false )
	private Timestamp created;

	@Column( name="expires", nullable=false, updatable=true )
	private Timestamp expires;

	@Column( name="updated", nullable=false, updatable=false )
	private Timestamp updated;

	public PageModel() {
	}

	public PageModel( Integer offsets, Integer counts, Long sizes, String token, UserModel owner, String filter, String values ) {
		this.offsets = offsets;
		this.counts = counts;
		this.sizes = sizes;
		this.token = token;
		this.owner = owner;
		this.filter = filter;
		this.values = values;
	}

	public PageModel( Integer offsets, Integer counts, Long sizes, String token, UserModel owner, String filter, String values, Timestamp expires ) {
		this.offsets = offsets;
		this.counts = counts;
		this.sizes = sizes;
		this.token = token;
		this.owner = owner;
		this.filter = filter;
		this.values = values;
		this.expires = expires;
	}

	public Long getId() {
		return( this.id );
	}

	public Integer getCounts() {
		return( this.counts );
	}

	public Integer getOffset() {
		return( this.offsets );
	}

	public Long getSizes() {
		return( this.sizes );
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

	public String getValue() {
		return( this.values );
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

	public boolean isExpired() {
		return( ( this.expires.getTime() / 1000 ) <= ( Timestamp.from( ZonedDateTime.now( ZoneId.of( "Asia/Jakarta" ) ).toInstant() ).getTime() / 1000 ) );
	}

	public boolean isExpired( boolean optional ) {
		return( this.isExpired() == optional );
	}

	@JsonIgnore
	public boolean isNotExpired() {
		return( this.isExpired( false ) );
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

	public PageModel setCounts( Integer counts ) {
		this.counts = counts;
		return( this );
	}

	public PageModel setOffset( Integer offsets ) {
		this.offsets = offsets;
		return( this );
	}

	public PageModel setSizes( Long sizes ) {
		this.sizes = sizes;
		return( this );
	}

	public PageModel setToken( String token ) {
		this.token = token;
		return( this );
	}

	public PageModel setValue( String values ) {
		this.values = values;
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
		return( this.setExpires( DateUtil.parse( expires ) ) );
	}

	public PageModel setExpires( LocalDateTime expires ) {
		return( this.setExpires( Timestamp.valueOf( expires ) ) );
	}

	public PageModel setExpires( Timestamp expires ) {
		this.expires = expires;
		return( this );
	}

}
