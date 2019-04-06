package com.lognull.libnetwork.consumer.spi;

import com.lognull.libnetwork.exception.ClientException;

/**
 * Client abstraction from model Client-Server, whose client side
 * is the consumer and, the server side is the producer.
 * 
 * @author Diogo Pinto <dio@lognull.com>
 * @since 1.0
 */
public interface Client
{
	/**
	 * Returns the ip/address to which the client is connected.
	 */
	public String
	getIpFromEndpoint();

	/**
	 * Returns the port to which the client is connected.
	 */
	public int
	getPort();

	/**
	 * Connects to endpoint.
	 * 
	 * @throws ClientException
	 *             If client cannot connect to endpoint
	 */
	public void
	connect() throws ClientException;

	/**
	 * Closes the connection with the endpoint.
	 * 
	 * @throws ClientException
	 *             If client cannot disconnect to endpoint
	 */
	public void
	disconnect() throws ClientException;

	/**
	 * Sends message to endpoint.
	 * 
	 * @param message
	 *            The target message to send.
	 * @throws ClientException
	 *             If client cannot send message to endpoint
	 */
	public void
	send( String message ) throws ClientException;

	/**
	 * Receives message from endpoint.
	 * 
	 * @return message get from endpoint or null, if was not received
	 * @throws ClientException
	 *             If client cannot receive message to endpoint
	 */
	public String
	receive() throws ClientException;

	/**
	 * Returns the connection state of the client.
	 */
	public boolean
	isConnect();

	/**
	 * @return the name from user.
	 */
	public String
	getUsername();

	/**
	 * @return the password from authentication
	 */
	public String
	getPassword();
}
