package org.hxari.exception;

public class UserException extends ClientException
{
	/*
	 * Construct overloading method of class UserException.
	 * 
	 * @access Public Initialzie
	 * 
	 * @return Void
	 */
	public UserException()
	{}

	/*
	 * Construct method of class UserException.
	 * 
	 * @access Public Initialzie
	 * 
	 * @return Void
	 */
	public UserException( String message )
	{
		super( message );
	}

}
