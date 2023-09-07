package org.hxari.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.hxari.model.RoleModel;
import org.hxari.model.UserModel;
import org.hxari.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Service
public class UserDetailsServiceImplement implements UserDetailsService {

	@Autowired
	private PasswordEncoder paswEncoder;

	@Autowired
	private UserRepository userRepository;

	public class UserDetailsImplement implements UserDetails {

		private Long id;
		private String fullname;
		private String usermail;
		private String username;
		@JsonIgnore
		private String password;
		private Set<RoleModel> roles;
		
		public UserDetailsImplement( UserModel user ) {
			this.id = user.getId();
			this.roles = user.getRoles();
			this.fullname = user.getFullname();
			this.usermail = user.getUsermail();
			this.username = user.getUsername();
			this.password = user.getPassword();
		}

		@Override
		public Collection<? extends GrantedAuthority> getAuthorities() {
        	Set<RoleModel> roles = this.roles;
			List<SimpleGrantedAuthority> authorities = new ArrayList<>();
			for( RoleModel role : roles ) {
				authorities.add( new SimpleGrantedAuthority( role.getRole() ) );
			}
			return( authorities );
		}

		public String getFullname() {
			return( this.fullname );
		}

		public Long getId() {
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
	
	@Override
	public UserDetails loadUserByUsername( String username ) throws UsernameNotFoundException {
		Optional<UserModel> user = this.userRepository.findByUsername( username );
		if( user.isPresent() ) {
			return( new UserDetailsImplement( user.get() ) );
		}
		throw new UsernameNotFoundException( String.format( "User %s not found", username ) );
	}

	public void save( UserModel user ) {
        user.setPassword( this.paswEncoder.encode( user.getPassword() ) );
        this.userRepository.save( user );
    }
	
}
