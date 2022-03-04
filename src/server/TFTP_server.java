package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Arrays;

import Tools.ContainedFiles;
import Tools.Helper;
import Tools.PackageCreator;

public class TFTP_server
	{
		int recieveTimeOut;
		int upTime;
		int portNumber;
		int vs;
		DatagramSocket serverSocket;
		byte[] receiveData = new byte[1024];
		byte[] sendData = new byte[1024];
		long end = System.currentTimeMillis()+upTime;
		String test = "Consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus."; 
		String newWrite = "Die Welt ist meine Vorstellung: - dies ist die Wahrheit, welche in Beziehung auf jedes lebende und erkennende Wesen gilt; wiewohl der Mensch allein sie in das reflektirte abstrakte Bewußtseyn bringen kann: und thut er dies wirklich; so ist die philosophische Besonnenheit bei ihm eingetreten. Es wird ihm dann deutlich und gewiß, daß er keine Sonne kennt und keine Erde; sondern immer nur ein Auge, das eine Sonne sieht, eine Hand, die eine Erde fühlt; daß die Welt, welche ihn umgiebt, nur als Vorstellung da ist, d.h. durchweg nur in Beziehung auf ein Anderes, das Vorstellende, welches er selbst ist. - Wenn irgendeine Wahrheit a priori ausgesprochen werden kann, so ist es diese: denn sie ist die Aussage derjenigen Form aller möglichen und erdenklichen Erfahrung, welche allgemeiner, als alle andern, als Zeit, Raum und Kausalität ist: denn alle diese setzen jene eben schon voraus, und wenn jede dieser Formen, welche alle wir als so viele besondere Gestaltungen des Satzes vom Grunde erkannt haben, nur für eine besondere Klasse von Vorstellungen gilt; so ist dagegen das Zerfallen in Objekt und Subjekt die gemeinsame Form aller jener Klassen, ist diejenige Form, unter welcher allein irgend eine Vorstellung, welcher Art sie auch sei, abstrakt oder intuitiv, rein oder empirisch, nur überhaupt möglich und denkbar ist. Keine Wahrheit ist also gewisser, von allen andern unabhängiger und eines Beweises weniger bedürftig, als diese, daß Alles, was für die Erkenntniß daist, also die ganze Welt, nur Objekt in Beziehung auf das Subjekt ist, Anschauung des Anschauenden, mit Einem Wort, Vorstellung. Natürlich gilt Dieses, wie von der Gegenwart, so auch von jeder Vergangenheit und jeder Zukunft, vom Fernsten, wie vom Nahen: denn es gilt von Zeit und Raum selbst, in welchen allein sich dieses alles unterscheidet. Alles, was irgend zur Welt gehört und gehören kann, ist unausweichbar mit diesem Bedingtseyn durch das Subjekt behaftet, und ist nur für das Subjekt da. Die Welt ist Vorstellung. Neu ist diese Wahrheit keineswegs. Sie lag schon in den skeptischen Betrachtungen, von welchen Cartesius ausgieng. Berkeley aber war der erste, welcher sie entschieden aussprach: er hat sich dadurch ein unsterbliches Verdienst um die Philosophie erworben, wenn gleich das Uebrige seiner Lehren nicht bestehn kann. Kants erster Fehler war die Vernachlässigung dieses Satzes, wie im Anhange ausgeführt ist. - Wie früh hingegen diese Grundwahrheit von den Weisen Indiens erkannt worden ist, indem sie als der Fundamentalsatz der dem Vyasa";
		boolean gotACK = false;
		boolean ending = false;
		
		public TFTP_server(int recieveTimeOut, int upTime, int portNumber)
		{
			this.recieveTimeOut = recieveTimeOut;
			this.upTime = upTime;
			this.portNumber = portNumber;
			this.vs = 0;
			this.end = System.currentTimeMillis()+upTime;
			
		}
		
    	public void startServer()
    	{
    	    try 
    	    {
    	        serverSocket = new DatagramSocket(portNumber);  	        
    			System.out.println("Server is opend at port number: "+ ((portNumber==69) ? "69 (TFTP)": "portNumber") +",\nRuns for "+upTime/1000+" seconds,\nCloses after "+recieveTimeOut/1000+" seconds if nothing is recevied.");

    	        while(System.currentTimeMillis() < end)
    	        {        // recieve data until timeout
	            	DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
	            	serverSocket.setSoTimeout(recieveTimeOut);   // set the timeout in millisecounds.
    	            try 
    	            {
    	    		    serverSocket.receive(receivePacket);
    	    		    
    	    		    
    	    		    if(Helper.getMode(receivePacket.getData()) == 1);
    	    		    {
    	    		    	System.out.println("\nRRQ received\n");
    	    		    	vs++;
    	    		    	answerRRQ(receivePacket);
    	    		    }  	
    	            }
    	            catch (SocketTimeoutException e) 
    	            {
    	                System.err.println("Server runtime is over after "+recieveTimeOut/1000+" seconds!");
    	                ending = true;
    	                serverSocket.close();
    	            }
    	        }
    	        System.err.println("Server closed, run time is over");
    	        serverSocket.close();
    	    }
    	    catch (SocketException e1) { System.out.println("Server socket closed " + e1); }
    	    catch (IOException e) {; e.printStackTrace(); }   
    	}
    	
    	public void answerRRQ(DatagramPacket receivePacket)
    	{
    		byte[] temp = receivePacket.getData();
    		byte[] data = Arrays.copyOfRange(temp, 0, Helper.getLength(temp)+1);
    		//byte[] data = Arrays.copyOfRange(temp, 0, java.util.Arrays.asList(temp).indexOf(0));
    		//byte[] data = temp;
    		String[] name = Helper.inspectPackage(data, false);
    		//System.out.println("\nServer received:\n"+Helper.inspectPackage(data, true));
    		String content;
    		
    		//Only default case for now
    		if( name[1].equalsIgnoreCase("test.txt"))
    			content=test;
    		else if(name[1].equalsIgnoreCase("s.txt"))
    			content=newWrite;
    		else
    			content="File does not exist";
    		
    		byte[][] toSend = PackageCreator.createDataPacket(content, vs);
    		System.out.println("\n--------------------------------------\n--------------------------------------\n"+toSend.length+" packets to send:\n");
    		DatagramPacket sendPacket;
    		
    		for(int i=0; i<toSend.length; i++)
    		{
    			System.out.println("Paket nr: "+(i+1));
    			gotACK = false;
    			sendPacket = new DatagramPacket( toSend[i], toSend[i].length, receivePacket.getAddress(), receivePacket.getPort());
    			Helper.inspectPackage(toSend[i], true);
    			
    			// set the socket-timeout to 3 seconds
    			try { serverSocket.setSoTimeout(3000); }
    			catch (SocketException e) { e.printStackTrace();}
 
    			// Send the packet and wait for an ACK, if an ACK is not received
    			// retransmit the packet
    			while (!gotACK) 
    			{
    			    try 
    			    {
    			    	serverSocket.send(sendPacket);
    					System.out.println("Packet sent, waiting for ACK.\n");
    			    }
    			    catch (IOException e) { e.printStackTrace(); }    			    
    			    try 
    			    {
	    				serverSocket.receive(receivePacket);
	    				byte[] ackPack = Arrays.copyOfRange(receivePacket.getData(), 0, 4);
	    				
	    				if( Helper.getBlockNumber(ackPack) == vs && Helper.getMode(ackPack) == 4)
	    				{
	    					System.out.println("Right Ack number, client received datagram");
	    					gotACK = true;
	    					vs++;
	    				}
	    				else
	    					System.out.println("Ivalid: "+Helper.getBlockNumber(ackPack) +" Blocknumber, "+ Helper.getMode(ackPack)+" Mode\nExpected: "+vs+" Blocknumber, 4 Mode\n");
	    				
	    				Helper.inspectPackage(ackPack, true);	
    			    }
    			    catch (IOException e) { System.err.println("Receive-method had a timeout. Restarting transmission."); }
    			    
    			    System.out.println("Package nr: "+(i+1)+" was successfully transmitted and received\n--------------------------------------\n--------------------------------------\n");
    			}

    		}
    		
    	}
    	
    	public void answerWRQ()
    	{
    		
    	}
    	
    	
}

		





