package qwirkle.Desktop.Communication.Messages.messages_to_server;

import qwirkle.Desktop.Communication.Messages.message_abstract.Message;

public class GameListRequest extends Message {

	public static final int uniqueID = 300;
	
	public GameListRequest() {
		super(uniqueID);
	}

}
