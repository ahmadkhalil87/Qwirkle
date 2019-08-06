package qwirkle.Desktop.Communication.Messages.gameMessages;

import qwirkle.Desktop.Communication.Messages.message_abstract.Message;

public class LeavingRequest extends Message {

	public static final int uniqueID = 405;
	
	public LeavingRequest() {
		super(uniqueID);
	}

}
