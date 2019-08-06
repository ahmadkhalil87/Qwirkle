package qwirkle.Desktop.Communication.Messages.gameMessages;

import qwirkle.Desktop.Communication.Client;
import qwirkle.Desktop.Communication.Messages.message_abstract.Message;

public class CurrentPlayer extends Message {
	
	private Client client;
	
	public static final int uniqueID = 409;
	
	public CurrentPlayer(Client client) {
		super(uniqueID);
		this.client = client;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

}
