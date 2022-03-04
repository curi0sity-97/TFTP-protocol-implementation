package Tools;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Helper 
{
	/**
	 * @param pack TFTP message
	 * @param print should a descriptive text be printed on console?
	 * @return returned array represents the specific components of a TFTP message
	 */
	public static String[] inspectPackage(byte[] pack, boolean print)
	{
		//Print binary values of package
		//if(print)
			//System.out.println("--------------------------------------------------------------------------------------------------\n"+"Binary array:\n"+Arrays.toString(pack)+"\n");	
		//A package conversion from byte to String, to extract content
		String message = new String(pack, StandardCharsets.UTF_8);
		//Stores printable textual description of the package
		String printable = "";
		//Dynamic ArrayList for adding the components
		ArrayList<String> content = new ArrayList<String>();
		//Position of upcode is equal to every message format so it can be excluded from case distinctive
		content.add(String.valueOf(byteToInt(Arrays.copyOfRange(pack, 0, 2))));
		
		//filling the TFTP message array with the specific message format components
		switch(byteToInt(Arrays.copyOfRange(pack, 0, 2)))
		{
		case 1: printable = "Mode: "+String.valueOf(byteToInt(Arrays.copyOfRange(pack, 0, 2)))+"(RRQ)"+"\n";
				content.add(message.substring(2, message.indexOf(0, 2)));
				printable += "Filename: " + message.substring(2, message.indexOf(0, 2))+"\n";
				content.add("0");
				content.add(message.substring(message.indexOf(0, 2)+1, message.length()-1));
				printable += "Mode:"+ message.substring(message.indexOf(0, 2), message.length()-1)+"\n";
				content.add("0");				
				break; 
		case 2: printable = "Mode: "+String.valueOf(byteToInt(Arrays.copyOfRange(pack, 0, 2)))+"(WRQ)"+"\n";
				content.add(message.substring(2, message.indexOf(0, 2)));
				printable += "Filename: "+ message.substring(2, message.indexOf(0, 2))+"\n";
				content.add("0");
				content.add(message.substring(message.indexOf(0, 2)+1, message.length()-1));
				printable += "Mode:"+ message.substring(message.indexOf(0, 2), message.length()-1)+"\n";
				content.add("0");							
				break; 
		case 3: printable += "Mode: "+String.valueOf(byteToInt(Arrays.copyOfRange(pack, 0, 2)))+"(Data)"+"\n";
				content.add(String.valueOf(byteToInt(Arrays.copyOfRange(pack, 2, 4))));
				printable += "Blocknumber: "+ String.valueOf(byteToInt(Arrays.copyOfRange(pack, 2, 4)))+"\n";
				content.add(message.substring(4));
				printable += "Data: "+ message.substring(4)+"\n";
				break; 
		case 4: printable += "Mode: "+String.valueOf(byteToInt(Arrays.copyOfRange(pack, 0, 2)))+"(ACK)"+"\n";
				content.add(String.valueOf(byteToInt(Arrays.copyOfRange(pack, 2, 4))));
				printable += "Block number: "+ String.valueOf(byteToInt(Arrays.copyOfRange(pack, 2, 4)))+"\n";
				break; 
		case 5:	printable += "Mode:" +String.valueOf(byteToInt(Arrays.copyOfRange(pack, 0, 2)))+"(Error)"+"\n";
				content.add(String.valueOf(byteToInt(Arrays.copyOfRange(pack, 2, 4))));
				printable += "Error number: "+ String.valueOf(byteToInt(Arrays.copyOfRange(pack, 2, 4)))+"\n";
				content.add(message.substring(4, message.length()-1));
				printable += "Error message: "+ message.substring(2, 4)+"\n";
				content.add("0");				
				break;
		default:return null;
		}
		
		//if(print)
			System.out.println("TFTP Message:\n" + printable +"Size: "+pack.length+" bytes");
			System.out.println("Message components: "+Arrays.toString(content.toArray(new String[content.size()]))+"\n--------------------------------------------------------------------------------------------------\n");
			
		//Converting ArrayList to simple Array
		return content.toArray(new String[content.size()]);
	}
	
	public static int getLength(byte[] data)
	{
		   for (int i = data.length - 1; i > -1; i--)
		    {
		        if (data[i] > 0) { return i+1; }
		    }
		    return -1;	
	}
	
	public static String shortPrint(byte[] data)
	{
		ArrayList<String> content = new ArrayList<String>();
		return Arrays.toString(content.toArray(new String[content.size()]));
	}
	public static int getBlockNumber(byte[] packet)
	{
		return byteToInt(Arrays.copyOfRange(packet, 2, 4));
	}
	
	public static int getMode(byte[] packet)
	{
		return byteToInt(Arrays.copyOfRange(packet, 0, 2));
	}
	
	public static byte[] convertStringToBytes(String content)
	{
		return content.getBytes(); 
	}
	
	public static String convertBytesToString(byte[] content)
	{
		return new String(content);
	}
	
	public static File assambleFile(byte[][] data, String fileName)
	{
		int amountBytes=0;
		for(int i=0; i<data.length; i++)
		{
			amountBytes += data[i].length-4;
		}
		System.out.println("Length: "+amountBytes);
		
		byte[] currentP;
		byte[] msg = new byte[amountBytes];
		int j = 0;
		for(int i=0; i<data.length; i++)
		{
			currentP = data[i];
			currentP = Arrays.copyOfRange(currentP, 4, currentP.length);
			System.arraycopy(data[i], 4, msg, j, data[i].length-4);
			j += data[i].length-4;
		}
		System.out.println("File text content: " +Helper.convertBytesToString(msg));
		return toFile(msg, fileName);
		
	}
	
	public static Byte[][] byteToByte( byte[][] bt)
	{	
		byte[] bytes;
		Byte[] byteObjects;
		
		ArrayList<ArrayList<Byte>> myList = new ArrayList<ArrayList<Byte>>();

		
		for(int i=0; i<bt.length; i++)
		{
			bytes = new byte[ bt[i].length ];
			byteObjects = new Byte[bytes.length];
 
			for( int j=0; j<bytes.length; j++)
			{
				byteObjects[j] = new Byte(bt[i][j]);
			}
			ArrayList<Byte> op = new ArrayList<>(Arrays.asList(byteObjects));
			myList.add(op);
		}
		
		Byte[][] byteArray = myList.stream().map(u -> u.toArray(new Byte[0])).toArray(Byte[][]::new);
		return byteArray;

	}
	
	public static String byteMatrixToString( byte[][] data)
	{
		Byte[][] test = byteToByte(data);
		Byte[] saver;
		
		if( data.length == 0)
			return "Empty package";
		if(data.length == 1)
			return Arrays.toString(data[0]);
		
			saver = concatenate(Arrays.copyOfRange(test[0], 4, test[0].length), Arrays.copyOfRange(test[1], 4, test[1].length));
		if( data.length == 2)
			return Arrays.toString(saver);
					
		for(int i=2; i<test.length; i++)
			saver=concatenate(saver, Arrays.copyOfRange(test[i], 4, test[i].length));		

		return Arrays.toString(saver);	
	}
	
	public static  <T> T[] concatenate(T[] a, T[] b) {
	    int aLen = a.length;
	    int bLen = b.length;

	    @SuppressWarnings("unchecked")
	    T[] c = (T[]) Array.newInstance(a.getClass().getComponentType(), aLen + bLen);
	    System.arraycopy(a, 0, c, 0, aLen);
	    System.arraycopy(b, 0, c, aLen, bLen);

	    return c;
	}
	
	public static File toFile(byte[][] data, String fileName)
	{
		String msg = "";
		byte[] fileContent = new byte[0];
		for(int i=0; i<data.length; i++)
		{
			fileContent = data[i];
			fileContent = Arrays.copyOfRange(fileContent, 4, fileContent.length);
			msg += convertBytesToString(fileContent);
		}
		return toFile(msg, fileName );
	}
	
	
	public static int byteToInt(byte[] b)
	{
		ByteBuffer wrapped = ByteBuffer.wrap(b);
		short num = wrapped.getShort();
		return (int)num;	
	}
	
	public static byte[] intToByte(int i)
	{
		short num = (short)i;
		ByteBuffer dbuf = ByteBuffer.allocate(2);
		dbuf.putShort(num);
		return dbuf.array();
	}
	
	public static File toFile( String fileContent, String fileName )
	{
		return toFile( convertStringToBytes(fileContent), fileName);
	}
	
	public static File toFile( byte[] fileContent, String fileName )
	{	
		try { return Files.write(Paths.get(fileName).toAbsolutePath(), fileContent, StandardOpenOption.CREATE, StandardOpenOption.APPEND).toFile(); }
		catch (IOException e) {System.err.println("cant create"); return null;}	
	}
	
	public static File toNewFile( byte[] fileContent, String fileName )
	{	
		Random r = new Random();
		int result = r.nextInt(90) + 10;
		String st = fileName + Integer.toString(result) ;
		try { return Files.write(Paths.get(fileName).toAbsolutePath(), fileContent, StandardOpenOption.CREATE, StandardOpenOption.APPEND).toFile(); }
		catch (IOException e) {System.err.println("cant create"); return null;}	
	}
	
	public static String getContent(File f) throws FileNotFoundException
	{
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(f);
		try
		{
			if(f.getName().endsWith(".jpg"))
			{
				Desktop d = Desktop.getDesktop();
				d.open(f);
			}		
			sc = new Scanner(f);
		}
		catch(Exception e) {}
		
		String data = "";
		while(sc.hasNextLine())
			data = data + sc.nextLine();
		return data;	
	}
	
	public static byte[] fileToBytes( File f )
	{
		try { return Files.readAllBytes(f.toPath());}
		catch (IOException e) {return null;}
	}
	
	public static Byte[] byteByte(byte[] data)
	{
		Byte[] byteObjects = new Byte[data.length];
		int i=0;    
		for(byte b: data)
		   byteObjects[i++] = b;
		return byteObjects;
	}
	
	public static byte[] Bytebyte(Byte[] data)
	{
		byte[] bytes = new byte[data.length];
		int i=0;    
		for(Byte b: data)
		    bytes[i++] = b.byteValue();
		return bytes;
	}
	
	public static byte[][] matrixBytebyte(Byte[][] data)
	{
		byte[][] returnable = new byte[data.length][];			
		for(int i=0; i<data.length; i++)
			returnable[i]=Helper.Bytebyte(data[i]);
		return returnable;
	}
	
	public String randomizeFileName( String fileName )
	{
		Random r = new Random();
		int result = r.nextInt(90) + 10;
		return fileName.substring(0, fileName.lastIndexOf(".")) + Integer.toString(result) + fileName.substring(fileName.lastIndexOf("."), fileName.length());
	}
	
}
