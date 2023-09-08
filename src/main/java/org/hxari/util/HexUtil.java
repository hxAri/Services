package org.hxari.util;

import java.io.ByteArrayOutputStream;
import java.io.CharArrayWriter;
import java.math.BigInteger;

public final class HexUtil
{

	private HexUtil()
	{}

	public static String encode( char[] chars ) throws Exception
	{
		// Buat objek ByteArrayOutputStream
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

		// Buat objek CharArrayWriter
		CharArrayWriter charArrayWriter = new CharArrayWriter();
	
		// Tulis char array ke CharArrayWriter
		charArrayWriter.write( chars );
	
		// Baca CharArrayWriter ke ByteArrayOutputStream
		byteArrayOutputStream.write( charArrayWriter.toString().getBytes() );
	
		// Teruskan byte array hasil konversi
		return( HexUtil.encode( byteArrayOutputStream.toByteArray() ) );
	}

	public static String encode( byte[] bytes )
	{
		String hex = new BigInteger( 1, bytes ).toString( 16 );	
		int length = bytes.length *2;
			length-= hex.length();
		
		return( length >= 1 ? String.format( "%0" + length + "d", 0 ) +hex : hex );
	}

	public static byte[] decode( String hex )
	{
		byte[] bytes = new byte[hex.length() / 2];
		for(int i = 0; i < bytes.length ;i++)
		{
			bytes[i] = (byte)Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
		}
		return bytes;
	}

}
