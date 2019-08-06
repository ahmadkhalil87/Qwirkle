package qwirkle.Desktop.Controller;


import qwirkle.Desktop.Communication.Client;
import qwirkle.Desktop.Communication.Messages.gameMessages.*;
import qwirkle.Desktop.Game.Game;
import qwirkle.Desktop.entity.TileOnPosition;
import qwirkle.Desktop.Main.Mainstarter;
import qwirkle.Desktop.Resources.Resources;
import qwirkle.Desktop.View.InGame.BagView;
//import qwirkle.Desktop.View.InGame.GridCells;
import qwirkle.Desktop.View.InGame.PlayerContainer;
import qwirkle.Desktop.entity.PlayerInGame;
import qwirkle.Desktop.enumeration.GameState;
import qwirkle.Desktop.enumeration.SoundEffects;
import qwirkle.Desktop.entity.Tile;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Collection;

public class GameController extends Parent {

	public static boolean playersSet = false;
	public static boolean scoreInitiated = false;
	private static boolean bagInitiated = false;

	public static Game joinedGame;
	public static Client currentPlayer;

	private static ArrayList<TileOnPosition> lastTurn = new ArrayList<>();

	// --- MODEL HANDLING ---
	/**
	 *  Method that will initiate the information of the game when the spectator joins it
	 * @param game
	 */
	public static void initiateGame(Game game){

		int tilesInGame = (int) (Math.pow(game.getConfig().getColorShapeCount(), 2) * game.getConfig().getTileCount());
		Mainstarter.gameView.setFieldOrigin(tilesInGame);
		Mainstarter.gameView.setFieldBounds(tilesInGame * 2);

		if (game.getGameState() == GameState.NOT_STARTED) {			
			int tilesInBagCount = game.getConfig().getTileCount() * (int) Math.pow(game.getConfig().getColorShapeCount(), 2);
			Mainstarter.gameView.controlBar.setBagValue(tilesInBagCount);
			Mainstarter.gameView.controlBar.setTimeLeft(game.getConfig().getTurnTime()/1000); //because server uses milliseconds and timeLeft uses seconds
//			Mainstarter.gameView.controlBar.timeline.stop();
			// Adding label that signals that the game has not started yet
			Mainstarter.gameView.botLayer.getChildren().add(Mainstarter.gameView.getStartWaitPane());
		}
		else if(game.getGameState() == GameState.IN_PROGRESS 
				|| game.getGameState() == GameState.PAUSED){
			Mainstarter.meClient.sendMessage(new PlayerHandsRequest());
			Mainstarter.meClient.sendMessage(new GameDataRequest());
			Mainstarter.meClient.sendMessage(new TurnTimeLeftRequest());
			Mainstarter.meClient.sendMessage(new ScoreRequest());

			if (game.getGameState() == GameState.PAUSED) {
				Mainstarter.gameView.botLayer.getChildren().add(Mainstarter.gameView.getPausePane());
			}
		}
	}

	/**
	 * This method checks, if the model has been initialized with accurate values.<br>
	 * It will show a message box if not so.
	 *
	 * @param forceMessage Set to true, if you want to have shown message box when model is valid
	 * @return true if model is valid, false if not so
	 */
	public static boolean checkIfModelIsValid(boolean forceMessage) {
		boolean retval = true;
		String message = "";

		// Check if GameView has been instantiated
		if (Mainstarter.gameView == null) {
			retval = false;
			message += "GameView not instantiated...ERROR\n";
		} else {
			message += "GameView instantiated...OK\n";
		}

		// Check if deck has been set
		if (joinedGame.getAllBagTiles() != null) {
			if (joinedGame.getAllBagTiles().size() > 0) {
				message += "'deck[]' has been set...OK\n";
			}
		} else {
			retval = false;
			message += "'deck[]' has not been set...ERROR\n";
		}

		// Check if maxTiles has been set
		if (joinedGame.getBagSize() == 0) {
			retval = false;
			message +=
					"'maxTiles' has been set...ERROR (" + Integer.toString(joinedGame.getBagSize()) + ")\n";
		} else {
			message += "'maxTiles' has been set...OK (" + Integer.toString(joinedGame.getBagSize()) + ")\n";
		}

		// Check if players has been set
		if (joinedGame.getPlayers() != null) {
			if (joinedGame.getPlayers().size() > 0) {
				message += "'players[]' has been set...OK\n";
			}
		} else {
			// retval = false;
			message += "'players[]' has not been set...WARNING\n";
		}

		// Check if maxPlayers is in the allowed range
		if ((joinedGame.getConfig().getMaxPlayerNumber() >= 1) && (joinedGame.getConfig().getMaxPlayerNumber() <= 5)) {
			message +=
					"'maxPlayers' is in the allowed range...OK ("
							+ Integer.toString(joinedGame.getConfig().getMaxPlayerNumber())
							+ ")\n";
		} else {
			retval = false;
			message +=
					"'maxPlayers' is not in the allowed range...ERROR ("
							+ Integer.toString(joinedGame.getConfig().getMaxPlayerNumber())
							+ ")\n";
		}
		return retval;
	}


