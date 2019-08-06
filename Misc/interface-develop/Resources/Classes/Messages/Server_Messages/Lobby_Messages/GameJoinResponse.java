public class GameJoinResponse extends Message {
	private int gameID;
	private boolean accepted;
	private String reason;
	
	public GameJoinResponse(int gameID, boolean accepted, String reason) {
		this.setUniqueID(303);
		this.gameID = gameID;
		this.accepted = accepted;
		this.reason = reason;
	}
}
