public class TurnTimeLeftResponse extends Message {
	private long time;
	
	public TurnTimeLeftResponse(long time) {
		this.setUniqueID(420);
		this.time = time;
	}
}
