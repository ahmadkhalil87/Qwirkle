package qwirkle.Desktop.Communication.Messages.gameMessages;

import qwirkle.Desktop.Communication.Messages.message_abstract.Message;

public class ScoreRequest extends Message {

	public static final int uniqueID = 417;
	
	public ScoreRequest() {
		super(uniqueID);
	}

}
