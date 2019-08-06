package de.upb.swtpra1819interface.messages;

import java.util.ArrayList;

public class TileSwapResponse extends Message {
	
	private ArrayList<Tile> tiles;
	
	public static final int uniqueID = 413;
	
	public TileSwapResponse(ArrayList<Tile> tiles) {
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
