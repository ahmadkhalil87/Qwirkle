public class TotalTimeResponse extends Message {
	private long time;
	
	public TotalTimeResponse(long time) {
		this.setUniqueID(422);
		this.time = time;
	}
}
