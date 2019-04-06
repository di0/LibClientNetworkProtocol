package com.lognull.libnetwork;

import com.lognull.libnetwork.consumer.spi.Client;
import com.lognull.libnetwork.exception.ClientException;
import com.lognull.libnetwork.helper.Error;
import com.lognull.libnetwork.helper.LoggerHelper;
import com.lognull.libnetwork.notify.ClientCallBack;

/**
 * This class have as responsibility, managering a client specific, starting
 * your connections, sending your messages or receiving them. This class can also
 * if you prefer, delegate the response message to registered callback entity.
 * 
 * See the examples on directory sample to know how it works with callback
 * through this manager client.
 *
 * @author Diogo Pinto <dio@lognull.com>
 * @since 1.0
 */
public class ClientWrapper
{
	/** Log this class */
	private static final LoggerHelper LOG = LoggerHelper
			.thisClass( ClientWrapper.class );

	/** Registered Callback which will be invoked after
	  of the response */
	private ClientCallBack clientCallBack = null;

	/** The client */
   	private Client client = null;

	/**
	 * Builds a client which will be managed.
	 * 
	 * @param client
	 *            The client which will be managed
	 */
	public
	ClientWrapper( final Client client ) throws ClientException
	{
		this.client = client;
	}

	/**
	 * Connects the given client to the target endpoint host.
	 */
	public void
	connect() throws ClientException
	{
		client.connect();
	}

	/**
	 * Sends a message at the endpoint.
	 * 
	 * @param message
	 *            The given message that will be send
	 */
	public void
	send( final String message ) throws ClientException
	{
		// FIXME: Argh! If client do not sending
		// login(anonymous mode), we get this error:
		// java.lang.OutOfMemoryError: Java heap space
		// We should always send credential, until this
		// problem is fixed. 
		if ( ! client.isConnect() )
			throw new ClientException( Error
				.ER_CLIENT_IS_NOT_CONNECTED );

		LOG.debug( "Send message..." );
		client.send( message );

		if ( clientCallBack != null )
			clientCallBack.onResponse(
				client.receive() );
		else
		{
			LOG.warn( "There is no active listener to"
				+ " this client Perhaps you would"
				+ " like register a listener that"
				+ " will receive messages from"
				+ " endpoint "
				+ client.getIpFromEndpoint()
				+ ". For more details, see"
				+ " examples available on samples" 
				+ " directory." );
		}
	}

	/**
	 * Closes client connection side with endpoint.
	 */
	public void
	close() throws ClientException
	{
		client.disconnect();
	}

	/**
	 * @return The IP Address from endpoint.
	 */
	public String
	getIpFromEndpoint()
	{
		return client.getIpFromEndpoint();
	}

	public String
	getUsername()
	{
		return client.getUsername();
	}

	public String
	getPassword()
	{
		return client.getPassword();
	}

	/**
	 * Registers an entity which will be invoke when
	 * messages arrive from endpoint.
	 *
	 * @see #ClientCallBack{@link #addListener(ClientCallBack)}
	 */
	public void
	registerCallBack( ClientCallBack clientCallBack )
	{
		this.clientCallBack = clientCallBack;
	}
}
