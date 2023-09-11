package org.hxari.service;

import java.util.NoSuchElementException;

import org.hxari.exception.UserNotFoundException;
import org.hxari.model.UserModel;
import org.hxari.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;

	public void delete( Long id ) {
		this.userRepository.deleteById( id );
	}

	public void delete( UserModel user ) {
		this.userRepository.delete( user );
	}

	public UserModel findById( Long id ) {
		try {
			return( this.userRepository.findById( id ).get() );
		}
		catch( NoSuchElementException e ) {
			throw new UserNotFoundException( "User not found" );
		}
	}

	public UserModel findByUsername( String username ) {
		try {
			return( this.userRepository.findByUsername( username ).get() );
		}
		catch( NoSuchElementException e ) {
			throw new UserNotFoundException( "User not found" );
		}
	}
}
