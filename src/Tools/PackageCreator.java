package Tools;

import java.io.File;
import java.util.Arrays;

public class PackageCreator 
{
	public static byte[] createRRQPacket(final String fileName)
	{
		//OPCode for the RRQ is 1
		byte[] opCode = {0, 1};
		//Get the Bytes of the fileName
		byte[] fileNameInBytes = fileName.getBytes();
		//For this project you only need to use the mode "netascii"
		byte[] modeInBytes = "netascii".getBytes();
		
		//Create an Index for filling the rrq-array
		int rrqIndex = 0;
		//Calculate the length of the array to store the information in. 2 = length of OPCode, 1+1 = length of two zero-bytes
		int rrqLength = 2 + fileName.length() + 1 + modeInBytes.length + 1;
		
		//Create an array for the rrq
		byte[] rrq = new byte[rrqLength];
		
		//Copy the opCode into the rrq-message-array
		System.arraycopy(opCode, 0, rrq, rrqIndex, opCode.length);
		rrqIndex += 2;
		
		//Copy the fileName into the rrq-message-array
		System.arraycopy(fileNameInBytes, 0, rrq, rrqIndex, fileNameInBytes.length);
		rrqIndex += fileNameInBytes.length;
		
		//Copy the zero-byte (used to differ between fileName and mode) into the rrq-message-array
		rrq[rrqIndex++] = 0;
		
		//Copy the mode into the rrq-message-array
		System.arraycopy(modeInBytes, 0, rrq, rrqIndex, modeInBytes.length);
		rrqIndex += modeInBytes.length;
		
		//Copy the second zero-byte (used to indicate the end of the message) into the rrq-message-array
		rrq[rrqIndex] = 0;
		
		//Return the created Array
		return rrq;
	}
	
	/** Creates a WRQ-packet as specified by RFC 1350.
	 * @param fileName The name of the file.
	 * @return Returns an array which contains the data for a WRQ-packet.
	 */
	
	public static byte[] createWRQPacket(final String fileName)
	{
		//OPCode for the RRQ is 2
		byte[] opCode = {0, 2};		
		//Get the Bytes of the fileName
		byte[] fileNameInBytes = fileName.getBytes();
		//For this project you only need to use the mode "netascii"
		byte[] modeInBytes = "netascii".getBytes();
		
		//Create an Index for filling the wrq-array
		int rrqIndex = 0;
		//Calculate the length of the array to store the information in. 2 = length of OPCode, 1+1 = length of two zero-bytes
		int rrqLength = 2 + fileName.length() + 1 + modeInBytes.length + 1;
		
		//Create an array for the rrq
		byte[] rrq = new byte[rrqLength];
		
		//Copy the opCode into the rrq-message-array
		System.arraycopy(opCode, 0, rrq, rrqIndex, opCode.length);
		rrqIndex += 2;
		
		//Copy the fileName into the rrq-message-array
		System.arraycopy(fileNameInBytes, 0, rrq, rrqIndex, fileNameInBytes.length);
		rrqIndex += fileNameInBytes.length;
		
		//Copy the zero-byte (used to differ between fileName and mode) into the rrq-message-array
		rrq[rrqIndex++] = 0;
		
		//Copy the mode into the rrq-message-array
		System.arraycopy(modeInBytes, 0, rrq, rrqIndex, modeInBytes.length);
		rrqIndex += modeInBytes.length;
		
		//Copy the second zero-byte (used to indicate the end of the message) into the rrq-message-array
		rrq[rrqIndex] = 0;
		
		//Return the created Array
		return rrq;	
	}
	
	public static byte[][] createDataPacket(File f, int blockNumber)
	{
		return createDataPacket(Helper.fileToBytes(f), blockNumber);
	}
	
	public static byte[][] createDataPacket(String fileName, int blockNumber)
	{
		return createDataPacket(Helper.convertStringToBytes(fileName), blockNumber);
	}
	
