package com.lognull.libnetwork.utility.extra;

/**
 * This interface is used to invoke back the client SCP to
 * talk them about the process copy.
 *
 * @author Diogo Pinto <dio@lognull.com>
 * @since 1.0
 */
public interface ScpNotify
{
	/**
	 * Call back when know the given data size.
	 *
	 * @param targetSize
	 *            information about size data
	 */
	public void
	onTargetSize( long targetSize );

	/**
	 * Call back while data remaining are not over.
	 *
	 * @param targetSize
	 *            information about size data
	 */
	public void
	onRemaining( long remaining );
}
