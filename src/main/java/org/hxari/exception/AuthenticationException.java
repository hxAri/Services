package org.hxari.exception;

public class AuthenticationException extends ClientException
{
	
	/*
	 * Construct overloading method of class AuthenticationException.
	 * 
	 * @access Public Initialzie
	 * 
	 * @return Void
	 */
	public AuthenticationException()
	{}

	/*
	 * Construct method of class ClientException.
	 * 
	 * @access Public Initialzie
	 * 
	 * @return Void
	 */
	public AuthenticationException( String message )
	{
		super( message );
	}

}
