package de.upb.swtpra1819interface.messages;

import java.util.ArrayList;

public class Game {

	private int gameID;
	private String gameName;
	private GameState gameState;
	private boolean isTournament;
	private ArrayList<Client> players;
	private Configuration config;

	public Game(int gameID, String gameName, GameState gameState, boolean isTournament, ArrayList<Client> players,
			Configuration config) {

		this.gameID = gameID;
		this.gameName = gameName;
		this.gameState = gameState;
		this.isTournament = isTournament;
		this.players = players;
		this.config = config;

	}

	public int getGameID() {
		return gameID;
	}

	public void setGameID(int gameID) {
		this.gameID = gameID;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public GameState getGameState() {
		return gameState;
	}

	public void setGameState(GameState gameState) {
		this.gameState = gameState;
	}

	public boolean isTournament() {
		return isTournament;
	}

	public void setTournament(boolean isTournament) {
		this.isTournament = isTournament;
	}

	public ArrayList<Client> getPlayers() {
		return players;
	}

	public void setPlayers(ArrayList<Client> players) {
		this.players = players;
	}

	public Configuration getConfig() {
		return config;
	}

	public void setConfig(Configuration config) {
		this.config = config;
	}
}
