package org.hxari.payload.request;

public record TaskFetchParameterRequest(
	Integer count, 
	String filter,  
	Integer offset, 
	String next,
	String value
) {

	public TaskFetchParameterRequest withCount( Integer count ) {
		return( new TaskFetchParameterRequest( count, this.filter, this.offset, this.next, this.value ) );
	}

	public TaskFetchParameterRequest withFilter( String filter ) {
		return( new TaskFetchParameterRequest( this.count, filter, this.offset, this.next, this.value ) );
	}

	public TaskFetchParameterRequest withOffset( Integer offset ) {
		return( new TaskFetchParameterRequest( this.count, this.filter, offset, this.next, this.value ) );
	}

	public TaskFetchParameterRequest withNext( String next ) {
		return( new TaskFetchParameterRequest( this.count, this.filter, this.offset, next, this.value ) );
	}

	public TaskFetchParameterRequest withValue( String value ) {
		return( new TaskFetchParameterRequest( this.count, this.filter, this.offset, this.next, value ) );
	}
}
