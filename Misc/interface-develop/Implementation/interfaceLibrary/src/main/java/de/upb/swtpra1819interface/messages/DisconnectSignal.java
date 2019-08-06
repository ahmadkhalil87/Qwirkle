package de.upb.swtpra1819interface.messages;

public class DisconnectSignal extends Message {
	
	private String reason;
	private Client client;

	public static final int uniqueID = 200;
	
	public DisconnectSignal(String reason, Client client) {
		super(uniqueID);
		this.reason = reason;
		this.client = client;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

}
