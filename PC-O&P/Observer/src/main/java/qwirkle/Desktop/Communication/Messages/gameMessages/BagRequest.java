package qwirkle.Desktop.Communication.Messages.gameMessages;

import qwirkle.Desktop.Communication.Messages.message_abstract.Message;

public class BagRequest extends Message {

	public static final int uniqueID = 423;
	
	public BagRequest() {
		super(uniqueID);
	}

}
