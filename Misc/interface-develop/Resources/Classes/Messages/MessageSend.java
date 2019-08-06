public class MessageSend extends Message {
	private String message;
	
	public MessageSend(String message) {
		this.setUniqueID(306);
		this.message = message;
	}
}
