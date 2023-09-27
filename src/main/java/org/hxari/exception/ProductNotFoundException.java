package org.hxari.exception;

public class ProductNotFoundException extends ClientException
{
	
	/*
	 * Construct overloading method of class TaskNotFoundException.
	 * 
	 * @access Public Initialzie
	 * 
	 * @return Void
	 */
	public ProductNotFoundException()
	{}

	/*
	 * Construct method of class TaskNotFoundException.
	 * 
	 * @access Public Initialzie
	 * 
	 * @return Void
	 */
	public ProductNotFoundException( String message )
	{
		super( message );
	}

}
