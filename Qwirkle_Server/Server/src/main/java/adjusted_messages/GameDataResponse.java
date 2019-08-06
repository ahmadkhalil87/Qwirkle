package adjusted_messages;

import java.util.Collection;
import client.Client;
import de.upb.swtpra1819interface.messages.Message;
import de.upb.swtpra1819interface.models.GameState;
import de.upb.swtpra1819interface.models.Tile;
import gameboard.TileOnPosition;


public class GameDataResponse extends Message {
	
	private Collection<TileOnPosition> board;
	private Client currentClient;
	private Collection<Tile> ownTiles;
	private GameState gameState;
	
	public static final int uniqueID = 499;

	public GameDataResponse(Collection<TileOnPosition> board, Client currentClient, Collection<Tile> ownTiles, GameState gameState) {
		super(uniqueID);
		this.board = board;
		this.currentClient = currentClient;
		this.ownTiles = ownTiles;
	}

	public Collection<TileOnPosition> getBoard() {
		return board;
	}

	public void setBoard(Collection<TileOnPosition> board) {
		this.board = board;
	}

	public Client getCurrentClient() {
		return currentClient;
	}

	public Collection<Tile> getOwnTiles() {
		return ownTiles;
	}

	public GameState getGameState() {
		return gameState;
	}
}
