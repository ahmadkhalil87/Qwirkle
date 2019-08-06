package de.upb.swtpra1819interface.messages;

import java.util.ArrayList;

public class SendTiles extends Message {
	
	private ArrayList<Tile> tiles;
	
	public static final int uniqueID = 410;
	
	public SendTiles(ArrayList<Tile> tiles) {
		super(uniqueID);
		this.tiles = tiles;
	}

	public ArrayList<Tile> getTiles() {
		return tiles;
	}

	public void setTiles(ArrayList<Tile> tiles) {
		this.tiles = tiles;
	}

}
