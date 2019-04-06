package com.lognull.libnetwork.consumer.spi;

import com.lognull.libnetwork.exception.ClientException;
import com.lognull.libnetwork.protocol.Telnet;

/**
 * Just a wrapper facade to Telnet Client.
 *
 * @author Diogo Pinto <dio@lognull.com>
 * @since 1.0
 * @see com.lognull.libnetwork.protocol.Telnet
 */
public class TelnetClient implements Client
{
	/** The real Telnet client */
	private Telnet telnet = null;

	/**
	 * Constructs inject of the real implementation.
	 *
	 * @param telnet
	 *            the real telnet client implementation
	 */
	public
	TelnetClient( Telnet telnet )
	{
		this.telnet = telnet;
	}

	/**
	 * @see Client#getPort()
	 */
	public int
	getPort()
	{
		return telnet.getPort();
	}

	/**
	 * @see Client#getIpFromEndpoint()
	 */
	public String
	getIpFromEndpoint()
	{
		return telnet.getIpFromEndpoint();
	}

	/**
	 * @see Client#connect()
	 */
	public void
	connect() throws ClientException
	{
		telnet.connect();
	}

	/**
	 * @see Client#disconnect()
	 */
	public void
	disconnect() throws ClientException
	{
		telnet.disconnect();
	}

	/**
	 * @see Client#send(String)
	 */
	public void
	send(String message) throws ClientException
	{
		telnet.send( message );
	}

	/**
	 * @see Client#receive()
	 */
	public String
	receive() throws ClientException
	{
		return telnet.receive();
	}

	/**
	 * @see Client#isConnect()
	 */
	public boolean
	isConnect()
	{
		return telnet.isConnect();
	}

	public String
	getUsername()
	{
		return telnet.getUsername();
	}

	public String
	getPassword()
	{
		return telnet.getPassword();
	}
}
