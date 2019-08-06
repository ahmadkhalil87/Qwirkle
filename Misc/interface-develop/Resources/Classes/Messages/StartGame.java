import java.util.ArrayList;
public class StartGame extends Message {
	private Configuration config;
	private ArrayList<Integer> playerIDs;
	
	public StartGame(Configuration config, ArrayList<Integer> playerIDs) {
		this.setUniqueID(400);
		this.config = config;
		this.playerIDs = playerIDs;
	}
}