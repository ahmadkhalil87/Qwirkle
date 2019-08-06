package qwirkle.Desktop.Communication.Messages.gameMessages;

import qwirkle.Desktop.Communication.Messages.message_abstract.Message;

public class ResumeGame extends Message {
	
	public static final int uniqueID = 404;
	
	public ResumeGame() {
		super(uniqueID);
	}

}
