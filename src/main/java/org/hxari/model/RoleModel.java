package org.hxari.model;

import org.hxari.model.UserModel.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table( name="roles" )
public class RoleModel {

    @Id
    @Column( name="id" )
    @GeneratedValue( strategy=GenerationType.IDENTITY )
    private Long id;
    
	@Column( length=24, name="role" )
    private String role;
	
	public RoleModel() {
	}

	public RoleModel( Role role ) {
		this.setRol( role );
	}

	public RoleModel( String role ) {
		this.setRol( role );
	}

    public Long getId() {
        return( this.id );
    }

	public String getRole() {
		return( this.role );
	}

	public RoleModel setRol( Role role ) {
		return( this.setRol( role.value() ) );
	}

	public RoleModel setRol( String role ) {
		this.role = role;
		return( this );
	}
}

