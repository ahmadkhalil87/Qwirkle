package de.upb.swtpra1819interface.messages;

import java.util.HashMap;

public class ScoreResponse extends Message {
	
	private HashMap<Client, Integer> scores;
	
	public static final int uniqueID = 418;
	
	public ScoreResponse(HashMap<Client, Integer> scores) {
		super(uniqueID);
		this.scores = scores;
	}

	public HashMap<Client, Integer> getScores() {
		return scores;
	}

	public void setScores(HashMap<Client, Integer> scores) {
		this.scores = scores;
	}

}
