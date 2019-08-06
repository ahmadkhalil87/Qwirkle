public class DisconnectRequest extends Message {
	private String reason;
	
	public DisconnectRequest(String reason) {
		this.setUniqueID(200);
		this.reason = reason;
	}
}
