package org.hxari.request;

public record TaskRequest( 
	String next, 
	String created, 
	// int owner, 
	// String title,  
	String updated 
)
{
	public TaskRequest withCreated( String created )
	{
		return( new TaskRequest( this.next, created, this.updated ) );
	}

	public TaskRequest withNext( String next )
	{
		return( new TaskRequest( next, this.created, this.updated ) );
	}

	public TaskRequest withUpdated( String update )
	{
		return( new TaskRequest( this.next, this.created, updated ) );
	}

}
