package qwirkle.Desktop.Communication.Messages.Exceptions;


public class AccessDenied extends Error {

	public static final int uniqueID = 900;
	
	public AccessDenied(String message){
		super(uniqueID,message);
	}
	
}
