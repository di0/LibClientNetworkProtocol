package com.lognull.libnetwork.utility.extra;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import com.lognull.libnetwork.exception.ClientException;
import com.lognull.libnetwork.helper.Error;
import com.lognull.libnetwork.producer.EndpointInfo;
import com.lognull.libnetwork.protocol.Protocol;
import com.lognull.libnetwork.protocol.Ssh;

/**
 * This class encapsules and provide the communication between client
 * and server through the SCP(Secure Copy) utility, that standarding
 * secure connection and encrypted copies.
 *
 * @author Diogo Pinto <dio@lognull.com>
 * @since 1.0
 * @see scp man
 * @see ssh man
 */
public class Scp extends Ssh implements ScpClient
{
	/** We use this to notify about our progress */
	private ScpNotify scpNotify = null;

	private OutputStream out = null;

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
	Scp( EndpointInfo endpoint ) throws ClientException
	{
		super( endpoint );

		// We always use SCP through SSH protocol
		endpoint.setProtocol( Protocol.SSH );
	}

	/**
	 * Constructor with endpoint entity and notifier. This entity represents the
	 * endpoint whose this client will connect and the notify, an entity that
	 * works as listener callback.
	 *
	 * @param endpoint
	 *            the endpoint whose this client will connect.
	 * @param scpNotify
	 *            an entity that works as listener callback.
	 * @throws ClientException
	 *             There least two reasons by which this client might not
	 *             working correctly. One is because the own client introducing
	 *             problem and another is by the fact to occur problems with of
	 *             endpoint target.
	 */
	public
	Scp( EndpointInfo endpoint, ScpNotify scpNotify ) throws ClientException
	{
		super( endpoint );
		this.scpNotify = scpNotify;
	}

	/**
	 * This method copy something from remote endpoint.
	 *
	 * @param from
	 *            source of something from remote
	 * @param to
	 *            destiny where something will be copied
	 * @param nameFromSomething
	 *            final name of the something
	 * @throws ClientException
	 *             if client is not availaible by some reason
	 */
	public void
	copyFromRemote( String from, String to ) throws ClientException
	{
		try
		{
			channel = session.openChannel( "exec" );
			( (ChannelExec) channel).setCommand( "scp -r -f " + from );
			channel.setInputStream( null );
			( (ChannelExec) channel ).setErrStream( System.err );
			in = channel.getInputStream();
			out = channel.getOutputStream();

			channel.connect();

			__copyFromRemote( to );
		}
		catch( IOException ioe )
		{
			throw new ClientException( Error.ER_INPUT_OUTPUT_STREAM, ioe );
		}
		catch( JSchException je )
		{
			throw new ClientException( Error.ER_CREATE_SOCKET_CONNECTION, je );
		}
	}

	public void
	connect()
	{
		try {
			super.connect();
		} catch( ClientException ce ) {
			ce.printStackTrace();
		}
	}

	public void
	close()
	{
		super.close();
	}

	private void
	__copyFromRemote( String to ) throws ClientException
	{
		FileOutputStream fos = null;
		try
		{
			String prefix = null;
			if ( new File( to ).isDirectory() )
				prefix = to + File.separator;

			byte[] buf = new byte[ 1024 ];

			// send '\0'
			buf[ 0 ] = 0;
			out.write( buf, 0, 1 );
			out.flush();

			while( true )
			{
				int c = checkAck( in );
				if ( c != 'C' )
					break;

				// read '0644 '
				in.read( buf, 0, 5 );

				long filesize = 0L;
				while( true )
				{
					if ( in.read( buf, 0, 1 ) <0 )
						break;

					if ( buf[0] == ' ' )
						break;

					filesize = filesize * 10L + (long)( buf[0] - '0' );
				}

				// We notify the client about size from file
				if ( scpNotify != null )
					scpNotify.onTargetSize( filesize );

				String file = null;
				for( int i = 0; ;i++ )
				{
					in.read( buf, i, 1 );
					if( buf[i] == (byte)0x0a )
					{
						file = new String( buf, 0, i );
						break;
					}
				}

				// send '\0'
				buf[0] = 0;
				out.write( buf, 0, 1 );
				out.flush();

				// read a content of lfile
				fos = new FileOutputStream( prefix == null ? to : prefix + file );
				int foo;
				while( true )
				{
					if ( buf.length < filesize )
						foo = buf.length;
					else
						foo = (int)filesize;
					foo = in.read( buf, 0, foo );
					if ( foo < 0 )
						break;
					fos.write( buf, 0, foo );
					filesize -= foo;
					// We always notify the about remaining to complete
					if ( scpNotify != null )
						scpNotify.onRemaining( filesize );
					if ( filesize == 0L )
						break;
				}
				fos.close();
				fos = null;

				if ( checkAck( in ) != 0 )
					System.exit( 0 );

				// send '\0'
				buf[ 0 ] = 0;
				out.write( buf, 0, 1 );
				out.flush();
			}
		}
		catch( Exception e )
		{
			try
			{
				if ( fos != null )
					fos.close();
			}
			catch( Exception ignored ){}
			throw new ClientException( Error.ER_UNEXPECTED_ERROR + " : " + e.getMessage() );
		}
	}

	static int checkAck( InputStream in ) throws IOException
	{
		int b = in.read();
		if ( b == 0 ) return b;
		if ( b == -1 ) return b;

		if ( b == 1 || b == 2 )
		{
			StringBuffer sb = new StringBuffer();
			int c;
			do
			{
				c = in.read();
				sb.append( (char)c );
			}
			while( c != '\n' );

			if ( b == 1 )
				System.out.print( sb.toString() );
			if ( b == 2 )
				System.out.print( sb.toString() );
		}
		return b;
	}
}
