package qwirkle.Desktop.Communication.Messages.messages_from_server;

import qwirkle.Desktop.Communication.Client;
import qwirkle.Desktop.Communication.Messages.message_abstract.Message;

public class MessageSignal extends Message {
	
	private String message;
	private Client client;
	
	public static final int uniqueID = 307;
	
	public MessageSignal(String message, Client client) {
		super(uniqueID);
		this.message = message;
		this.client = client;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

}
