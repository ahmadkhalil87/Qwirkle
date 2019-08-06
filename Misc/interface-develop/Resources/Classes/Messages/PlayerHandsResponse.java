import java.util.HashMap;
import java.util.ArrayList;
public class PlayerHandsResponse extends Message {
	private HashMap<Integer, ArrayList<Tile>> hands;
	
	public PlayerHandsResponse(HashMap<Integer, ArrayList<Tile>> hands) {
		this.setUniqueID(426);
		this.hands = hands;
	}
}
