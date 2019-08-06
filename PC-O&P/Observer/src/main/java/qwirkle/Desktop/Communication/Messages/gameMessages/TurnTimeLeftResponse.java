package qwirkle.Desktop.Communication.Messages.gameMessages;

import qwirkle.Desktop.Communication.Messages.message_abstract.Message;

public class TurnTimeLeftResponse extends Message {
	
	private long time;
	
	public static final int uniqueID = 420;
	
	public TurnTimeLeftResponse(long time) {
		super(uniqueID);
		this.time = time;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

}
