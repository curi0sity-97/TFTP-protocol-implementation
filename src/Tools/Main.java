package Tools;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import java.util.Scanner;

import client.TFTPClientTemplate;
import server.TFTPServerTemplate;

public class Main 
{
	
	//creating a client and a server

	public static void main(String[] args) throws Exception 
	{	
	}
	
	public void intro()
	{
		System.out.println("Welcome to client-server application!" +"\n" + "Type in:" +"\n" + "RRQ to get a file" +"\n"+ "WRQ to change a file"+"\n");
		String eingabe = "";
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		
		do
		{
			eingabe = sc.next();
			if( !eingabe.equalsIgnoreCase("RRQ") && !eingabe.equalsIgnoreCase("WRQ")) System.out.println("Error!: "+ eingabe + " is not a valid choice" );
		}
		while( !eingabe.equalsIgnoreCase("RRQ") && !eingabe.equalsIgnoreCase("WRQ"));

		System.out.println("...\n"+"...\n" +"...\n");
		
		//if( eingabe.equalsIgnoreCase("RRQ") ) ClientRRQ();
	}

	
	public static String display(byte[] packet)
	{
		String msg = new String(packet);
		msg = msg.substring(2, msg.indexOf("netascii")-1);
		return msg;
	}
	

	public static void ClientRRQ() throws Exception
	{
		System.out.println("Type in the file name (e.g. test.txt):");
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		String eingabe = "";
		byte wrq[] = null;
		
		boolean inputValid = false;
		while(!inputValid)
		{
			try 
			{
				eingabe = sc.nextLine();
				wrq = PackageCreator.createRRQPacket(eingabe);
				inputValid = true;
			}
			catch (Exception e)
			{
				System.out.println("Invalid input");				
			}
		}
		
		DatagramSocket clientSocket = new DatagramSocket();	
		DatagramPacket packet = new DatagramPacket(wrq, wrq.length, InetAddress.getByName("localhost"), 69);	
		clientSocket.send(packet);
		clientSocket.close();

		System.out.println( "requesting: "+eingabe+" from server\n" +"...\n"+"...\n"+"...\n");	
		
		//receive and display
		DatagramSocket server = new DatagramSocket(69);		
		byte[] receivedPacket = new byte[1024];				
		DatagramPacket packet1 = new DatagramPacket(receivedPacket, receivedPacket.length);			
		server.receive(packet1);
		System.out.println("Server: Recieved read request for file: " + display(receivedPacket) );
		server.close();
	}
	
	public static void ServerData()
	{
		
	}
}
