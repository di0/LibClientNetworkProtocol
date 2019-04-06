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

public class ScpTest implements ClientCallBack
{
	private static final LoggerHelper LOG = LoggerHelper
			.thisClass( Main.class );

	static int total = 0;
	static int aux = 0;
	Progress bar;

	public void createClient() throws ClientException
	{
		bar = Progresso.init();

		// We create the information about the endpoint that our
		// client will connect.
		EndpointInfo endpoint = new EndpointInfo();
		endpoint.setIp( "192.168.1.0" );
		endpoint.setPort( 22 );
		endpoint.setProtocol( Protocol.SSH );

		// We create the endpoint credential whose our client
		// will connect.
		EndpointInfo.Credential credential = endpoint.new Credential();
		credential.setUser( "root" );
		credential.setPassword( "foo123" );
		endpoint.setCredential( credential );
		ScpClient scp = new Scp( endpoint, this );
		System.out.println( "Starting copy..." );
		scp.copyFromRemote( "/remote/tmp/foo.txt", "/local/tmp/" );
		System.out.println( "\nend copy..." );
	}

	public void onResponse( String response )
	{
		if ( response != null )
		{
			if ( response.contains( "Filesize" ) ) {
				total = Integer.valueOf(
						response.split( ":" )[ 1 ] );
			}
			if ( response.contains( "Remaining" ) )
			{
				int restante = Integer.valueOf(
						response.split( ":" )[ 1 ] );
				bar.update( restante, total );
			}
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
			LOG.error( "An error occurred while attempting to
					+ " create client >> ", ce );
		}
	}
}
