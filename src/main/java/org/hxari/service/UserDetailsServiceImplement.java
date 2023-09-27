package org.hxari.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.hxari.exception.ServiceException;
import org.hxari.exception.UserNotFoundException;
import org.hxari.model.RoleModel.Role;
import org.hxari.model.UserModel;
import org.hxari.payload.hit.ElasticHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Service
public class UserDetailsServiceImplement implements UserDetailsService {

	@Autowired
	private UserService userService;

	@Override
	public UserDetails loadUserByUsername( String username ) {
		try {
			return new UserDetailsImplement( this.userService.getByUsername( username ) );
		}
		catch( IOException e ) {
			throw new ServiceException( "Something wrong when getting user info" );
		}
		catch( UserNotFoundException e ) {
			throw new UsernameNotFoundException( String.format( "User %s not found", username ), e );
		}
	}

	public class UserDetailsImplement implements UserDetails {

		private String id;
		private String fullname;
		private String usermail;
		private String username;

		@JsonIgnore
		private String password;
		
		private Set<Role> roles;
		
		public UserDetailsImplement( ElasticHit<UserModel> hit ) {
			this.id = hit.id();
			this.roles = hit.source().getRoles();
			this.fullname = hit.source().getFullname();
			this.usermail = hit.source().getUsermail();
			this.username = hit.source().getUsername();
			this.password = hit.source().getPassword();
		}

		@Override
		public Collection<? extends GrantedAuthority> getAuthorities() {
        	Set<Role> roles = this.roles;
			List<SimpleGrantedAuthority> authorities = new ArrayList<>();
			for( Role role : roles ) {
				authorities.add( new SimpleGrantedAuthority( role.value() ) );
			}
			return( authorities );
		}

		public String getFullname() {
			return( this.fullname );
		}

		public String getId() {
			return( this.id );
		}

		@Override
		public String getPassword() {
			return( this.password );
		}

		public String getUsermail() {
			return( this.usermail );
		}

		@Override
		public String getUsername() {
			return( this.username );
		}

		@Override
		public boolean isAccountNonExpired() {
			return( true );
		}

		@Override
		public boolean isAccountNonLocked() {
			return( true );
		}

		@Override
		public boolean isCredentialsNonExpired() {
			return( true );
		}

		@Override
		public boolean isEnabled() {
			return( true );
		}
	}
	
}
