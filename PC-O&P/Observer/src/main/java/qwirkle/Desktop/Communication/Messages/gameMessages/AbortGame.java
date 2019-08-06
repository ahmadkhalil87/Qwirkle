package qwirkle.Desktop.Communication.Messages.gameMessages;

import qwirkle.Desktop.Communication.Messages.message_abstract.Message;

public class AbortGame extends Message {

	public static final int uniqueID = 402;
	
	public AbortGame() {
		super(uniqueID);
	}
}
