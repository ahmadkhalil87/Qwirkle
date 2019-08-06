package qwirkle.Desktop.Communication.Messages.gameMessages;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import qwirkle.Desktop.Communication.Client;
import qwirkle.Desktop.Communication.Messages.message_abstract.Message;

import qwirkle.Desktop.entity.Tile;
import qwirkle.Desktop.entity.TileOnPosition;
import qwirkle.Desktop.enumeration.GameState;


public class GameDataResponse extends Message {
	
	private Collection<TileOnPosition> board;
	private Client currentClient;
	private Collection<Tile> ownTiles;
	private GameState gameState;
	
	public static final int uniqueID = 499;

	public GameDataResponse(Collection<TileOnPosition> board, Client currentClient, Collection<Tile> ownTiles,
			GameState gameState) {
		super(uniqueID);
		this.board = board;
		this.currentClient = currentClient;
		this.ownTiles = ownTiles;
		this.gameState = gameState;
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

	public void setCurrentClient(Client currentClient) {
		this.currentClient = currentClient;
	}

	public Collection<Tile> getOwnTiles() {
		return ownTiles;
	}

	public void setOwnTiles(Collection<Tile> ownTiles) {
		this.ownTiles = ownTiles;
	}

	public GameState isPaused() {
		return gameState;
	}

	public void setPaused(GameState gameState) {
		this.gameState = gameState;
	}


}
