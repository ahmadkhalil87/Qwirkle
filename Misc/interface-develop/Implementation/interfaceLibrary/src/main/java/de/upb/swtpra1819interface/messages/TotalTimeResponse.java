package de.upb.swtpra1819interface.messages;

public class TotalTimeResponse extends Message {
	
	private long time;
	
	public static final int uniqueID = 422;
	
	public TotalTimeResponse(long time) {
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
