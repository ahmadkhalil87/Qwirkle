package de.upb.swtpra1819interface.messages;

public class MessageSend extends Message {

	private String message;
	
	public static final int uniqueID = 306;

	public MessageSend(String message) {
		super(uniqueID);
		this.message = message;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
