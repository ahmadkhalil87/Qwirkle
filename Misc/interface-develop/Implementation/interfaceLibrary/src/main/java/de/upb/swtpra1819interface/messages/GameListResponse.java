package de.upb.swtpra1819interface.messages;

import java.util.ArrayList;

public class GameListResponse extends Message {
	
	private ArrayList<Game> games;
	
	public static final int uniqueID = 301;
	
	public GameListResponse(ArrayList<Game> games) {
		super(uniqueID);
		this.games = games;
	}

	public ArrayList<Game> getGames() {
		return games;
	}

	public void setGames(ArrayList<Game> games) {
		this.games = games;
	}

}