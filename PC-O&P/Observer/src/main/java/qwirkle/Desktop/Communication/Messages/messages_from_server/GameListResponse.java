package qwirkle.Desktop.Communication.Messages.messages_from_server;

import java.util.ArrayList;

import qwirkle.Desktop.Game.Game;
import qwirkle.Desktop.Communication.Messages.message_abstract.Message;

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