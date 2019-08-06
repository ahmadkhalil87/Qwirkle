public class SpectatorJoinRequest extends Message {
	private int gameID;
	
	public SpectatorJoinRequest(int gameID) {
		this.setUniqueID(304);
		this.gameID = gameID;
	}
}
