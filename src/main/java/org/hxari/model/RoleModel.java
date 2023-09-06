package org.hxari.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table( name="roles" )
public class RoleModel {

	@Id
	@GeneratedValue( strategy=GenerationType.IDENTITY )
	private Long id;

	@Enumerated( EnumType.STRING )
	@Column( length=20 )
	private Role role;

	public RoleModel() {
	}

	public RoleModel( Role role ) {
		this.setRole( role );
	}

	public Long getId() {
		return( id );
	}

	public Role getRole() {
		return( this.role );
	}

	public String getRoleName() {
		return( this.role.name() );
	}

	public RoleModel setRole( Role role ) {
		this.role = role;
		return( this );
	}

	public enum Role {
		ADMIN,
		MODER,
		USER
	}
}