	/** Creates a Data-packet as specified by RFC 1350.
	 * @param fileName The name of the file.
	 * @return Returns an array which contains the arrays for the content.
	 */
	private static byte[][] createDataPacket(byte[] f, int blockNumber)
	{
		byte[] blockNumberByte = Helper.intToByte(blockNumber);
		byte[] data = f;
		byte[] opCode = {0, 3};
		byte[] content;		
		int contentIndex = 0;
		int amountPackages;
		
		//Calculate the amount of data messages to be created
		amountPackages = (data.length > 512) ? (data.length / 512)+1: 1; 
		//Create the array of packages to send
		byte[][] returnable = new byte[amountPackages][];
		
		//Build as many arrays as there are packages
		for(int i=0; i<amountPackages; i++)
		{
			//Message length based on Data length
			if( data.length >=512 )
				//Length of a full message 
				content = new byte[2+2+512];
			else
				//Length of the last message
				content = new byte[2+2+data.length];
				
			//Copy the opCode into the data-message-array
			System.arraycopy(opCode, 0, content, contentIndex, opCode.length);
			contentIndex += 2;
			
			//Copy the block number into the data-message-array
			System.arraycopy(Helper.intToByte(Helper.byteToInt(blockNumberByte)+i), 0, content, contentIndex, blockNumberByte.length);
			contentIndex += 2;
			
			//Copy the data content that is left to into a data-message-array
			System.arraycopy(Arrays.copyOfRange(data, 0, (data.length<512) ? data.length : 512) , 0, content, contentIndex, (data.length<512) ? data.length : 512);
			contentIndex += data.length;
			
			//Shorten the data that is left to put in a packet
			data = Arrays.copyOfRange(data, (data.length>512) ? 512 : data.length, data.length);
			//fill the package array
			returnable[i]=content;
			//reset the index
			contentIndex=0;
		}
		return returnable;
	}
	
	/** Creates an ACK-packet as specified by RFC 1350.
	 * @return Returns an array which contains an ACK-packet.
	 */
	public static byte[] createACKPacket(final int blockNumber)
	{
		//OPCode for the RRQ is 4
		byte[] opCode = {0, 4};
		
		//Get the Bytes of the block number
		byte[] blockNumberBytes = Helper.intToByte(blockNumber);
		
		//Create an Index for filling the ACK-array
		int ackIndex = 0;
		int ackLenght = 4;
		
		//Create an array for the ACK
		byte[] ack = new byte[ackLenght];
		
		//Copy the opCode into the ack-message-array
		System.arraycopy(opCode, 0, ack, ackIndex, opCode.length);
		ackIndex += 2;
		
		//Copy the block number into the ack-message-array
		System.arraycopy(blockNumberBytes, 0, ack, ackIndex, blockNumberBytes.length);
		ackIndex += 2;
		
		return ack;
	}
	
	/** Creates an Error-packet as specified by RFC 1350.
	 * @return Returns an array which contains the error message for an Error-packet.
	 */
	public static byte[] createErrorPacket(final String errorMessage, int blockNumber)
	{
			//OPCode for the error is 5
			byte[] opCode = {0, 5};		
			
			//Get the Bytes of the block number
			byte[] errorNumberBytes = Helper.intToByte(blockNumber);
			
			//Get the Bytes of the errorMessage
			byte[] errorMessageBytes = errorMessage.getBytes();
			
			//Create an Index for filling the error-array
			int errorIndex = 0;
			//Calculate the length of the array to store the information in. 2 = length of OPCode, 2 = length error Number, n = length of the message, 1 = Last zero
			int errorLength = 2 + 2 + errorMessage.length() + 1;
			
			//Create an array for the error
			byte[] error = new byte[errorLength];
			
			//Copy the opCode into the error-message-array
			System.arraycopy(opCode, 0, error, errorIndex, opCode.length);
			errorIndex += 2;
			
			//Copy the error number into the error-message-array
			System.arraycopy(errorNumberBytes, 0, error, errorIndex, errorNumberBytes.length);
			errorIndex += 2;
			
			//Copy the error message into the error-message-array
			System.arraycopy(errorMessageBytes, 0, error, errorIndex, errorMessageBytes.length);
			errorIndex += errorMessageBytes.length;
			
			//Return the created Array
			return error;	
	}

}
