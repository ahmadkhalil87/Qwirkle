package qwirkle.Desktop.Communication.Messages.messages_from_server;

import qwirkle.Desktop.Communication.Messages.message_abstract.Message;

public class GameJoinAccepted extends Message {
	
	private int gameID;
	
	public static final int uniqueID = 303;
	
	public GameJoinAccepted(int gameID) {	
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
