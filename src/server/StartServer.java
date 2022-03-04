package server;

public class StartServer 
{
	public static void main(String[] args)
	{
		int receiveTimeOutSeconds = 60;
		int serverTimeOutSeconds = 60;
		int portNumber = 69;
		TFTP_server server = new TFTP_server((receiveTimeOutSeconds*1000), (serverTimeOutSeconds*1000), portNumber);
		server.startServer();
	}

}
