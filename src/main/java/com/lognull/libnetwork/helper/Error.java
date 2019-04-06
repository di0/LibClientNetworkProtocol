package com.lognull.libnetwork.helper;

/**
 * This class encapsulate and holding business errors.
 *
 * @author Diogo Pinto <dio@lognull.com>
 * @since 1.0
 */
public class Error
{
	public static String ER_NOT_SUPPORTED_PROTOCOL = "Client does not support this protocol.";
	public static String ER_CREATE_SOCKET_CONNECTION = "Cannot Connect: Socket Connection Failed.";
	public static String ER_CLIENT_IS_NOT_CONNECTED = "Client is not connected.";
	public static String ER_ENDPOINT_DATA_NOT_FOUND = "Data information of endpoint not found.";
	public static String ER_ENDPOINT_IP_ADDRESS_NOT_AVAILABLE = "IP address or hostname do not available.";
	public static String ER_ENDPOINT_PROTOCOL_NOT_AVAILABLE = "Protocol is not available.";
	public static String ER_INPUT_OUTPUT_STREAM = "I/O exception of some sort has occurred.";
	public static String ER_UNEXPECTED_ERROR = "an unexpected error has occurred";
}
