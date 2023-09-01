package org.hxari.exception;

public class UserNotFoundException extends UserException
{
	
	/*
	 * Construct overloading method of class UserException.
	 * 
	 * @access Public Initialzie
	 * 
	 * @return Void
	 */
	public UserNotFoundException()
	{}

	/*
	 * Construct method of class UserException.
	 * 
	 * @access Public Initialzie
	 * 
	 * @return Void
	 */
	public UserNotFoundException( String message )
	{
		super( message );
	}

}
