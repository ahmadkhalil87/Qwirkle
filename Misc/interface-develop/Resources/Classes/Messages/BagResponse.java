import java.util.ArrayList;
public class BagResponse extends Message
{
	private ArrayList<Tile> bag;
	
	public BagResponse(ArrayList<Tile> bag) {
		this.setUniqueID(424);
		this.bag = bag;
	}
}
