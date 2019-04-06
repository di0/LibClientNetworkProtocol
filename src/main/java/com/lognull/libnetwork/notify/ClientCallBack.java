package com.lognull.libnetwork.notify;

/**
 * This interface is used by endpoint target to pass the data back after
 * process.
 *
 * @author Diogo Pinto <dio@lognull.com>
 * @since 1.0
 */
public interface ClientCallBack
{
	/**
	 * Provide call back functionality after the message response
	 * arrive from endpoint.
	 *
	 * @param message
	 *            The given message received from endpoint.
	 */
	public void
	onResponse( String message );
}
