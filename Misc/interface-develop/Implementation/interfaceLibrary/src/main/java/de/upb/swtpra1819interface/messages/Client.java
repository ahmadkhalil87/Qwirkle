package de.upb.swtpra1819interface.messages;

public class Client {

	private int clientID;
	private String clientName;
	private ClientType clientType;

	public Client(int clientID, String clientName, ClientType clientType) {
		this.clientID = clientID;
		this.clientName = clientName;
		this.clientType = clientType;
	}

	public int getClientID() {
		return clientID;
	}

	public void setClientID(int clientID) {
		this.clientID = clientID;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public ClientType getClientType() {
		return clientType;
	}

	public void setClientType(ClientType clientType) {
		this.clientType = clientType;
	}
}