package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;
import Tools.Helper;
import Tools.PackageCreator;
import java.util.ArrayList;
import java.util.Arrays;

public class TFTP_client 
{
	int Vr;
	DatagramSocket clientSocket;
	byte[] sendData;
	byte[] receiveData;
	InetAddress IPAddress;
	Scanner inFromUser;
	boolean gotACK;
	int port;
	
	public TFTP_client() throws SocketException, UnknownHostException
	{
		clientSocket = new DatagramSocket();
		Vr = 0;
		receiveData = new byte[1024];
		IPAddress = InetAddress.getByName("localhost");
		clientSocket = new DatagramSocket();
		inFromUser = new Scanner(System.in);
		gotACK = false;
		port = 69;
	}
	
	  public void startClient() throws Exception 
	  {
		System.out.println("Welcome to client-server application!" +"\n" + "Type in:" +"\n" + "RRQ to get a file" +"\n"+ "WRQ to change a file");
		String request;	
		
		do
		{
			request = inFromUser.nextLine();
			if( !request.equalsIgnoreCase("RRQ") && !request.equalsIgnoreCase("WRQ")) System.out.println("Error!: "+ request + " is not a valid choice" );
		}			
		while( !request.equalsIgnoreCase("RRQ") && !request.equalsIgnoreCase("WRQ"));
		System.out.println("...\n");
		
		//Client sends a read request
		if(request.equalsIgnoreCase("RRQ"))
		{
			Vr++;
			System.out.println("Enter the filename (eg. test.txt)");
			String data = inFromUser.nextLine();	
			System.out.println("Send to Server: "+data);
			this.sendRRQ(data);
			
			byte[][] f = this.recieveData();
			Helper.assambleFile(f, "s.txt");
			Helper.toFile(f, "test.txt");
		}
		
		//Receive Data
		
	  }
	  
	  public byte[][] recieveData() throws IOException
	  {
		  ArrayList<Byte[]> data = new ArrayList<Byte[]>();
		  byte[] buffer;
		  int j = 0;
		  do
		  {
			  j++;
			  // Receive Data
			  System.out.println("Recieved PacketNr : "+j);
			  buffer = this.receivePaket();	  
			  data.add(Helper.byteByte(buffer));
			  System.out.println("length of current package: "+ buffer.length+"\n");
			  // Send ACK
			  System.out.println("Send Ack for package nr: "+Helper.getBlockNumber(buffer));
			  this.sendACK(Helper.getBlockNumber(buffer));
		  }
		  while( buffer.length == 516);
		  
		  System.out.println("Print all packages");
		  Byte[][] returnable = new Byte[j][];
		  for(int i=0; i<data.size(); i++)
		  {
			  returnable[i] = data.get(i);
			  System.out.println("received packet number: "+(i+1));
			  Helper.inspectPackage(Helper.Bytebyte(data.get(i)) , false);
		  }
		  
		  return Helper.matrixBytebyte(returnable);
	  }
	  
	  public void sendRRQ(String data) throws IOException
	  {
		sendData = PackageCreator.createRRQPacket(data);
		Helper.inspectPackage(sendData, false);
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
		
		clientSocket.send(sendPacket);
	  }
	  
	  public void sendACK(int blockNumber) throws IOException
	  {
		sendData = PackageCreator.createACKPacket(blockNumber);
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
		Helper.inspectPackage(sendData, true);
		clientSocket.send(sendPacket);
		  
	  }
	  public byte[] receivePaket() throws IOException
	  {
		receiveData = new byte[1024];
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		// Read a datagram from server
		clientSocket.receive(receivePacket);
		byte[] data = receivePacket.getData();
		byte[] findata = Arrays.copyOfRange(data, 0, Helper.getLength(data));
		Helper.inspectPackage(findata, true);
		return findata;
	  }
	  
	  
	  

}
