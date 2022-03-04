package Tools;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Scanner;

public class ContainedFiles 
{
	private static Scanner sc;
	private ArrayList<File> dataFiles = new ArrayList<File>();
	public ContainedFiles(File... files)
	{
		this.addFile(new File("C:\\Users\\Philipp\\eclipse-workspace\\Data communication TFTP\\src\\smallFile.txt"));
		this.addFile(new File("C:\\Users\\Philipp\\eclipse-workspace\\Data communication TFTP\\src\\largeFile.txt"));
		this.addFile(new File("C:\\Users\\Philipp\\eclipse-workspace\\Data communication TFTP\\sea.jpg"));
		
		for(File i: files)
			dataFiles.add(i);
	}
	
	public static void main(String args[])
	{
		getContent(new File("C:\\Users\\Philipp\\eclipse-workspace\\Data communication TFTP\\Sea.jpg"));
	}
	
	public String availableFiles()
	{
		String files="";
		for(int i=0; i<dataFiles.size(); i++)
			files += i +": "+getName(dataFiles.get(i))+"\n";
		return files;
	}	
	
	public void addFile(File file)
	{
		dataFiles.add(file);
	}
	
	public File getFile(int index)
	{
		return dataFiles.get(index);
	}
	
	public File getFile(String name)
	{
		for(int i=0; i<dataFiles.size(); i++)
		{
			if(getName(dataFiles.get(i)).equals(name))
				return dataFiles.get(i);
		}
		return null;
	}
	
	public static String getName(File f)
	{
		if(f.exists())
			return f.getName();
		else
		return null;
	}
	
	
	public static String getContent(File f)
	{
		try
		{
			if(getName(f).endsWith(".jpg"))
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
	
	public static String readFile(File f, Charset encoding) throws IOException
	{
		byte[] encoded = Files.readAllBytes(f.toPath());
		return new String(encoded, encoding);
	}
	
	public static String readFile(File f) throws IOException
	{
		return readFile(f, Charset.defaultCharset());
	}
	
	public String readFile(String fileName)
	{
		try {return readFile(this.getFile(fileName));} 
		catch (IOException e) {return "File not found Exception: "+fileName;}
	}
	
	public String getContent(String fileName)
	{
		File f = (this.getFile(fileName));
		if( f==null ) 
			return null;
		try
		{
			if(getName(f).endsWith(".jpg"))
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

}
