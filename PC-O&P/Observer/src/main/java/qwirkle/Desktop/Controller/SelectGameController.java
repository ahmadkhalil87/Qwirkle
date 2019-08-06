package qwirkle.Desktop.Controller;

import java.io.IOException;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import qwirkle.Desktop.Communication.MessageHandlers.MessagesFromServerCaseHandling;
import qwirkle.Desktop.Communication.Messages.messages_to_server.GameJoinRequest;
import qwirkle.Desktop.Communication.Messages.messages_to_server.GameListRequest;
import qwirkle.Desktop.Communication.Messages.messages_to_server.SpectatorJoinRequest;
import qwirkle.Desktop.Game.Game;
import qwirkle.Desktop.Main.Mainstarter;
import qwirkle.Desktop.View.SelectGameView;


/**
 * This Controller is used for the Lobby process.
 * It has Methods, that send Messages to the Server, the results of which then are being processed in
 * {@linkplain MessagesFromServerCaseHandling}
 * 
 * @author Lukas
 *
 */
public class SelectGameController {
	
	/**
	 * Sends a GameListRequest to the Server
	 * @throws IOException
	 */
	public static void GameListRequest() throws IOException{
		Mainstarter.meClient.sendMessage(new GameListRequest());
	}
	
	// Process the server Response to a gameList request
	public static boolean setMatchList(ArrayList<Game> gamesList) {

		try{
			ObservableList<Game> templ = FXCollections.observableArrayList();
			for (Game tmpGame : gamesList) {
				templ.add(tmpGame);
			}
			SelectGameView.games = templ;
			onLoadListSuccess();
			return true;
		}catch(IllegalArgumentException e){
			System.out.println("I Don't know what this is, but I know what it is not, and it isn't a gameList.");
			return false;
		}catch(Exception ex){
			System.out.println("To be honest I did not see this exception coming.");
			return false;
		}
	}
	
	/*
	 * when loading the games list succeeds
	 */
	public static void onLoadListSuccess(){
		MainMenuController.switchMatchListView();
	}
	
	/*
	 * When loading the games list fails
	 */
	public static void onLoadListFailure(){
		
	}
	
	/**
	 * send a join as Spectator request
	 * 
	 * @param GameId
	 * @throws IOException
	 */
	public static void requestSpectate(int GameId) throws IOException{
		Mainstarter.meClient.sendMessage(new SpectatorJoinRequest(GameId));
	}
	
	/**
	 * Send a join as Player request
	 * 
	 * @param GameId
	 * @throws IOException
	 */
	public static void requestJoin(int GameId) throws IOException{
		Mainstarter.meClient.sendMessage(new GameJoinRequest(GameId));
	}
	
	/**
	 * This method is called by the communication controller on receiving a positive response on SpectateGameRequest
	 * 
	 * @return
	 */
	public static boolean playerJoinSuccess() {
		System.out.println("PLAYER JOIN SUCCESS INVOKED");
		Game tmpGame = Mainstarter.selectGameView.selectedGame;
		GameController.joinedGame = tmpGame;
		GameController.joinedGame.getPlayers().add(Mainstarter.meClient);
		Mainstarter.meClient.setIngame(true);
		
//		if(tmpGame.getState() == GameState.NOT_STARTET) {
			MainMenuController.switchGameView(tmpGame);
//		}
		return true;
	}

	/*
	 * 
	 */
	public static boolean spectatorJoinSuccess(Game game){
		GameController.joinedGame = game;
		Mainstarter.meClient.setIngame(true);
		MainMenuController.switchGameView(game);
		return true;
	}
	
	/**
	 * This method is called by the communication controller on receiving a negative response on SpectateGameRequest
	 * 
	 * @return
	 */
	public static boolean joinFailure(){
		
		return true;
	}
}
