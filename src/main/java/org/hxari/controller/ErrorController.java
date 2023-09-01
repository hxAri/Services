package org.hxari.controller;

import org.hxari.response.BodyResponse;
import org.hxari.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ErrorController
{

	/*
	 * Response Entity builder for error response.
	 * 
	 * @access Private
	 * 
	 * @params Throwable e
	 * @params HttpStatus status
	 * 
	 * @return ResponseEntity<BodyResponse<ErrorResponse>>
	 */
	private ResponseEntity<BodyResponse<ErrorResponse>> builder( Throwable e, HttpStatus status )
	{
		return( new ResponseEntity<>( 
			new BodyResponse<>( 
				e.getMessage(), "failed", status.value(),
				new ErrorResponse(
					e.getClass().getName()
				)
			), 
			status
		));
	}

	@ResponseBody
	@ExceptionHandler( Throwable.class )
	public ResponseEntity<BodyResponse<ErrorResponse>> common( Throwable e )
	{
		HttpStatus status = null;
		switch( e.getClass().getSimpleName() )
		{
			case "AuthenticationException":
				status = HttpStatus.UNAUTHORIZED;
				break;
			case "ForbidenRequestException":
				status = HttpStatus.FORBIDDEN;
				break;
			case "HttpMediaTypeNotSupportedException":
				status = HttpStatus.UNSUPPORTED_MEDIA_TYPE;
				break;
			case "HttpMessageNotReadableException":
			case "MissingServletRequestParameterException":
			case "UserException":
				status = HttpStatus.BAD_REQUEST;
				break;
			case "HttpRequestMethodNotSupportedException":
				status = HttpStatus.METHOD_NOT_ALLOWED;
				break;
			case "NoResourceFoundException":
			case "NotFoundException":
			case "TaskNotFoundException":
			case "UserNotFoundException":
				status = HttpStatus.NOT_FOUND;
				break;
			case "ServiceException":
				status = HttpStatus.SERVICE_UNAVAILABLE;
				break;
			default:
				status = HttpStatus.INTERNAL_SERVER_ERROR;
				break;
		}
		return( this.builder( e, status ) );
	}

}
