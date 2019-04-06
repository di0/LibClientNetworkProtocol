import com.lognull.libnetwork.consumer.spi.Client;
import com.lognull.libnetwork.consumer.ClientFactory;
import com.lognull.libnetwork.ClientWrapper;
import com.lognull.libnetwork.exception.ClientException;
import com.lognull.libnetwork.helper.LoggerHelper;
import com.lognull.libnetwork.notify.ClientCallBack;
import com.lognull.libnetwork.producer.EndpointInfo;
import com.lognull.libnetwork.protocol.Protocol;

public class SshOrTelnet implements ClientCallBack
{
	private static final LoggerHelper LOG = LoggerHelper
			.thisClass( SshOrTelnet.class );

	// Override ClientCallBack interface.
	public void onResponse( String message )
	{
		System.out.println( "Message received: " + message );
	}

	public void createClient() throws ClientException
	{
		// We create the information about the endpoint that our
		// client will connect.
		EndpointInfo endpoint = new EndpointInfo();
		endpoint.setIp( "192.168.1.0" );
		endpoint.setPort( 22 );
		endpoint.setProtocol( Protocol.SSH );

		// We create the endpoint credential whose our client will
		// connect.
		EndpointInfo.Credential credential = endpoint.new Credential();
		credential.setUser( "foo" );
		credential.setPassword( "12345" );
		endpoint.setCredential( credential );

		// Lastly, we create our client, binding the endpoint
		// credential with this client.
		ClientWrapper client = new ClientWrapper( ClientFactory
				.createByEndpoint( endpoint ) );
		client.registerCallBack( this );
		client.connect();
		client.send( "grep -RHin \"word\" /tmp/Foo.txt" );
		client.send( "/sbin/ifconfig -a" );
		client.send( "ls -la /tmp/" );
		client.close();
	}

	public static void main( String[] args )
	{
		try
		{
			new SshOrTelnet().createClient();
		}
		catch( ClientException ce )
		{
			LOG.error( "An error occurred while attempting to "
					+ "create client >> ", ce );
		}
	}
}
