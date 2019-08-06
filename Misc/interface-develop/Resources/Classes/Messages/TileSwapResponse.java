import java.util.ArrayList;
public class TileSwapResponse extends Message {
	private ArrayList<Tile> tiles;
	
	public TileSwapResponse(ArrayList<Tile> tiles) {
		this.setUniqueID(413);
		this.tiles = tiles;
	}
}
