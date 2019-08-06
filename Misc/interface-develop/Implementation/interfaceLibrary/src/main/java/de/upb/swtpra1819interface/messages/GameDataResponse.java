package de.upb.swtpra1819interface.messages;

import java.util.List;

public class GameDataResponse extends Message {
	
	private List<TileOnPosition> board;
	private Client currentClient;
	private List<Tile> ownTiles;
	private boolean paused;
	
	public static final int uniqueID = 499;

	public GameDataResponse(List<TileOnPosition> board, Client currentClient, List<Tile> ownTiles,
			boolean paused) {
		super(uniqueID);
		this.board = board;
		this.currentClient = currentClient;
		this.ownTiles = ownTiles;
		this.paused = paused;
	}

	public List<TileOnPosition> getBoard() {
		return board;
	}

	public void setBoard(List<TileOnPosition> board) {
		this.board = board;
	}

	public Client getCurrentClient() {
		return currentClient;
	}

	public void setCurrentClient(Client currentClient) {
		this.currentClient = currentClient;
	}

	public List<Tile> getOwnTiles() {
		return ownTiles;
	}

	public void setOwnTiles(List<Tile> ownTiles) {
		this.ownTiles = ownTiles;
	}

	public boolean isPaused() {
		return paused;
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
	}


}
