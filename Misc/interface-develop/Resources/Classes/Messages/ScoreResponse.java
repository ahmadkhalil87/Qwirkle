import java.util.HashMap;
public class ScoreResponse extends Message {
	private HashMap<Integer, Integer> scores;
	
	public ScoreResponse(HashMap<Integer, Integer> scores) {
		this.setUniqueID(418);
		this.scores = scores;
	}
}
