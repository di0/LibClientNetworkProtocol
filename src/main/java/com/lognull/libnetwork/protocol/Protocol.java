package com.lognull.libnetwork.protocol;

/**
 * Available protocols that can used by client.
 * @since 1.0
 */
public enum Protocol
{
	SSH( 22 ), TELNET( 23 );

	private final int port;

	Protocol( int value )
	{
		port = value;
	}

	public int getPort()
	{
		return port;
	}

	@Override
	public String toString()
	{
		return this.name();
	}
}
