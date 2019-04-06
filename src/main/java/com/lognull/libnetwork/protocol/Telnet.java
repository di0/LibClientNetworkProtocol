package com.lognull.libnetwork.protocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.SocketException;

import org.apache.commons.net.telnet.TelnetClient;

import com.lognull.libnetwork.consumer.spi.Client;
import com.lognull.libnetwork.exception.ClientException;
import com.lognull.libnetwork.helper.Error;
import com.lognull.libnetwork.helper.LoggerHelper;
import com.lognull.libnetwork.producer.EndpointInfo;

/**
 * This class encapsules and provide the communication between client
 * and server through the Protocol TELNET, whose provide general,
 * bi-directional, eight-bit byte oriented communications.
 * 
 * @author Diogo Pinto <dio@lognull.com>
 * @since 1.0
 */
public class Telnet implements Client
{
	private static final LoggerHelper LOG = LoggerHelper
			.thisClass( Telnet.class );

	/** The target IP Address from endpoint */
	private String ip = null;

	private String username = null;

	private String password = null;

	/** Default port that can be overwritten */
	private int port = 23;

	/** Holds data from endpoint */
	private String receive = "";

	/** Information about endpoint credential */
	private EndpointInfo.Credential credential = null;

	/** Reads data from endpoint */
	private InputStream in = null;

	/** Writes on remote endpoint */
	private PrintStream out = null;

	private TelnetClient telnet = null;

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
	Telnet( EndpointInfo endpoint ) throws ClientException
	{
		try
		{
			this.ip = endpoint.getIp();
			this.username = endpoint.getCredential().getUser();
			this.password = endpoint.getCredential().getPassword();
			if ( endpoint.getPort() != 0 )
				this.port = endpoint.getPort();

			this.credential = endpoint.getCredential();

			telnet = new TelnetClient();
		}
		catch( Exception e )
		{
			throw new ClientException( Error.ER_CREATE_SOCKET_CONNECTION + " "
					+ this.ip + ":" + this.port, e );
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
			LOG.debug( "Starting connection..." );
			telnet.connect( ip, port );

			in = telnet.getInputStream();
			out = new PrintStream( telnet.getOutputStream() );

			// Encountered credential, we going to login. 
			if ( null != credential )
				__login();
			else
				LOG.debug( "Anonymous access." );
		}
		catch( SocketException se )
		{
			throw new ClientException( Error.ER_CREATE_SOCKET_CONNECTION + " "
					+ this.ip + ":" + this.port, se );
		}
		catch( IOException ioe )
		{
			throw new ClientException( Error.ER_INPUT_OUTPUT_STREAM, ioe );
		}
	}

	/**
	 * @see Client#close()
	 */
	public void
	close() throws ClientException
	{
		try
		{
			LOG.debug( "Closing communication with server " + ip );
			telnet.disconnect();
		}
		catch( IOException io )
		{
			throw new ClientException( Error.ER_INPUT_OUTPUT_STREAM, io );
		}
	}

	/**
	 * @see Client#send(String)
	 */
	public void
	send( String message ) throws ClientException
	{
		this.receive = __send( message );
	}

	/**
	 * @see Client#receive()
	 */
	public
	String receive()
	{
		return this.receive;
	}

	/**
	 * @see Client#isConnect()
	 */
	public boolean
	isConnect()
	{
		return telnet.isConnected();
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

	/*
	 * Sends something to the endpoint.
	 */
	private String
	__send( String something ) throws ClientException
	{
		if ( ! isConnect() )
			throw new ClientException( Error.ER_CLIENT_IS_NOT_CONNECTED );
		try
		{
			String prompt = "$";
			__write( something );
			return __readUntil( prompt + " " );
		} 
		catch( Exception e )
		{
			throw new ClientException( Error.ER_UNEXPECTED_ERROR, e );
		}
	}

	/*
	 * Reads until specific pattern. 
	 */
	private String
	__readUntil( String pattern )
			throws ClientException
	{
		try
		{
			char lastChar = pattern.charAt( pattern.length() - 1 );
			StringBuffer sb = new StringBuffer();
			char ch = (char) in.read();
			while( true )
			{
				sb.append( ch );
				if ( ch == lastChar )
				{
					if ( sb.toString().endsWith( pattern ) )
						return sb.toString();
				}
				ch = (char) in.read();
			}
		}
		catch( Exception e )
		{
			throw new ClientException( Error.ER_UNEXPECTED_ERROR, e );
		}
	}

	/*
	 * Writes to output Stream.
	 */
	private void
	__write( String value ) throws ClientException
	{
		try
		{
			out.println( value );
			out.flush();
		}
		catch( Exception e )
		{
			throw new ClientException( Error.ER_UNEXPECTED_ERROR, e );
		}
	}

	/*
	 * Starts process of login with credentials given previously. Obviously,
	 * just to authentication that is not anonymous.
	 * 
	 * The taken prompt from command line, can match with "#" or "$". The
	 * __readUntil method, getting through all String until it's match with one
	 * of the two quoted prompts.
	 */
	private void
	__login() throws ClientException
	{
		__readUntil( "login: " );
		__write( credential.getUser() );
		__readUntil( "Password: " );
		__write( credential.getPassword() );
	
		String prompt = "$";
		__readUntil( prompt + " " );
	}
}
