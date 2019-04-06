package com.lognull.libnetwork.protocol;

import java.io.IOException;
import java.io.InputStream;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;
import com.lognull.libnetwork.consumer.spi.Client;
import com.lognull.libnetwork.exception.ClientException;
import com.lognull.libnetwork.helper.Error;
import com.lognull.libnetwork.helper.LoggerHelper;
import com.lognull.libnetwork.producer.EndpointInfo;

/**
 * This class encapsules and provide the communication between client
 * and server through the SSH(Secure Shell) protocol, that standarding
 * secure connection and encrypted exchange of messages.
 *
 * @author Diogo Pinto <dio@lognull.com>
 * @since 1.0
 * @see ssh man
 */
public class Ssh implements Client
{
	private static final LoggerHelper LOG = LoggerHelper
			.thisClass( Ssh.class );

	/** The target IP Address from endpoint */
	private String ip = null;

	private String username = null;

	private String password = null;

	/** Default port that can be overwritten */
	private int port = 22;

	/** Holds data from endpoint */
	protected String receive = "";

	/** Information about endpoint credential */
	private EndpointInfo.Credential credential = null;

	/** Reads data from endpoint */
	protected InputStream in = null;

	protected Session session = null;
	protected Channel channel = null;
	private UserInfo ui = null; 

	/**
	 * Constructor default with endpoint entity. This entity represents the
	 * endpoint whose this client will connect.
	 *
	 * @param endpoint
	 *            the endpoint whose this client will connect.
	 * @throws ClientException
	 *             There least two reasons by which this client might not
	 *             working correctly. One is because the own client introducing
	 *             problem and another is by the fact to occur problems with of
	 *             endpoint target.
	 */
	public
	Ssh( final EndpointInfo endpoint ) throws ClientException
	{
		this.ip = endpoint.getIp();
		this.username = endpoint.getCredential().getUser();
		this.password = endpoint.getCredential().getPassword();
		if ( endpoint.getPort() != 0 ) this.port = endpoint.getPort();
		this.credential = endpoint.getCredential();

		try
		{
			JSch jsch = new JSch();
			session = jsch.getSession( credential.getUser(), ip, port );
			ui = new ClientInfo();
		}
		catch( JSchException je )
		{
			throw new ClientException( Error.ER_CREATE_SOCKET_CONNECTION + " "
					+ this.ip + ":" + this.port, je );	
		}
	}

	/**
	 * @see Client#getIpFromEndpoint()
	 */
	public String
	getIpFromEndpoint()
	{
		return this.ip;
	}

	/**
	 * @see Client#getPort()
	 */
	public int
	getPort()
	{
		return this.port;
	}

	/**
	 * @see Client#connect()
	 */
	public void 
	connect() throws ClientException
	{
		try
		{
			session.setUserInfo( ui );
			session.connect();
		}
		catch( JSchException je )
		{
			throw new ClientException( Error.ER_CREATE_SOCKET_CONNECTION + " "
					+ this.ip + ":" + this.port, je );
		}
	}

	/**
	 * @see Client#disconnect()
	 */
	public void
	disconnect()
	{
		if ( channel != null )
			channel.disconnect();
		if ( session != null )
			session.disconnect();
	}

	/**
	 * @see Client#send(String)
	 */
	public void
	send( String message ) throws ClientException
	{
		try
		{
			channel = session.openChannel( "exec" );
			( (ChannelExec) channel).setCommand( message );
			channel.setInputStream( null );
			( (ChannelExec) channel ).setErrStream( System.err );
			in = channel.getInputStream();
			channel.connect();

			__read();
		}
		catch( IOException ioe )
		{
			throw new ClientException( Error.ER_INPUT_OUTPUT_STREAM, ioe );
		}
		catch ( JSchException je )
		{
			throw new ClientException( Error.ER_CREATE_SOCKET_CONNECTION, je );
		}
	}

	/**
	 * @see Client#receive()
	 */
	public String
	receive()
	{
		return receive;
	}

	/**
	 * @see Client#isConnect()
	 */
	public boolean
	isConnect()
	{
		return true;
	}

	public String
	getUsername()
	{
		return username;
	}

	public String
	getPassword()
	{
		return password;
	}

	private void
	__read() throws ClientException
	{
		byte[] tmp = new byte[ 1024 ];
		StringBuilder sb = new StringBuilder();

		while( true )
		{
			try
			{
				while( in.available() > 0 )
				{
					int i = in.read( tmp, 0, 1024 );
					if ( i < 0 ) break;

					sb.append( new String( tmp, 0, i ) );
				}
			}
			catch( IOException ioe )
			{
				throw new ClientException( Error.ER_INPUT_OUTPUT_STREAM, ioe );
			}

			if ( channel.isClosed() )
			{
				try 
				{
					if ( in.available() > 0 ) continue;
				}
				catch( IOException ioe ) 
				{
					throw new ClientException( Error.ER_INPUT_OUTPUT_STREAM, ioe );
				}

				LOG.debug( "exit-status: " + channel.getExitStatus() );
				break;
			}
			try
			{
				Thread.sleep( 1000 );
			}
			catch( Exception ee )
			{
				throw new ClientException( Error.ER_UNEXPECTED_ERROR, ee );
			}
		}
		receive = sb.toString();
	}

	/**
	 * Wrapper class that have contents information about client user
	 * that desired connect to endpoint.
	 */
	private class ClientInfo implements UserInfo
	{
		public void
		showMessage( String message )
		{
			LOG.info( message );
		}

		public boolean
		promptYesNo( String message )
		{
			return true;
		}

		public String
		getPassword()
		{
			return credential.getPassword();
		}

		public boolean promptPassphrase( String message ){ return true; }
		public boolean promptPassword( String message ) { return true; }
		public String getPassphrase(){ return null; }
	}
}
