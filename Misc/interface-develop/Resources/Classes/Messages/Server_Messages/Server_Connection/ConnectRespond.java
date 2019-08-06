public class ConnectRespond extends Message {
	private int playerID;
	private boolean accepted;
	private String reason;
	
	public ConnectRespond(int playerID, boolean accepted, String reason) {
		this.setUniqueID(101);
		this.playerID = playerID;
		this.accepted = accepted;
		this.reason = reason;
	}
}
