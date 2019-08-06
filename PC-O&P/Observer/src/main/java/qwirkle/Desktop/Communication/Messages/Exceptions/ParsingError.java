package qwirkle.Desktop.Communication.Messages.Exceptions;


public class ParsingError extends Error {

	public static final int uniqueID = 910;
	
	public ParsingError(String message) {
		super(uniqueID,message);
	}

}
