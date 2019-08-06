package qwirkle.Desktop.Communication.Messages.gameMessages;

import java.util.ArrayList;



import qwirkle.Desktop.Communication.Messages.message_abstract.Message;
import qwirkle.Desktop.entity.Tile;

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
