package qwirkle.Desktop.Communication.Messages.gameMessages;

import java.util.HashMap;

import qwirkle.Desktop.Communication.Client;
import qwirkle.Desktop.Communication.Messages.message_abstract.Message;

public class Winner extends Message {
	
	private Client client;
	private int score;
	private HashMap<Client, Integer> leaderboard;
	
	public Winner(Client client, int score, HashMap<Client, Integer> leaderboard) {
		super(407);
		this.client = client;
		this.score = score;
		this.leaderboard = leaderboard;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public HashMap<Client, Integer> getLeaderboard() {
		return leaderboard;
	}

	public void setLeaderboard(HashMap<Client, Integer> leaderboard) {
		this.leaderboard = leaderboard;
	}

}
