package de.upb.swtpra1819interface.messages;

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
