package org.hxari.util;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class Security
{

	/*
	 * Default hash algorithm.
	 * 
	 * @access Private Static
	 * 
	 * @values String
	 */
	private static String DEFAULT_HASH_ALGO = "PBKDF2WithHmacSHA512";

	/*
	 * Minimum iteration length for hash.
	 * 
	 * @access Private Static
	 * 
	 * @values Int
	 */
	private static int DEFAULT_HASH_ITER = 10000;

	/*
	 * Default length of hash.
	 * 
	 * @access Private Static
	 * 
	 * @values Int
	 */
	private static int DEFAULT_HASH_LENGTH = 258;

	/*
	 * Default salt length.
	 * 
	 * @access Private Static
	 * 
	 * @values Int
	 */
	private static int DEFAULT_SALT_LENGTH = 128;

	/*
	 * Overloading method of method hash.
	 * 
	 * @access Public Static
	 * 
	 * @params String password
	 * 
	 * @return String
	 */
	public static String hash( String password ) throws InvalidKeySpecException, NoSuchAlgorithmException, NoSuchProviderException
	{
		return( Security.hash( password, Security.salt() ) );
	}

	/*
	 * Overloading method of method hash.
	 * 
	 * @access Public Static
	 * 
	 * @params String password
	 * @params Int length
	 * 
	 * @return String
	 */
	public static String hash( String password, int length ) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException
	{
		return( Security.hash( password.toCharArray(), Security.salt( length ) ) );
	}

	/*
	 * Overloading method of method hash.
	 * 
	 * @access Public Static
	 * 
	 * @params String password
	 * @params byte[] salt
	 * 
	 * @return String
	 */
	public static String hash( String password, byte[] salt ) throws InvalidKeySpecException, NoSuchAlgorithmException
	{
		return( Security.hash( password.toCharArray(), salt ) );
	}

	/*
	 * Overloading method of method hash.
	 * 
	 * @access Public Static
	 * 
	 * @params String password
	 * @params String salt
	 * 
	 * @return String
	 */
	public static String hash( String password, String salt ) throws InvalidKeySpecException, NoSuchAlgorithmException
	{
		return( Security.hash( password.toCharArray(), salt.getBytes() ) );
	}

	/*
	 * Generate password hash.
	 * 
	 * @access Public Static
	 * 
	 * @params char[] password
	 * @params byte[] salt
	 * 
	 * @return String
	 */
	public static String hash( char[] password, byte[] salt ) throws InvalidKeySpecException, NoSuchAlgorithmException
	{
		return( Security.hash( SecretKeyFactory
			.getInstance( Security.DEFAULT_HASH_ALGO )
			.generateSecret( 
				new PBEKeySpec( 
					password, 
					salt, 
					Security.DEFAULT_HASH_ITER, 
					Security.DEFAULT_HASH_LENGTH 
				) 
			)
			.getEncoded(), 
			salt 
		));
	}

	/*
	 * Normalize hash result.
	 * 
	 * @access Private Static
	 * 
	 * @params byte[] hash
	 * @params byte[] salt
	 * 
	 * @return String
	 */
	private static String hash( byte[] hash, byte[] salt )
	{
		return( String.format( "%d:%s:%s", 
			Security.DEFAULT_HASH_ITER, 
			Hex.encode( salt ), 
			Hex.encode( hash )
		));
	}

	/*
	 * Overloading method of method salt.
	 * 
	 * @access Public Static
	 * 
	 * @return byte[]
	 */
	public static byte[] salt() throws NoSuchAlgorithmException, NoSuchProviderException
	{
		return( Security.salt( Security.DEFAULT_SALT_LENGTH ) );
	}

	/*
	 * Generate random pseudo bytes, salt.
	 * 
	 * @access Public Static
	 * 
	 * @params Int length
	 *  The length of bytes must be event number.
	 * 
	 * @return byte[]
	 * 
	 * @throws IllegalArgumentException
	 *  When the length of bytes is odd.
	 */
	public static byte[] salt( int length ) throws NoSuchAlgorithmException, NoSuchProviderException
	{
		if( length % 2 == 0 )
		{
			// Get secure random instance providered of sun.
			SecureRandom sr = SecureRandom.getInstance( "SHA1PRNG", "SUN" );

			// Create byte array for salt.
			byte[] salt = new byte[length];

			// Gets random salt.
			sr.nextBytes( salt );

			return( salt );
		}
		throw new IllegalArgumentException( "Length of salt must be even number" );
	}

	/*
	 * Overloading method of method valid.
	 * 
	 * @access Public Static
	 * 
	 * @params String raw
	 * @params Boolean optional
	 * 
	 * @return Boolean
	 */
	public static boolean valid( String raw, boolean optional )
	{
		return( Security.valid( raw ) == optional );
	}

	/*
	 * Return if string is hashed password.
	 * 
	 * @access Public Static
	 * 
	 * @params String hash
	 * 
	 * @return Boolean
	 */
	public static boolean valid( String raw )
	{
		try
		{
			// Split per part hashed password.
			String[] parts = raw.split( ":" );

			// Return if iteration is equals or greater than default iteration.
			return( parts.length == 3 ? ( Integer.parseInt( parts[0] ) >= Security.DEFAULT_HASH_ITER ) : false );
		}
		catch( Throwable e )
		{
			return( false );
		}
	}

	/*
	 * Validate hash password.
	 * 
	 * @access Public Static
	 * 
	 * @params String hashed
	 *  Hashed passsword.
	 * @params String password
	 *  Plaintext password.
	 * 
	 * @return Boolean
	 */
	public static boolean verify( String hashed, String password ) throws Exception
	{
		// Split per part hashed password.
		String[] parts = hashed.split( ":" );

		// Value of part is must be 3 only.
		if( parts.length == 3 )
		{
			// Normalize iteration length.
			int iterations = Integer.parseInt( parts[0] );

			// Normalize salt and hash from hexadecimal.
			byte[] salt = Hex.decode( parts[1] );
			byte[] hash = Hex.decode( parts[2] );

			// Hashing password.
			byte[] pasw = SecretKeyFactory
				.getInstance( Security.DEFAULT_HASH_ALGO )
				.generateSecret( new PBEKeySpec( password.toCharArray(), salt, iterations, hash.length * 8 ) )
				.getEncoded();

			// When the hashed passwords have the same length
			int diff = hash.length ^ pasw.length;

			// Perform iterations along the hash value or password to be verified.
			for( int i = 0; i < hash.length && i < pasw.length; i++ ) diff |= hash[i] ^ pasw[i];
			
			// Return comparation of difference with zero.
			return( diff == 0 );
		}
		return( false );
	}

	public class Message
	{

		/*
		 * Decrypt chiphertext.
		 * 
		 * @access Public Static
		 * 
		 * @params String algorithm
		 * @params String ciphertext
		 * @params SecretKey key
		 * @params IvParameterSpec iv
		 * 
		 * @return String
		 */
		public static String decrypt( String algorithm, String ciphertext, SecretKey key, IvParameterSpec iv ) throws Exception
		{
			Cipher cipher = Cipher.getInstance( algorithm );
			cipher.init( Cipher.DECRYPT_MODE, key, iv );
			return( new String(
				cipher.doFinal( Base64.getDecoder()
					.decode( ciphertext )
				)
			));
		}

		/*
		 * Encrypt plaintext.
		 * 
		 * @access Public Static
		 * 
		 * @params String algorithm
		 * @params String plaintext
		 * @params SecretKey key
		 * @params IvParameterSpec iv
		 * 
		 * @return String
		 */
		public static String encrypt( String algorithm, String plaintext, SecretKey key, IvParameterSpec iv ) throws Exception
		{
			Cipher cipher = Cipher.getInstance( algorithm );
			cipher.init( Cipher.ENCRYPT_MODE, key, iv );
			return( Base64.getEncoder()
				.encodeToString(
					cipher.doFinal(
						plaintext.getBytes()
					)
				)
			);
		}

		/*
		* Generate key from given password.
		* 
		* @access Public Static
		* 
		* @params char[] password
		* @params byte[] salt
		* 
		* @return SecretKeySpec
		*/
		public static SecretKeySpec ksec( char[] password, byte[] salt ) throws Exception
		{
			return( new SecretKeySpec( SecretKeyFactory
				.getInstance( Security.DEFAULT_HASH_ALGO )
				.generateSecret(
					new PBEKeySpec( 
						password, 
						salt, 
						65536, 
						256 
					)
				)
				.getEncoded(),
				"AES"
			));
		}

		/*
		 * Generate IV ParameterSpec.
		 * 
		 * @access Public Static
		 * 
		 * @return IvParameterSpec
		 */
		public static IvParameterSpec ivp()
		{
			byte[] iv = new byte[16];
			new SecureRandom().nextBytes( iv );
			return( new IvParameterSpec( iv ) );
		}
	}

}
