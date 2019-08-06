package de.upb.swtpra1819interface.messages;

public class AccessDenied extends Error {

	public static final int uniqueID = 900;
	
	public AccessDenied(String message){
		super(uniqueID,message);
	}
	
}
