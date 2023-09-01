package org.hxari.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

public class DateParser
{

	/*
	 * Parse datetime format.
	 * 
	 * @access Public Static
	 * 
	 * @params String format
	 * 
	 * @return LocalDateTime
	 * 
	 * @throws IllegalArgumentexception
	 *  When the datetime format is invalid or
	 *  Unsupported unit time.
	 */
	public static LocalDateTime localDateTime( String format )
	{
		// Removing spaces in first and end of text.
		format = format.trim();

		// Try parsing as a valid LocalDateTime
		try
		{
            return( LocalDateTime.parse( format ) );
        }
		catch( DateTimeParseException e )
		{
			// Trying to split per part format.
            String[] parts = format.split( " " );

			// When the date time format is valid.
            if( parts.length == 2 )
			{
                int amount = Integer.parseInt( parts[0]
					.replace( "+", "" )
					.replace( "-", "" )
				);
				String unit = parts[1].toLowerCase();
				LocalDateTime now = LocalDateTime.now();
				ChronoUnit chrono = getChronoUnit( unit );

				// If the amount is started with minus symbol.
				if( parts[0].startsWith( "-" ) )
				{
					return( now.minus( amount, chrono ) );
				}
				return( now.plus( amount, chrono ) );
            }
            throw new IllegalArgumentException( "Invalid date format: " + format );
		}
	}

	/*
	 * Return crono unit.
	 * 
	 * @access Private Static
	 * 
	 * @params String
	 *  Crono unit name.
	 * 
	 * @return ChronoUnit
	 */
	private static ChronoUnit getChronoUnit( String unit )
	{
        switch( unit )
		{
            case "years":
            case "year":
                return( ChronoUnit.YEARS );
            case "months":
            case "month":
                return( ChronoUnit.MONTHS );
            case "weeks":
            case "week":
                return( ChronoUnit.WEEKS );
            case "days":
            case "day":
                return( ChronoUnit.DAYS );
            case "hours":
            case "hour":
                return( ChronoUnit.HOURS );
            case "minutes":
            case "minute":
                return( ChronoUnit.MINUTES );
            case "seconds":
            case "second":
                return( ChronoUnit.SECONDS );
        }
		throw new IllegalArgumentException( "Unsupported time unit: " + unit );
	}

}
