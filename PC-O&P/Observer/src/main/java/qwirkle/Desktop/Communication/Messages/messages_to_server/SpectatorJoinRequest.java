package qwirkle.Desktop.Communication.Messages.messages_to_server;

import qwirkle.Desktop.Communication.Messages.message_abstract.Message;

public class SpectatorJoinRequest extends Message {

	private int gameId;
	
	public static final int uniqueID = 304;

	public SpectatorJoinRequest(int gameID) {
		super(uniqueID);
		this.gameId = gameID;
	}

	public int getGameID() {
		return this.gameId;
	}

	public void setGameID(int gameID) {
		this.gameId = gameID;
	}
}
