package de.upb.swtpra1819interface.messages;

import java.util.HashMap;
import java.util.ArrayList;

public class PlayerHandsResponse extends Message {
	
	private HashMap<Client, ArrayList<Tile>> hands;
	
	public static final int uniqueID = 426;
	
	public PlayerHandsResponse(HashMap<Client, ArrayList<Tile>> hands) {
		super(uniqueID);
		this.hands = hands;
	}

	public HashMap<Client, ArrayList<Tile>> getHands() {
		return hands;
	}

	public void setHands(HashMap<Client, ArrayList<Tile>> hands) {
		this.hands = hands;
	}

}
