package org.hxari.exception;

public class ClientException extends RuntimeException
{
	
	/*
	 * Construct overloading method of class ClientException.
	 * 
	 * @access Public Initialzie
	 * 
	 * @return Void
	 */
	public ClientException()
	{}

	/*
	 * Construct method of class ClientException.
	 * 
	 * @access Public Initialzie
	 * 
	 * @return Void
	 */
	public ClientException( String message )
	{
		super( message );
	}

}
