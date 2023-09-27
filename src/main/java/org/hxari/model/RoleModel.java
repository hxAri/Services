package org.hxari.model;

public record RoleModel( String name ) {

	public enum Role {

		ROOT( "ROLE_ROOT" ),
		USER( "ROLE_USER" );

		private String value;

		private Role( String value ) {
			this.value = value;
		}

		public String getValue() {
			return( this.value );
		}

		public String value() {
			return( this.value );
		}
	}

	public RoleModel withName( String name ) {
		return new RoleModel( name );
	}
}
