package qwirkle.Desktop.Communication.Messages.gameMessages;

import java.util.ArrayList;

import qwirkle.Desktop.Communication.Messages.message_abstract.Message;
import qwirkle.Desktop.entity.Tile;



public class BagResponse extends Message
{

	private ArrayList<Tile> bag;

	public static final int uniqueID = 424;
	
	public BagResponse(ArrayList<Tile> bag) {
		super(uniqueID);
		this.bag = bag;
	}

	public ArrayList<Tile> getBag() {
		return bag;
	}

	public void setBag(ArrayList<Tile> bag) {
		this.bag = bag;
	}
}
