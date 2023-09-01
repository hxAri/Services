package org.hxari.exception;

public class ServiceException extends RuntimeException
{

	/*
	 * Construct overloading method of class ServiceException.
	 * 
	 * @access Public Initialzie
	 * 
	 * @return Void
	 */
	public ServiceException()
	{}

	/*
	 * Construct method of class ServiceException.
	 * 
	 * @access Public Initialzie
	 * 
	 * @return Void
	 */
	public ServiceException( String message )
	{
		super( message );
	}

}
