import java.util.ArrayList;
public class SendTiles extends Message {
	private ArrayList<Tile> tiles;
	
	public SendTiles(ArrayList<Tile> tiles) {
		this.setUniqueID(410);
		this.tiles = tiles;
	}
}
