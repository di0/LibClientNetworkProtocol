package com.lognull.libnetwork.consumer;

import com.lognull.libnetwork.consumer.spi.Client;
import com.lognull.libnetwork.consumer.spi.SshClient;
import com.lognull.libnetwork.consumer.spi.TelnetClient;
import com.lognull.libnetwork.exception.ClientException;
import com.lognull.libnetwork.helper.Error;
import com.lognull.libnetwork.helper.LoggerHelper;
import com.lognull.libnetwork.producer.EndpointInfo;
import com.lognull.libnetwork.protocol.Ssh;
import com.lognull.libnetwork.protocol.Telnet;

/**
 * Class responsible by factory a client according with available
 * protocol.
 * 
 * A protocol can be SSH, Telnet or any another that works on model
 * client-server.
 * 
 * In version 1.0, we just available SSH and Telnet protocol. However,
 * client is an abstract entity and it can become extensible to anothers
 * protocol that works on model pronounced. Feel free to adjust according
 * your need.
 * 
 * @author Diogo Pinto <dio@lognull.com>
 * @since 1.0
 * 
 */
public class ClientFactory
{
	/** Log this class */
	private static final LoggerHelper LOG = LoggerHelper
			.thisClass( ClientFactory.class );

	/**
	 * Create client according with Endpoint information.
	 *
	 * @param endpoint
	 *            The Endpoint target
	 * 
	 * @return Client configured with input Endpoint
	 * @throws ClientException
	 *             Case client cannot create sucessful
	 */
	public static Client
	createByEndpoint( EndpointInfo endpoint ) throws ClientException
	{
		validateEndpoint( endpoint );

		switch( endpoint.getProtocol() )
		{
			case TELNET:
				LOG.debug( "Connection across protocol "
					+ endpoint.getProtocol() );
				return new TelnetClient(
						new Telnet( endpoint ) );

			case SSH:
				LOG.debug( "Connection across protocol "
					+ endpoint.getProtocol() );
				return new SshClient(
						new Ssh( endpoint ) );

			default:
				throw new ClientException(
					Error.ER_NOT_SUPPORTED_PROTOCOL
					+ endpoint.getProtocol()
						+ " does not available to"
						+ " endpoint " +
						endpoint.getIp() );
		}
	}

	/*
	 * Validate endpoint input.
	 */
	private static void
	validateEndpoint( EndpointInfo endpoint ) throws ClientException
	{
		if ( endpoint == null )
			throw new ClientException(
					Error.ER_ENDPOINT_DATA_NOT_FOUND );

		if ( endpoint.getIp() == null )
			throw new ClientException(
					Error.ER_ENDPOINT_IP_ADDRESS_NOT_AVAILABLE );

		if ( endpoint.getProtocol() == null )
			throw new ClientException(
					Error.ER_ENDPOINT_PROTOCOL_NOT_AVAILABLE );
	}
}