	// --- Player Handling ---
	/**
	 * Here the Hand that was dealt to each player will be set
	 * @param playersHands
	 */
	public static void setPlayerHands(Map<Client, Collection<Tile>> playersHands) {
		if(!playersSet) {
			for(Client player : playersHands.keySet()) {
				
				if(!playersSet) {
					joinedGame.getPlayers().add(player);
				}
				if (joinedGame.playerHands == null) {
					joinedGame.playerHands = new HashMap<Client, Collection<Tile>>();
					joinedGame.playerHands.put(player, playersHands.get(player));				
				}else {
					joinedGame.playerHands.put(player, playersHands.get(player));
				}
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						PlayerInGame plInGame = new PlayerInGame(player
								, (ArrayList<Tile>) joinedGame.playerHands.get(player)
								, 0
								, false);
						Mainstarter.gameView.playerAdapter.getPlayers().add(new PlayerContainer(plInGame));
					}				
				});
			}
			playersSet = true;
		}else {
			Mainstarter.gameView.playerAdapter.updatePlayerHands(playersHands);
		}
	}	

	/**
	 * This method tells the GameView to add a player/spectator to the game.<br>
	 * Will be forwarded to -> GameView -> PlayerContainer -> PlayerRibbon (constructor)<br>
	 * Will be forwarded to -> GameView -> PlayerContainer -> SpectatorsRibbon<br>
	 *
	 * @param id The ID of the player, who has to be added
	 * @param name The name of the player, who has to be added
	 * @param type The type of the player, who has to be added
	 * @return true, if player was successfully added to GameView / false, if not so
	 */
	public static boolean addPlayer(Client client) {
		boolean retval = false;
		switch (client.getClientType()) {
		case PLAYER: // not implemented yet
			joinedGame.getPlayers().add(client);
			break;
		case ENGINE_PLAYER: // Add AI-player
			joinedGame.getPlayers().add(client);
			// retval = Mainstarter.model.getGameView().playerAdapter.addPlayer(client);
			break;
		case SPECTATOR: // Add Spectator
			break;
		default: // Wrong player type
			retval = false;
			break;
		}
		return retval;
	}

	/**
	 * this method sets each player score
	 * @param scores
	 */
	public static void setScores(Map<Client, Integer> scores) {
		for (Client player : scores.keySet()) {
			if (joinedGame.playersScores == null) {
				joinedGame.playersScores = new HashMap<Client, Integer>();
				joinedGame.playersScores.put(player, scores.get(player));
			}else {
				joinedGame.playersScores.put(player, scores.get(player));
			}
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					for(PlayerContainer plCon : Mainstarter.gameView.playerAdapter.getPlayers()) {
						if (plCon.getPlayerInGame().getPlayer().getClientId() ==
								player.getClientId()) {
							plCon.setScore(Integer.toString(scores.get(player)));
						}
					}
				}				
			});
		}
	}

	/**
	 * This method tells the GameView to remove a player/spectator from the game.<br>
	 * Will be forwarded to -> GameView -> PlayerContainer -> PlayerRibbon (set to null)<br>
	 * Will be forwarded to -> GameView -> PlayerContainer -> SpectatorsRibbon<br>
	 *
	 * @param id The ID of the player who has to be removed
	 * @return true, if player was successfully removed from GameView / false, if not so
	 */
	public static boolean removePlayer(int id) {
		for (Client cl : joinedGame.getPlayers()) {
			if (cl.getClientId() == id) {
				Platform.runLater(()->{
					Mainstarter.gameView.playerAdapter.removePlayer(cl.getClientId());
				});
				return true;
			}
		}
		return false;
	}

	public static void informOfLeave(Client leaver) {
		int score = Integer.parseInt(Mainstarter
				.gameView
				.playerAdapter
				.getPlayerContainer(leaver.getClientId()).getScore().get());
		Platform.runLater(()->{
			Mainstarter.gameView.setServerMsg("Player \"" 
					+ leaver.getClientName() +"\" left the game with " 
					+ score + " points.");			
		}); 
		playMessage(Mainstarter.gameView.getServerMsg());
	}
	
	/**
	 * Updates the Current player
	 * @param player
	 */
	public static void setCurrentPlayer(Client player) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				for(PlayerContainer plCon : Mainstarter.gameView.playerAdapter.getPlayers()) {
					System.out.println(plCon);
				}
				if (currentPlayer != null) {

					for(PlayerContainer plCon : Mainstarter.gameView.playerAdapter.getPlayers()) {
						if (plCon.getPlayerInGame().getPlayer().getClientId()
								== player.getClientId()){
							plCon.infoBox.setBackground(plCon.getActive());
							plCon.getPlayerHand().setBackground(plCon.getActive());

						}else if (plCon.getPlayerInGame().getPlayer().getClientId() ==
								currentPlayer.getClientId()) {
							plCon.infoBox.setBackground(plCon.getInActive());
							plCon.getPlayerHand().setBackground(plCon.getInActive());
						}
					}
					currentPlayer = player;
				}else {
					currentPlayer = player;
					for(PlayerContainer plCon : Mainstarter.gameView.playerAdapter.getPlayers()) {
						if (plCon.getPlayerInGame().getPlayer().getClientId()
								== player.getClientId()){
							plCon.infoBox.setBackground(plCon.getActive());
							plCon.getPlayerHand().setBackground(plCon.getActive());
						}
					}
				}
				Mainstarter.gameView.controlBar.stopTimer();
				if (Mainstarter.gameView.controlBar.timeline!=null) {
					Mainstarter.gameView.controlBar.timeline.stop();
				}
				Mainstarter.gameView.controlBar.setTimeLeft(joinedGame.getConfig().getTurnTime()/1000); // because server uses milliseconds and timeLeft works with seconds
				Mainstarter.gameView.controlBar.startTimer();
			}
		});
	}

	// --- View Management ---
	/*
	 * Resets GameView (Players list & Filed & Time & bag)
	 */
	public static void resetGameView() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Mainstarter.gameView.fieldGrid.getChildren().clear();
				Mainstarter.gameView.playerAdapter.resetPlayers();
				
				if(Mainstarter.gameView.botLayer.getChildren().contains(Mainstarter.gameView.getStartWaitPane())) {
					Mainstarter.gameView.getStartWaitPane().setVisible(false);
				}
				if(Mainstarter.gameView.botLayer.getChildren().contains(Mainstarter.gameView.getWinnerPane())){
					Mainstarter.gameView.getWinnerPane().setVisible(false);
				}
			}
		});
	}

	/**
	 * this method will call the settings menu
	 */
	public static void callSettings () {
		BorderPane tmpPane = Mainstarter.settingsView.setsContainer;
		tmpPane.getStylesheets().add(Resources.Css_InGame);

		if(!Mainstarter.gameView.rootPane.getChildren().contains(tmpPane)) {
			Mainstarter.gameView.rootPane.getChildren().add(tmpPane);
		}else {
			Mainstarter.gameView.rootPane.getChildren().remove(tmpPane);
		}
	}

	/**
	 * Show Winner and his/her score
	 * @param winner
	 * @param score
	 */
	public static void showWinner(Client winner, int score) {	
		

		Label winLbl = new Label("Winner");
		winLbl.setId("winnerLabel");

		Label playerLbl = new Label();
		playerLbl.setText(winner.getClientName() + ": " + Integer.toString(score));
		playerLbl.setId("WaitMessage");
		
		VBox labelBox = new VBox(10);
		labelBox.getChildren().addAll(winLbl, playerLbl);
		labelBox.setAlignment(Pos.CENTER);
		
		Mainstarter.gameView.getWinnerPane().getChildren().addAll(labelBox);
		Mainstarter.gameView.getWinnerPane().setAlignment(Pos.CENTER);

		Platform.runLater(() ->{
			Mainstarter.gameView.botLayer.getChildren().add(Mainstarter.gameView.getWinnerPane());	
		});
	}

	public static void playMessage(Label msgLabel) {
		FadeTransition serverMsgIn = new FadeTransition(Duration.millis(2000), msgLabel);
		serverMsgIn.setFromValue(0.0);
		serverMsgIn.setToValue(1.0);	
		
		FadeTransition serverMsgOut = new FadeTransition(Duration.millis(1000), msgLabel);
		serverMsgOut.setFromValue(1.0);
		serverMsgOut.setToValue(0.0);
		
		serverMsgIn.setOnFinished(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				serverMsgOut.play();
			}
		});		
		serverMsgIn.play();
	}

	// --- Controlbar Handling ---
	/**
	 * Update the bag after every turn
	 */
	public static void updateBag(ArrayList<Tile> tiles) {
		if (!bagInitiated) {
			Mainstarter.gameView.controlBar.setBgView(new BagView());
			Mainstarter.gameView.controlBar.getBgView().initiateBagView(tiles);
			Mainstarter.gameView.controlBar.showBag();
			bagInitiated = true;
		}else {
			Mainstarter.gameView.controlBar.getBgView().updateBag(tiles);
			Mainstarter.gameView.controlBar.showBag();
		}
	}

	/**
	 * Will set the time left in the current turn 
	 * @param turnTime
	 */
	public static void updateTimeLeft(TurnTimeLeftResponse turnTime) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Mainstarter.gameView.controlBar.timeline.stop();
				Mainstarter.gameView.controlBar.setTimeLeft(turnTime.getTime()/1000);// because server uses milliseconds and timeLeft works with seconds!!
				System.out.println(turnTime.getTime()/1000);
				Mainstarter.gameView.controlBar.startTimer();
			}
		});
	}


	// --- Field Handling ---
	/**
	 * Setting the tiles that are placed on the board
	 * @param onBoard
	 */
	public static void setBoard(Collection<TileOnPosition> onBoard){
		Mainstarter.gameView.getField().setBoard(onBoard);
		ArrayList<TileOnPosition> tmpBoard = (ArrayList<TileOnPosition>) onBoard;
		try {
			for (int i=0; i<tmpBoard.size(); i++) {
				Image tileImage = new Image(Resources.getTileImage(tmpBoard.get(i).getTile()));
				ImageView tileView = new ImageView(tileImage);
				StackPane tileViewContainer = new StackPane();
				tileViewContainer.getChildren().add(tileView);
				tileViewContainer.setPadding(new Insets(7,7,7,7));
				tileViewContainer.setStyle("-fx-background-color: white;");
				int x = tmpBoard.get(i).getX() + Mainstarter.gameView.getFieldOrigin();
				int y = tmpBoard.get(i).getY() + Mainstarter.gameView.getFieldOrigin();

				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						Mainstarter.gameView.fieldGrid.add(tileViewContainer, x, y);	
					}
				});
			}
		}catch(NullPointerException npE) {
			System.out.println("The Board may not have been initialized how it should have");
		}
	}

	/**
	 * update the information of the game; namely bagTilesCount and Field after each turn
	 * @param setTiles
	 * @param tileInBag
	 */
	public static void update(Collection<TileOnPosition> setTiles, int tileInBag) {

		Platform.runLater(() -> {
			Mainstarter.gameView.controlBar.setBagValue(tileInBag);
			Mainstarter.gameView.getField().addTile(setTiles);
		});

		if(!lastTurn.isEmpty()) {
			for (Node node : Mainstarter.gameView.fieldGrid.getChildren()) {
				Platform.runLater(()->{	
					node.setStyle("-fx-background-color: white;");
				});
			}
		}

		lastTurn.clear();
		for(TileOnPosition tile : setTiles){
			lastTurn.add(tile);
			Image tileImage = new Image(Resources.getTileImage(tile.getTile()));
			ImageView tileView = new ImageView(tileImage);
			StackPane tileViewContainer = new StackPane();
			tileViewContainer.getChildren().add(tileView);
			tileViewContainer.setPadding(new Insets(7,7,7,7));
			tileViewContainer.setStyle("-fx-background-color: red;");
			int x = tile.getX() + Mainstarter.gameView.getFieldOrigin();
			int y = tile.getY() + Mainstarter.gameView.getFieldOrigin();
			Platform.runLater(()->{
				Mainstarter.gameView.fieldGrid.add(tileViewContainer, x, y);
			});
		}

		Mainstarter.meClient.sendMessage(new ScoreRequest());
		Mainstarter.meClient.sendMessage(new PlayerHandsRequest());
	}


	// --- Chat Management ---
	/**
	 * Method for showing client Messages
	 * @param msg
	 * @param from
	 */
	public static void showPlayerMessage(String msg, Client from) {
		Platform.runLater(()->{
			//			Mainstarter.gameView.ChatView.chatMessages.add(from.getClientName() + ": "+ msg);
			//			Mainstarter.gameView.ChatView.addMessage(from.getClientName() + ": "+ msg);
			Mainstarter.gameView.ChatView.addMessage(from.getClientName() + ": " + msg);

		});
	}


	// --- GAME FLOW HANDLING ---
	/**
	 * values and calculate the maximum size of the grid in the GameView from the server-submitted
	 * deck.<br>
	 * Will be forwarded to -> GameView.<br>
	 * Will be forwarded to -> GameView -> ControlSidebar.<br>
	 * Will be forwarded to -> GameView -> DeckSlide.
	 *
	 * @param forceStart Set to true if game should also start with an inaccurate model
	 * @param showValidModel Set to true, if you want to have shown message box when model is valid
	 */
	public static void startGame() {

		Mainstarter.meClient.sendMessage(new PlayerHandsRequest());
//		Mainstarter.meClient.sendssage(new CurrentPlayerRequest());
		if (Mainstarter.gameView.botLayer.getChildren().contains(Mainstarter.gameView.getStartWaitPane())){
			Mainstarter.gameView.getStartWaitPane().setVisible(false);
		}
	//		Platform.runLater(new Runnable(){
	//			@Override
	//			public void run() {
	//				if (!Mainstarter.gameView.botLayer.getChildren().contains(Mainstarter.gameView.countDownPane)){
	//					Mainstarter.gameView.countDownPane.setVisible(true);
	//					Mainstarter.gameView.botLayer.getChildren().add(Mainstarter.gameView.countDownPane);
	//				}
	//			}
	//		});
	//		Mainstarter.gameView.startCountDown();
		// Play Start Sound
		SoundEffects.BUTTON_CLICK.play(Mainstarter.model.getSoundVol());
	}

	/**
	 * This method will pause the game. It will also interrupt the countdown if one player is just
	 * making a turn.<br>
	 * Will be forwarded to -> GameView -> ControlBar
	 *
	 * @return true, if successful / false, if not so
	 */
	public static boolean pauseGame() {
		Mainstarter.gameView.setGameIsPaused(true);
		return Mainstarter.gameView.controlBar.pauseGame();
	}

	/**
	 * This method will resume the game. It will also continue the countdown if one player is just
	 * making a turn.<br>
	 * Will be forwarded to -> GameView -> ControlSidebar<br>
	 *
	 * @return true, if successful / false, if not so
	 */
	public static boolean resumeGame() {
		Mainstarter.gameView.setGameIsPaused(false);
		return Mainstarter.gameView.controlBar.resumeGame();
	}

	public static void gameAborted() {
		StackPane abortPane = new StackPane();		
		abortPane.setId("abortPane");

		Label winLbl = new Label("Server has ended the Game ");
		winLbl.setId("abortLabel");
		
		Button btnExit = new Button("Leave Game");
		btnExit.setOnAction(e ->{
			Mainstarter.meClient.sendMessage(new LeavingRequest());
			MainMenuController.switchMainMenu();
		});
		
		VBox labelBox = new VBox(10);
		labelBox.getChildren().addAll(winLbl,btnExit);
		labelBox.setAlignment(Pos.CENTER);

		abortPane.prefHeightProperty().bind(Mainstarter.scene.heightProperty());
		abortPane.prefWidthProperty().bind(Mainstarter.scene.widthProperty());
		abortPane.getChildren().addAll(labelBox);
		abortPane.setAlignment(Pos.CENTER);

		Platform.runLater(() ->{
			Mainstarter.gameView.botLayer.getChildren().add(abortPane);	
		});
		
		
		MainMenuController.switchMainMenu();
	}
}
