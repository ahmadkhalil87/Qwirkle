package de.upb.swtpra1819interface.messages;

import java.util.ArrayList;

public class StartTiles extends Message {
	
	private ArrayList<Tile> tiles;
	
	public static final int uniqueID = 408;
	
	public StartTiles(ArrayList<Tile> tiles) {
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
