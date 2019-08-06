public class LeavingPlayer extends Message {
	private int playerID;
	
	public LeavingPlayer(int playerID) {
		this.setUniqueID(406);
		this.playerID = playerID;
	}
}
