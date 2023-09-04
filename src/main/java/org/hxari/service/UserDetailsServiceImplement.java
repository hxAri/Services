package org.hxari.service;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import org.hxari.model.UserModel;
import org.hxari.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Service
public class UserDetailsServiceImplement implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername( String username ) throws UsernameNotFoundException {
		Optional<UserModel> opt = this.userRepository.findByUsername( username );
		if( opt.isPresent() ) {
			return( this.build( opt.get() ) );
		}
		throw new UsernameNotFoundException( username );
	}

	private UserDetailsImplement build( UserModel user ) {
		return( new UserDetailsImplement(
			user.getId(),
			user.getFullname(),
			user.getUsermail(),
			user.getUsername(),
			user.getPassword(),
			user.getRoles()
				.stream()
				.map( role -> new SimpleGrantedAuthority( role.getRoleName() ) )
				.collect( Collectors.toList() )
		));
	}

	public class UserDetailsImplement implements UserDetails {

		private static final long serialVersionUID = 1L;

		private Long id;
		private String fullname;
		private String usermail;
		private String username;

		@JsonIgnore
		private String password;
		
		private Collection<? extends GrantedAuthority> authorities;

		public UserDetailsImplement() {
		}

		public UserDetailsImplement( Long id, String fullname, String usermail, String username, String password, Collection<? extends GrantedAuthority> authorities ) {
			this.id = id;
			this.fullname = fullname;
			this.usermail = usermail;
			this.username = username;
			this.password = password;
			this.authorities = authorities;
		}

		@Override
		public Collection<? extends GrantedAuthority> getAuthorities() {
			return( this.authorities );
		}

		public Long getId() {
			return( this.id );
		}

		public String getFullname() {
			return( this.fullname );
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
