import java.util.ArrayList;
public class GameListResponse extends Message {
	private ArrayList<Game> games;
	
	public GameListResponse(ArrayList<Game> games) {
		this.setUniqueID(301);
		this.games = games;
	}
}