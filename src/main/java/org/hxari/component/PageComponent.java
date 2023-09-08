package org.hxari.component;

import java.util.NoSuchElementException;

import org.hxari.exception.ClientException;
import org.hxari.model.PageModel;
import org.hxari.repository.PageRepository;
import org.hxari.util.DateParserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PageComponent
{

	@Value( "${service.fetch.next.page-expires}" )
	private String EXPIRES;

	@Autowired
	private PageRepository pageRepository;

	/*
	 * Find page.
	 * 
	 * @access Public
	 * 
	 * @params String secret
	 * 
	 * @return PageModel
	 */
	public PageModel find( String token ) throws Exception {
		try {
			return( this.pageRepository.findByToken( token ).get() );
		}
		catch( NoSuchElementException e ) {
			throw new ClientException( "Invalid next page request" );
		}
	}

	/*
	 * Delete page.
	 * 
	 * @access Public
	 * 
	 * @params PageModel page
	 * 
	 * @return Void
	 */
	public void delete( PageModel page ) {
		this.pageRepository.delete( page );
	}

	/*
	 * Save page.
	 * 
	 * @access Public
	 * 
	 * @params PageModel page
	 * 
	 * @return Void
	 */
	public void save( PageModel page ) throws Exception {
		if( page.getExpires() == null ) {
			page.setExpires( DateParserUtil.localDateTime( this.EXPIRES ) );
		}
		this.pageRepository.save( page );
	}

}
