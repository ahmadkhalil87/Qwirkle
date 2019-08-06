package qwirkle.Desktop.Communication.Messages.Exceptions;



public class NotAllowed extends Error {

	public static final int uniqueID = 920;
	
	public NotAllowed(String message) {
		super(uniqueID,message);
	}
	
}
