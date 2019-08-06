package de.upb.swtpra1819interface.messages;

public abstract class Error extends Message {

	private String message;
	
	public Error(int uniqueID, String message) {
		super(uniqueID);
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	

}
