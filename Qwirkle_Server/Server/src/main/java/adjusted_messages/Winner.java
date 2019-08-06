package adjusted_messages;

import java.util.Map;

import client.Client;
import de.upb.swtpra1819interface.messages.Message;

public class Winner extends Message {
	
	private Client client;
	private int score;
	private Map<Client, Integer> leaderboard;
	
	public Winner(Client client, int score, Map<Client, Integer> leaderboard) {
		super(407);
		this.client = client;
		this.score = score;
		this.leaderboard = leaderboard;
	}

	public Client getClient() {
		return client;
	}

	public int getScore() {
		return score;
	}

	public Map<Client, Integer> getLeaderboard() {
		return leaderboard;
	}

}
