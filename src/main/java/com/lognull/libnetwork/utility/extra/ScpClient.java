package com.lognull.libnetwork.utility.extra;

import com.lognull.libnetwork.exception.ClientException;

/**
 * This interface, hidden the complexity availaible in Client SSH entity.
 *
 * @author Diogo Pinto <dio@lognull.com>
 * @since 1.0
 */
public interface ScpClient
{
	/**
	 * This method copy something from remote endpoint.
	 *
	 * @param from
	 *            source of something from remote
	 * @param to
	 *            destiny where something will be copied
	 * @throws ClientException
	 *             if client is not availaible by some reason
	 */
	public void
	copyFromRemote( String from, String to ) throws ClientException;

	/**
	 * Connects on endpoint.
	 */
	public void
	connect();

	/**
	 * Closes the connection with endpoint.
	 */
	public void
	close();
}
