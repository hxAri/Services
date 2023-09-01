package org.hxari.component;

import java.util.Base64;
import java.util.Optional;

import javax.crypto.spec.IvParameterSpec;

import org.hxari.exception.ClientException;
import org.hxari.model.Page;
import org.hxari.repository.PageRepository;
import org.hxari.util.ByteParser;
import org.hxari.util.Hex;
import org.hxari.util.Security.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PageComponent
{

	@Autowired
	private PageRepository page;

	/*
	 * Find page.
	 * 
	 * @access Public
	 * 
	 * @params String secret
	 * 
	 * @return Page
	 */
	public Page find( String token ) throws Exception
	{
		// Trying to get page by padding.
		Optional<Page> result = this.page.findByToken( token );
		Page page;

		// Check if padding is available.
		if( result.isPresent() )
		{
			// Get page model.
			page = result.get();

			// Split secret key.
			String[] parts = page.getSecret().split( ":" );

			// Decode encrypted padding.
			page.setPadding(
				Message.decrypt(
					"AES/CBC/PKCS5Padding",
					page.getPadding(),
					Message.ksec(
						ByteParser.bytesToChars( Hex.decode( parts[0] ) ),
						Base64.getDecoder().decode( parts[1] )
					),
					new IvParameterSpec(
						Base64.getDecoder().decode(
							page.getIvp()
						)
					)
				)
			);
			return( page );
		}
		throw new ClientException( "Invalid request padding page" );
	}

	/*
	 * Delete page.
	 * 
	 * @access Public
	 * 
	 * @params Page page
	 * 
	 * @return Void
	 */
	public void delete( Page page )
	{
		this.page.delete( page );
	}

	/*
	 * Save page.
	 * 
	 * @access Public
	 * 
	 * @params Page page
	 * 
	 * @return Void
	 */
	public void save( Page page ) throws Exception
	{
		if( page.getPadding().length() <= 40 )
		{
			System.out.print( "Plaintext: " );
			System.out.print( "\033[1;34m" );
			System.out.print( page.getPadding().length() );
			System.out.print( "\033[1;37m: " );
			System.out.print( "\033[1;32m" );
			System.out.print( page.getPadding() );
			System.out.println( "\033[0m" );
			String[] parts = page.getSecret().split( ":" );
			page.setPadding(
				Message.encrypt(
					"AES/CBC/PKCS5Padding",
					page.getPadding(),
					Message.ksec(
						ByteParser.bytesToChars( Hex.decode( parts[0] ) ),
						Base64.getDecoder().decode( parts[1] )
					),
					new IvParameterSpec(
						Base64.getDecoder().decode(
							page.getIvp()
						)
					)
				)
			);
		}
		System.out.print( "Chipertext: " );
		System.out.print( "\033[1;34m" );
		System.out.print( page.getPadding().length() );
		System.out.print( "\033[1;37m: " );
		System.out.print( "\033[1;32m" );
		System.out.print( page.getPadding() );
		System.out.println( "\033[0m" );
		this.page.save( page );
	}

}
