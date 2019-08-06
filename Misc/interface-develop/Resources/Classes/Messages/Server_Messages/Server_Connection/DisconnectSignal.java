public class DisconnectSignal extends Message {
	private String reason;
	private int playerID;
	private String playerName;
	private int playerState;
	
	public DisconnectSignal(String reason, int playerID, String playerName, int playerState) {
		this.setUniqueID(202);
		this.reason = reason;
		this.playerID = playerID;
		this.playerName = playerName;
		this.playerState = playerState;
	}
}
