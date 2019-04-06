package com.lognull.libnetwork.producer;

import com.lognull.libnetwork.protocol.Protocol;

/**
 * Holds information about endpoint.
 *
 * @author Diogo Pinto <dio@lognull.com>
 * @since 1.0
 */
public class EndpointInfo
{
	private String hostname = "";
	private String ip = "";
	private int port;
	private Protocol protocol = null;
	private Credential credential = null;

	public String getHostname() { return hostname; }
	public void setHostname( String hostname ) { this.hostname = hostname; }

	public String getIp() { return ip; }
	public void setIp( String ip ) { this.ip = ip; }

	public int getPort() { return port; }
	public void setPort( int port ) { this.port = port; }

	public Protocol getProtocol() { return protocol; }
	public void setProtocol( Protocol protocol ) { this.protocol = protocol; }

	public Credential getCredential() { return credential; }
	public void setCredential( Credential credential ) { this.credential = credential; }

	public class
	Credential
	{
		private String user = "";
		private String password = "";

		public String
		getUser()
		{
			return user;
		}

		public void
		setUser( String user )
		{
			this.user = user;
		}

		public String
		getPassword()
		{
			return password;
		}

		public void
		setPassword( String password )
		{
			this.password = password;
		}
	}
}
