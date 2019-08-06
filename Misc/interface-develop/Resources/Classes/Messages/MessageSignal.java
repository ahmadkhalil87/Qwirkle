public class MessageSignal extends Message {
	private String message;
	private int playerID;
	private String playerName;
	private int playerState;
	
	public MessageSignal(String message, int playerID, String playerName, int playerState) {
		this.setUniqueID(307);
		this.message = message;
		this.playerID = playerID;
		this.playerName = playerName;
		this.playerState = playerState;
	}
}
