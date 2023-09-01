package org.hxari.exception;

public class TaskNotFoundException extends ClientException
{
	
	/*
	 * Construct overloading method of class TaskNotFoundException.
	 * 
	 * @access Public Initialzie
	 * 
	 * @return Void
	 */
	public TaskNotFoundException()
	{}

	/*
	 * Construct method of class TaskNotFoundException.
	 * 
	 * @access Public Initialzie
	 * 
	 * @return Void
	 */
	public TaskNotFoundException( String message )
	{
		super( message );
	}

}
