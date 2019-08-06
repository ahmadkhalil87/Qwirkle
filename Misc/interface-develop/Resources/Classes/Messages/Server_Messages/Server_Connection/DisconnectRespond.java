public class DisconnectRespond extends Message {
	private boolean accepted;
	private String reason;
	
	public DisconnectRespond(boolean accepted, String reason) {
		this.setUniqueID(201);
		this.accepted = accepted;
		this.reason = reason;
	}
}
