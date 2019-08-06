public class SpectatorJoinResponse extends Message {
	private int gameID;
	private boolean accepted;
	private String reason;
	
	public SpectatorJoinResponse(int gameID, boolean accepted, String reason) {
		this.setUniqueID(305);
		this.gameID = gameID;
		this.accepted = accepted;
		this.reason = reason;
	}
}
