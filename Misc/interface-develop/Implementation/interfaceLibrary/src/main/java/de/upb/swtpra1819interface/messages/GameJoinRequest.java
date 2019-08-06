package de.upb.swtpra1819interface.messages;

public class GameJoinRequest extends Message {

	private int gameID;
	
	public static final int uniqueID = 302;

	public GameJoinRequest(int gameID) {
		super(uniqueID);
		this.gameID = gameID;
	}

	public int getGameID() {
		return this.gameID;
	}

	public void setGameID(int gameID) {
		this.gameID = gameID;
	}
}
