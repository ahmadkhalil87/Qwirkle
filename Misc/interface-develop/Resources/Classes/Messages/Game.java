import java.util.ArrayList;
public class Game {
	private int gameID;
	private String gameName;
	private int gameState;
	private boolean isTournament;
	private ArrayList<Player> players;
	private Configuration config;
	
	public Game(int gameID, String gameName, int gameState, boolean isTournament, ArrayList<Player> players, Configuration config) {
		this.gameID = gameID;
		this.gameName = gameName;
		this.gameState = gameState;
		this.isTournament = isTournament;
		this.players = players;
		this.config = config;
	}
}
