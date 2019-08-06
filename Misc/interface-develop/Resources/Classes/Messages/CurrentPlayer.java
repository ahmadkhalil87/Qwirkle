public class CurrentPlayer extends Message {
	private int playerID;
	
	public CurrentPlayer(int playerID) {
		this.setUniqueID(409);
		this.playerID = playerID;
	}
}
