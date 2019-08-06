package qwirkle.Desktop.Communication.Messages.gameMessages;

import qwirkle.Desktop.Communication.Messages.message_abstract.Message;

public class EndGame extends Message {
	
	public static final int uniqueID = 401;
	
	public EndGame() {
		super(uniqueID);
	}

}
