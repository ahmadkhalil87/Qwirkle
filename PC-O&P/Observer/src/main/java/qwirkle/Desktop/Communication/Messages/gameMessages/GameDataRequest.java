package qwirkle.Desktop.Communication.Messages.gameMessages;

import qwirkle.Desktop.Communication.Messages.message_abstract.Message;

public class GameDataRequest extends Message {

	public static final int uniqueID = 498;
	
	public GameDataRequest() {
		super(uniqueID);
	}

}
