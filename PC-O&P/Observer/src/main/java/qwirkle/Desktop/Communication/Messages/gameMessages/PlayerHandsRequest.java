package qwirkle.Desktop.Communication.Messages.gameMessages;

import qwirkle.Desktop.Communication.Messages.message_abstract.Message;

public class PlayerHandsRequest extends Message {

	public static final int uniqueID = 425;
	
	public PlayerHandsRequest() {
		super(uniqueID);
	}

}
