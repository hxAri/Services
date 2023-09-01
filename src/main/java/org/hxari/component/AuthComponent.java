package org.hxari.component;

import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

import org.hxari.exception.AuthenticationException;
import org.hxari.exception.ForbidenRequestException;
import org.hxari.exception.ServiceException;
import org.hxari.model.Token;
import org.hxari.model.User;
import org.hxari.repository.TokenRepository;
import org.hxari.repository.UserRepository;
import org.hxari.util.Hex;
import org.hxari.util.Security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthComponent
{

	@Autowired
	private TokenRepository token;

	@Autowired
	private UserRepository user;

	/*
	 * Return if path is matchable for filter.
	 * 
	 * @access Private
	 * 
	 * @params String path
	 * 
	 * @return Boolean
	 */
	private boolean allow( String path )
	{
		return( Pattern.compile( "^\\/api\\/v1\\/(?:(auth\\/(?:logout|signin|signup))|(?:user\\/(?:[0-9]+)(?:\\/(?:tasks(?:\\/[0-9]+(?:\\/(?:collaborators|comments|orders)(?:\\/[0-9]+)*)*)*)*)*))$" ).matcher( path ).matches() );
	}

	/*
	 * @inherit org.hxari.service.AuthService.authenticate
	 * 
	 */
	public void authenticate( Token token )
	{
		this.token.save( token );
	}

	/*
	 * Authenticate user.
	 * 
	 * @access Public
	 * 
	 * @params User user
	 * @params HttpServletResponse response
	 * 
	 * @return Void
	 */
	public void authenticate( User user, HttpServletResponse response ) throws Exception
	{
		// Generating new authentication token.
		Token token = new Token(
			UUID.randomUUID().toString(),
			Hex.encode( Security.salt() ),
			user
		);

		// Authenticate user.
		this.token.save( token );

		// Create new Cookie instance.
		Cookie cookie = new Cookie( "csrftoken", token.toString() );
		cookie.setHttpOnly( true );
		cookie.setSecure( true );
		cookie.setValue( token.toString() );
		cookie.setMaxAge( 30 * 24 * 60 * 60 ); // 30d
		cookie.setPath( "/" );

		// Sent cookie to request response.
		response.addCookie( cookie );
	}

	/*
	 * Re-authenticate request.
	 * 
	 * @access Public
	 * 
	 * @params HttpServletRequest request
	 * @params HttpServletResponse response
	 * 
	 * @return Void
	 */
	public void authenticate( HttpServletRequest request, HttpServletResponse response )
	{
		// Check if cookie is available.
		if( request.getCookies() == null ) return;

		// Mapping cookies.
		for( Cookie cookie : request.getCookies() )
		{
			response.addCookie( cookie );
		}
	}

	/*
	 * Overloading method of method authenticated.
	 * 
	 * @access Public
	 * 
	 * @params HttpServletRequest request
	 * 
	 * @return Boolean
	 */
	public boolean authenticated( HttpServletRequest request, boolean optional )
	{
		return( this.authenticated( request ) == optional );
	}

	/*
	 * Return if current request is authenticated.
	 * 
	 * @access Public
	 * 
	 * @params String token
	 * 
	 * @return Boolean
	 */
	public boolean authenticated( HttpServletRequest request )
	{
		// Check if cookie is available.
		if( request.getCookies() == null ) return( false );

		// Mapping cookies.
		for( Cookie cookie : request.getCookies() )
		{
			// If csrftoken is available.
			if( cookie.getName().equalsIgnoreCase( "csrftoken" ) )
			{
				// Split csrftoken as part.
				String[] parts = cookie.getValue().split( ":" );

				// Because the token is included uuid, user id, and token.
				// we will check length parts of splited token is valid.
				if( parts.length == 3 )
				{
					Optional<User> userById = this.user.findById( Long.parseLong( parts[0] ) );
					Optional<Token> tokenByUuid = this.token.findByUuids( UUID.fromString( parts[1] ).toString() );
					Optional<Token> tokenByToken = this.token.findByToken( parts[2] );

					if( userById.isPresent() &&
						tokenByUuid.isPresent() &&
						tokenByToken.isPresent() )
					{
						return(
							userById.get().getId() == tokenByUuid.get().getUser().getId() &&
							userById.get().getId() == tokenByToken.get().getUser().getId() &&
							tokenByUuid.get().getId() == tokenByToken.get().getId()
						);
					}
				}
			}
		}
		return( false );
	}

	/*
	 * Filtering incoming request.
	 * 
	 * @access Public
	 * 
	 * @params HttpServletRequest request
	 * 
	 * @return Void
	 * 
	 * @throws AuthenticationException
	 *  When the path is only allow authenticated request.
	 * @throws ServiceException
	 *  When the path is only allow unauthorizated request.
	 */
	public void filter( HttpServletRequest request )
	{
		String path = request.getServletPath();

		// Check if path is matchable.
		if( this.allow( path ) )
		{
			// If request is authenticated and current path does not allowed authenticated request.
			if( this.authenticated( request ) && this.onlyUnauthorization( path ) )
			{
				throw new ServiceException( "Service unavailable" );
			}

			// If request is unauthorizated and current path must be authenticated.
			else if( this.authenticated( request, false ) && this.onlyAuthorization( path ) )
			{
				throw new AuthenticationException( "Login authentication required" );
			}
			System.out.println( "Matched: \033[1;32m" + path + "\033[0m" );
		}
	}

	private boolean onlyAuthorization( String path )
	{
		return( Pattern.compile( "^\\/api\\/v1\\/(?:(?:auth\\/logout)|(?:user\\/(?:[0-9]+)(?:\\/(?:tasks(?:\\/[0-9]+(?:\\/(?:collaborators|comments|orders)(?:\\/[0-9]+)*)*)*)*)*))$" ).matcher( path ).matches() );
	}

	/*
	 * Return if path is only allow when the request doesn't authenticated.
	 * 
	 * @access Private
	 * 
	 * @params String path
	 * 
	 * @return Boolean
	 */
	private boolean onlyUnauthorization( String path )
	{
		return( Pattern.compile( "^\\/api\\/v1\\/(?:auth)\\/(?:sigin|signup)\\/{0,1}$" ).matcher( path ).matches() );
	}

	/*
	 * Unauthorizate request.
	 * 
	 * @access Public
	 * 
	 * @params HttpServletRequest request
	 * 
	 * @return Void
	 */
	public void unauthorizate( HttpServletRequest request )
	{
		// Check if cookie is available.
		if( request.getCookies() == null ) return;

		// Mapping cookies.
		for( Cookie cookie : request.getCookies() )
		{
			// If csrftoken is available.
			if( cookie.getName().equalsIgnoreCase( "csrftoken" ) )
			{
				// Split csrftoken as part.
				String[] parts = cookie.getValue().split( ":" );

				// Because the token is included uuid, user id, and token.
				// we will check length parts of splited token is valid.
				if( parts.length == 3 )
				{
					// Trying to delete authentication token.
					this.token.delete( new Token( UUID.fromString( parts[1] ).toString(), parts[2], this.user.findById( Long.parseLong( parts[0] ) ).get() ) );
					return;
				}
			}
		}
	}

	/*
	 * Validate csrftoken with user id.
	 * 
	 * @access Public
	 * 
	 * @params String csrftoken
	 * @params Long id
	 * 
	 * @return Void
	 * 
	 * @throws ForbidenRequestException
	 *  When the user id is not equals with id in csrftoken.
	 */
	public void validate( String csrftoken, Long id )
	{
		// Split csrftoken as part.
		String[] parts = csrftoken.split( ":" );

		// Because the token is included uuid, user id, and token.
		// we will check length parts of splited token is valid.
		if( parts.length != 3 ||
			parts.length == 3 && Long.parseLong( parts[0] ) != id )
		{
			throw new ForbidenRequestException( "Forbidden resources" );
		}
	}

}
