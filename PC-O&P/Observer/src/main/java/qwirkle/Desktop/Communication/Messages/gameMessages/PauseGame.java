package qwirkle.Desktop.Communication.Messages.gameMessages;

import qwirkle.Desktop.Communication.Messages.message_abstract.Message;

public class PauseGame extends Message {
	
	public static final int uniqueID = 403;
	
	public PauseGame() {
		super(uniqueID);
	}

}
