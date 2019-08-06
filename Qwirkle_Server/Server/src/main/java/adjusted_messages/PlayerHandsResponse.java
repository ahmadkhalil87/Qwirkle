package adjusted_messages;

import java.util.Map;

import client.Client;
import de.upb.swtpra1819interface.messages.Message;
import de.upb.swtpra1819interface.models.Tile;

import java.util.ArrayList;

public class PlayerHandsResponse extends Message {
	
	private Map<Client, ArrayList<Tile>> hands;
	
	public static final int uniqueID = 426;
	
	public PlayerHandsResponse(Map<Client, ArrayList<Tile>> map) {
		super(uniqueID);
		this.hands = map;
	}

	public Map<Client, ArrayList<Tile>> getHands() {
		return hands;
	}
}
