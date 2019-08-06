package de.upb.swtpra1819interface.messages;

public class NotAllowed extends Error {

	public static final int uniqueID = 920;
	
	public NotAllowed(String message) {
		super(uniqueID,message);
	}
	
}
