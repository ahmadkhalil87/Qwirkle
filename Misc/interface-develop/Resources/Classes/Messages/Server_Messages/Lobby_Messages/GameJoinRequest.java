public class GameJoinRequest extends Message {
	private int gameID;
	
	public GameJoinRequest(int gameID) {
		this.setUniqueID(302);
		this.gameID = gameID;
	}
}
