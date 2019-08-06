package de.upb.swtpra1819interface.messages;

import java.util.List;

public class PlayTiles extends Message {

	private List<TileOnPosition> tiles;
	
	public static final int uniqueID = 414;

	public PlayTiles(List<TileOnPosition> tiles) {
		super(uniqueID);
		this.tiles = tiles;
	}

	public List<TileOnPosition> getTiles() {
		return tiles;
	}

	public void setTiles(List<TileOnPosition> tiles) {
		this.tiles = tiles;
	}


}
