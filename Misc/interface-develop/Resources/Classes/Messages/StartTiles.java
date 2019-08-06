import java.util.ArrayList;
public class StartTiles extends Message {
	private ArrayList<Tile> tiles;
	
	public StartTiles(ArrayList<Tile> tiles) {
		this.setUniqueID(408);
		this.tiles = tiles;
	}
}
