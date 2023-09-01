package org.hxari.exception;

public class ForbidenRequestException extends ClientException
{
	
	/*
	 * Construct overloading method of class ForbidenRequestException.
	 * 
	 * @access Public Initialzie
	 * 
	 * @return Void
	 */
	public ForbidenRequestException()
	{}

	/*
	 * Construct method of class ForbidenRequestException.
	 * 
	 * @access Public Initialzie
	 * 
	 * @return Void
	 */
	public ForbidenRequestException( String message )
	{
		super( message );
	}

}

