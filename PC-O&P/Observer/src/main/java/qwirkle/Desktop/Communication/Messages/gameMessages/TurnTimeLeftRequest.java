package qwirkle.Desktop.Communication.Messages.gameMessages;

import qwirkle.Desktop.Communication.Messages.message_abstract.Message;

public class TurnTimeLeftRequest extends Message {

	public static final int uniqueID = 419;
	
	public TurnTimeLeftRequest() {
		super(uniqueID);
	}

}
