package client;

import java.net.SocketException;
import java.net.UnknownHostException;

public class StartClient 
{
	public static void main(String[] args)
	{
		TFTP_client client;
		
		try 
		{ 
			client = new TFTP_client(); 
			client.startClient();
		}
		catch (Exception e) { e.printStackTrace(); }
	}


}
