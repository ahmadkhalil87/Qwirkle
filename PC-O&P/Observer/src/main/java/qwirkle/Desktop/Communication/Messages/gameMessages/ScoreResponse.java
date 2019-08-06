package qwirkle.Desktop.Communication.Messages.gameMessages;

import java.util.HashMap;

import qwirkle.Desktop.Communication.Client;
import qwirkle.Desktop.Communication.Messages.message_abstract.Message;

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
