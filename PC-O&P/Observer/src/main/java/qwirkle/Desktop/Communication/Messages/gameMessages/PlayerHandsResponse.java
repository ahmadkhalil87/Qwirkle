package qwirkle.Desktop.Communication.Messages.gameMessages;

import java.util.Map;

import qwirkle.Desktop.Communication.Client;
import qwirkle.Desktop.Communication.Messages.message_abstract.Message;
import qwirkle.Desktop.entity.Tile;

import java.util.ArrayList;
import java.util.Collection;

public class PlayerHandsResponse extends Message {
	
	private Map<Client, Collection<Tile>> hands;
	
	public static final int uniqueID = 426;
	
	public PlayerHandsResponse(Map<Client, Collection<Tile>> hands) {
		super(uniqueID);
		this.hands = hands;
	}

	public Map<Client, Collection<Tile>> getHands() {
		return hands;
	}

	public void setHands(Map<Client, Collection<Tile>> hands) {
		this.hands = hands;
	}

}
