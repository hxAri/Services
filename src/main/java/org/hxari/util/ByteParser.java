package org.hxari.util;

public class ByteParser
{
	/*
	 * Parse byte array to char array.
	 * 
	 * @access Public Static
	 * 
	 * @params byte[] bytes
	 * 
	 * @return char[]
	 */
	public static char[] bytesToChars( byte[] bytes )
	{
		char[] buffer = new char[( bytes.length >> 1 )];
		for( int i=0; i<buffer.length; i++ )
		{
			int bpos = i << 1;
			char c = ( char ) ( ( ( bytes[bpos] & 0x00FF ) << 8 ) + ( bytes[bpos + 1] & 0x00FF ) );
			buffer[i] = c;
		}
		return( buffer );
	}
}
