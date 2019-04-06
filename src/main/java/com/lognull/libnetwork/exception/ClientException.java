package com.lognull.libnetwork.exception;

/**
 * The class {@code Exception} and its subclasses are a form of
 * {@code Throwable} that indicates conditions that a reasonable
 * application might want to catch.
 *
 * @author Diogo Pinto <dio@lognull.com>
 * @since 1.0
 */
public class ClientException extends Exception
{
	private static final long serialVersionUID = -4653451676732079647L;

	public
	ClientException()
	{
		super();
	}

	public
	ClientException( String message )
	{
		super( message );
	}

	public
	ClientException ( String message, Throwable throwable )
	{
		super( message, throwable );
	}
}
