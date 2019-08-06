package qwirkle.Desktop.Communication.Messages.messages_to_server;

import qwirkle.Desktop.Communication.Client;
import qwirkle.Desktop.Communication.Messages.message_abstract.Message;

public class MessageSend extends Message {

	private String message;
	private Client client;
	
	public static final int uniqueID = 306;

	public MessageSend(String message, Client client) {
		super(uniqueID);
		this.message = message;
		this.client = client;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public Client getClient() {
		return this.client;
	}
	
}
