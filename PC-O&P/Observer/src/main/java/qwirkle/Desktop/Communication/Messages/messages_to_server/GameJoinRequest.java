package qwirkle.Desktop.Communication.Messages.messages_to_server;

import qwirkle.Desktop.Communication.Messages.message_abstract.Message;

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
