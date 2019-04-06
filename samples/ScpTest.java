import com.lognull.libnetwork.consumer.spi.Client;
import com.lognull.libnetwork.consumer.ClientFactory;
import com.lognull.libnetwork.ClientWrapper;
import com.lognull.libnetwork.exception.ClientException;
import com.lognull.libnetwork.helper.LoggerHelper;
import com.lognull.libnetwork.notify.ClientCallBack;
import com.lognull.libnetwork.producer.EndpointInfo;
import com.lognull.libnetwork.protocol.Protocol;
import com.lognull.libnetwork.utility.extra.Scp;
import com.lognull.libnetwork.utility.extra.ScpClient;
import com.lognull.libnetwork.utility.extra.ScpNotify;

public class ScpTest
{
	private static final LoggerHelper LOG = LoggerHelper
			.thisClass( ScpTest.class );

	static int total = 0;
	static int aux = 0;
	static Progress bar;

	public void createClient() throws ClientException
	{
		bar = Progress.init();

		// We create the information about the endpoint that our
		// client will connect.
		EndpointInfo endpoint = new EndpointInfo();
		endpoint.setIp( "192.168.1.0" );
		endpoint.setPort( 22 );
		endpoint.setProtocol( Protocol.SSH ); // We can use Protocol.TELNET and the port 23. 

		// We create the endpoint credential whose our client
		// will connect.
		EndpointInfo.Credential credential = endpoint.new Credential();
		credential.setUser( "root" );
		credential.setPassword( "foo123" );
		endpoint.setCredential( credential );
		ScpClient scp = new Scp( endpoint, new HandlerScpNotify() );
		System.out.println( "Starting copy..." );
		scp.copyFromRemote( "/from_remote/tmp/foo.txt", "/to_local/tmp/" );
		System.out.println( "\nend copy..." );
		scp.close();
	}

	private static class HandlerScpNotify implements ScpNotify
        {
		/** Represents the total size already transfered. */
		private static int total;

		/**
		 * Callback invoked to set the size of the target
		 * download.
		 */
		@Override
		public void onTargetSize( long targetSize )
		{
			total = (int)targetSize;
		}

		/**
		 * Callback invoked while a transfer is still
		 * remaining.
		 */
		@Override
		public void onRemaining( long remaining )
		{
			bar.update( (int) remaining, total );
		}
        }

	public static void main( String[] args )
	{
		try
		{
			new ScpTest().createClient();
		}
		catch( ClientException ce )
		{
			LOG.error( "An error occurred while attempting to"
					+ " create client >> ", ce );
		}
	}
}
