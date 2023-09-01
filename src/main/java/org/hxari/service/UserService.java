package org.hxari.service;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.hxari.exception.UserException;
import org.hxari.exception.UserNotFoundException;
import org.hxari.model.User;
import org.hxari.repository.UserRepository;
import org.hxari.util.Security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService
{

	@Autowired
	private UserRepository repo;

	public User find( Long id )
	{
		try
		{
			return( this.repo.findById( id ).get() );
		}
		catch( NoSuchElementException e )
		{}
		throw new UserNotFoundException( "User not found" );
	}

	public User findByEmail( String usermail )
	{
		try
		{
			return( this.repo.findByUsermail( usermail ).get() );
		}
		catch( NoSuchElementException e )
		{}
		throw new UserNotFoundException( "User not found" );
	}

	public User findByUname( String username )
	{
		try
		{
			return( this.repo.findByUsername( username ).get() );
		}
		catch( NoSuchElementException e )
		{}
		throw new UserNotFoundException( "User not found" );
	}

	public void save( User user ) throws Throwable
	{
		if( user.getId() != null )
		{
			Optional<User> find = this.repo.findById( user.getId() );
			if( find.isPresent() )
			{
				if( find.get().getUsermail() != user.getUsermail() && this.repo.existsByUsermail( user.getUsermail() ) )
				{
					throw new UserException( "Email address is already exists" );
				}
				if( find.get().getUsername() != user.getUsername() && this.repo.existsByUsername( user.getUsername() ) )
				{
					throw new UserException( "Username is already exists" );
				}
			}
		}
		else {
			if( this.repo.existsByUsermail( user.getUsermail() ) )
			{
				throw new UserException( "Email address is already exists" );
			}
			if( this.repo.existsByUsername( user.getUsername() ) )
			{
				throw new UserException( "Username is already exists" );
			}
		}
		if( Security.valid( user.getPassword(), false ) )
		{
			user.setPassword(
				Security.hash(
					user.getPassword()
				)
			);
		}
		this.repo.save( user );
	}

	public void delete( Long id )
	{
		this.repo.deleteById( id );
	}
	
}
