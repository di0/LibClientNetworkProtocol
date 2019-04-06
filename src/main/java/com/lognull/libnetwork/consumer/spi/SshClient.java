package com.lognull.libnetwork.consumer.spi;

import com.lognull.libnetwork.exception.ClientException;
import com.lognull.libnetwork.protocol.Ssh;

/**
 * Just a wrapper facade to SSH Client.
 *
 * @author Diogo Pinto <dio@lognull.com>
 * @since 1.0
 * @see com.lognull.libnetwork.protocol.Ssh
 */
public class SshClient implements Client
{
	/** The real SSH client */
	private Ssh ssh = null;

	/**
	 * Constructs inject of the real implementation.
	 *
	 * @param ssh
	 *            the real ssh client implementation
	 */
	public
	SshClient( final Ssh ssh )
	{
		this.ssh = ssh;
	}

	/**
	 * @see Client#getPort()
	 */
	public int
	getPort()
	{
		return ssh.getPort();
	}

	/**
	 *  @see Client#getIpFromEndpoint()
	 */
	public String
	getIpFromEndpoint()
	{
		return ssh.getIpFromEndpoint();
	}

	/**
	 * @see Client#connect()
	 */
	public void
	connect() throws ClientException
	{
		ssh.connect();
	}

	/**
	 * @see Client#close()
	 */
	public void
	close() throws ClientException
	{
		ssh.close();
	}

	/**
	 * @see Client#send(String)
	 */
	public void
	send(String message) throws ClientException
	{
		ssh.send( message );
	}

	/**
	 * @see Client#receive()
	 */
	public String
	receive() throws ClientException
	{
		return ssh.receive();
	}

	/**
	 * @see Client#isConnect()
	 */
	public boolean
	isConnect()
	{
		return ssh.isConnect();
	}

	public String
	getUsername()
	{
		return ssh.getUsername();
	}

	public String
	getPassword()
	{
		return ssh.getPassword();
	}
}
