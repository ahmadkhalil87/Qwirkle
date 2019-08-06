package de.upb.swtpra1819interface.messages;

public class ConnectAccepted extends Message {

	private int clientID;
	
	public static final int uniqueID = 101;
	
	public ConnectAccepted(int playerID) {
		super(uniqueID);
		this.clientID = playerID;
	}

	public int getClientID() {
		return clientID;
	}

	public void setClientID(int clientID) {
		this.clientID = clientID;
	}


}
