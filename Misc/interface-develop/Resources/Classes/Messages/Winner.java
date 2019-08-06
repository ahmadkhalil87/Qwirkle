import java.util.HashMap;
public class Winner extends Message {
	private int playerID;
	private int score;
	private HashMap<Integer, Integer> leaderboard;
	
	public Winner(int playerID, int score, HashMap<Integer, Integer> leaderboard) {
		this.setUniqueID(407);
		this.playerID = playerID;
		this.score = score;
		this.leaderboard = leaderboard;
	}
}
