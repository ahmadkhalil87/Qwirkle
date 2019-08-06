package qwirkle.Desktop.Communication.Messages.message_abstract;

import qwirkle.Desktop.Communication.Client;

/**
 * This Class is very important.
 * It is used to Create an Object that can transfer information about the 
 * Message and to which output to respond to.
 * 
 * @author Lukas
 *
 */
public class ClientMessage {
	
	
	Message message;
	Client client;
	
	/**
	 * The Constructor
	 * @param client
	 * @param msg
	 */
	public ClientMessage (Client client, Message msg) {
		this.client = (client);
		this.message = msg;
	}
	
	public Message getClientMessage() {
		return message;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	
	
}
