package de.upb.swtpra1819interface.messages;

public class LeavingPlayer extends Message {
	
	private Client client;
	
	public static final int uniqueID = 406;
	
	public LeavingPlayer(Client client) {
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
