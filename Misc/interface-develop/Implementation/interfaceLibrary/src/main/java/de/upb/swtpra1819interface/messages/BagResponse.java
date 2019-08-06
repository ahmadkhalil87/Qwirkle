package de.upb.swtpra1819interface.messages;

import java.util.ArrayList;

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
