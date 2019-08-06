import java.util.ArrayList;
public class TileSwapRequest extends Message {
	private ArrayList<Tile> tiles;
	
	public TileSwapRequest(ArrayList<Tile> tiles) {
		this.setUniqueID(411);
		this.tiles = tiles;
	}
}
