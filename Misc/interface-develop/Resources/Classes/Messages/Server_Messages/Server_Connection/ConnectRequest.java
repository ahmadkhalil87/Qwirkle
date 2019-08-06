public class ConnectRequest extends Message {
	private String playerName;
	
	public ConnectRequest(String playerName) {
		this.setUniqueID(100);
		this.playerName = playerName;
	}
}
