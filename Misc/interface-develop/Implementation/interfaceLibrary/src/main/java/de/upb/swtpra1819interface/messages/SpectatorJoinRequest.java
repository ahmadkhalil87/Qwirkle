package de.upb.swtpra1819interface.messages;

public class SpectatorJoinRequest extends Message {

	private int gameID;
	
	public static final int uniqueID = 304;

	public SpectatorJoinRequest(int gameID) {
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
