# LibClientNetworkProtocol
Creates a connection remote with endpoint using abstractedly a protocol communication.

#### Choosing between Protocols

This library provides a way of connect to the endpoint of abstract form, choosing between an intended
protocol.

By example:

Using ***SSH*** Protocol, you can do something like it:
```java
EndpointInfo endpoint = new EndpointInfo();
endpoint.setIp( "192.168.1.0" );
endpoint.setPort( 22 );
endpoint.setProtocol( Protocol.SSH );
```

In this case, you do not need to specify the **port 22**, by default we assume that the port is 22.

Or, ***TELNET*** Protocol:
```java
endpoint.setProtocol( Protocol.TELNET );
```

specifying in this case, the **port 23**:
```java
endpoint.setPort( 23 );
```

Here also, if you not specific the port, will be used the port 23 by default.

Once choose the protocol, you might authenticate on endpoint, using **EndpointInfo.Credential**
utility, specifing the corresponding information. By example:
```java
EndpointInfo.Credential credential = endpoint.new Credential();
credential.setUser( "foo" );
credential.setPassword( "12345" );
```

So, we set the credential to endpoint:
```java
endpoint.setCredential( credential );
```

To create the client to the endpoint target, we can do like this:
```java
ClientWrapper client = new ClientWrapper( ClientFactory
              .createByEndpoint( endpoint ) );
```

Once created, we connect to the endpoint:
```java
client.connect();
```

and finally we can send the message to the remote target:
```java
client.send( "ls -la /tmp/" );
client.send( "grep -RHin \"word\" /tmp/Foo.txt" );
// and so on ...
```

So, you must close the connection this way:
```java
client.close()
```

If you desire receive the response, you must register a callback through **ClientCallBack** interface and consume the message response injected on method onResponse of this interface.

For more details, look into ***samples*** directory, by **SshOrTelnet.java** class to demo stretches.

#### Easter Egg

This library also provides an extra functionally, through Secure Copy Protocol(SCP) utility. The **SCP.java** class is the
responsible by provide this goal.

The ***SCP*** class is an extension of the **SSH** class, so we just use the **EndpointInfo** and **EndpointInfo.Credential** as we done early in SSH connection demo. Once created respectively the endpoint info and their corresponding
credential, we can do this:
```java
ScpClient scp = new Scp( endpoint );
```

To go along get the processing of the copy, you can register an observer implementing the **ScpNotify** interface, abstracting the methods **onTargetSize** and **onRemaining** where onTargetSize method returns the total size of the target and the onRemaining method returns the remaining size to complete.

If you are interested to be notified, you must use the ***Scp( EndpointInfo endpoint, ScpNotify scpNotify )*** constructor
instead ***Scp( EndpointInfo )**** constructor.
i.e:
```java
ScpClient scp = new Scp( endpoint, this );
```
Where in this case, the **"this"** reference implements the **ScpNotify** interface.

So, you must close the connection this way:
```java
scp.close()
```

For more details, look into samples directory, the **ScpTest.java** class demo.
