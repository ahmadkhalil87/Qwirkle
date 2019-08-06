package qwirkle.Desktop.Communication.Messages.gameMessages;

import qwirkle.Desktop.Communication.Messages.message_abstract.Message;

public class TotalTimeRequest extends Message {

	public static final int uniqueID = 421;
	
	public TotalTimeRequest() {
		super(uniqueID);
	}
}
