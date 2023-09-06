package org.hxari.service;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import org.hxari.model.UserModel;
import org.hxari.model.UserModel.Role;
import org.hxari.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImplement implements UserDetailsService {

	@Autowired
	private PasswordEncoder paswEncoder;

	@Autowired
	private UserRepository userRepository;

	public class UserDetailsImplement implements UserDetails {

		private Long id;
		private String username;
		private String password;
		private Role role;
		
		public UserDetailsImplement( UserModel user ) {
			this.id = user.getId();
			this.role = user.getRole();
			this.username = user.getUsername();
			this.password = user.getPassword();
		}

		@Override
		public Collection<? extends GrantedAuthority> getAuthorities() {
			SimpleGrantedAuthority authority = new SimpleGrantedAuthority( this.role.name() );
        	return( Collections.singletonList( authority ) );
		}

		public Long getId() {
			return( this.id );
		}

		@Override
		public String getPassword() {
			return( this.password );
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
