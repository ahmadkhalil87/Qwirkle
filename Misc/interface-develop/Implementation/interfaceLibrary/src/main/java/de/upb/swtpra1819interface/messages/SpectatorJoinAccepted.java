package de.upb.swtpra1819interface.messages;

public class SpectatorJoinAccepted extends Message {
	
	private int gameID;
	
	public static final int uniqueID = 305;
	
	public SpectatorJoinAccepted(int gameID) {
		super(uniqueID);
		this.gameID = gameID;
	}

	public int getGameID() {
		return gameID;
	}

	public void setGameID(int gameID) {
		this.gameID = gameID;
	}

}
