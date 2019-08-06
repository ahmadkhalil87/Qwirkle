package qwirkle.Desktop.Communication.Messages.gameMessages;

import java.util.ArrayList;



import qwirkle.Desktop.Communication.Messages.message_abstract.Message;
import qwirkle.Desktop.entity.Tile;

public class TileSwapRequest extends Message {

	private ArrayList<Tile> tiles;
	
	public static final int uniqueID = 411;

	public TileSwapRequest(ArrayList<Tile> tiles) {
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
