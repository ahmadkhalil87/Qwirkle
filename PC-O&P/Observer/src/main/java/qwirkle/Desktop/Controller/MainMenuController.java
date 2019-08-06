package qwirkle.Desktop.Controller;

import qwirkle.Desktop.Game.Game;
import qwirkle.Desktop.Main.Mainstarter;
import qwirkle.Desktop.Resources.Resources;
import qwirkle.Desktop.View.SelectGameView;
import qwirkle.Desktop.View.InGame.GameView;

/**
 * Any changes in the scenes of the application are made through this controller.
 * (Defines functionalities of the main menu buttons). 
 * 
 * @author Youssef Ben Ameur
 *
 */

public class MainMenuController {

	private static boolean MmViewAnimation = true;


	public static void switchSelectServer(){
		MmViewAnimation = true;
		Mainstarter.scene.setRoot(Mainstarter.selectServerView);
	}

	public static void switchMainMenu(){
		Mainstarter.meClient.setIngame(false);
		Mainstarter.scene.setRoot(Mainstarter.mainMenuView);
		if(MmViewAnimation) {
			Mainstarter.mainMenuView.Tadaaaa();
		}
	}
	
	public static void switchSettings(){
		MmViewAnimation = true;
		if (!Mainstarter.settingsView.rootPane.getChildren().
				contains(Mainstarter.settingsView.setsContainer)) {
			Mainstarter.settingsView.setsContainer.getStylesheets().add(Resources.Css_SettingsView);
			Mainstarter.settingsView.rootPane.getChildren().
					add(Mainstarter.settingsView.setsContainer);
		}
		Mainstarter.scene.setRoot(Mainstarter.settingsView);
	}

	public static void switchMatchListView(){
		MmViewAnimation = false;
		Mainstarter.selectGameView = new SelectGameView();
		Mainstarter.scene.setRoot(Mainstarter.selectGameView);
	}
	
	/**
	 * Method used for actual Client connection to the Game
	 * @param selectedGame
	 */
	public static void switchGameView(Game selectedGame) {
		MmViewAnimation = true;
//		if (selectedGame.getGameState() == GameState.NOT_STARTED) {	
			
			Mainstarter.gameView = new GameView();
			GameController.initiateGame(selectedGame);
			Mainstarter.settingsView.rootParent = Mainstarter.gameView;
			Mainstarter.scene.setRoot(Mainstarter.gameView);
	}
	
	/**
	 * Method used for the Test View
	 */
	public static void switchGameView() {
		MmViewAnimation = true;
//		Mainstarter.model.setInGame(true);

		if (Mainstarter.gameView == null){
			Mainstarter.gameView = new GameView();
		}
		Mainstarter.settingsView.rootParent = Mainstarter.gameView;
		Mainstarter.scene.setRoot(Mainstarter.gameView);
	}
	
	/**
	 * Exit Game method
	 */
	public static void closeAll() {
		Mainstarter.mainStage.hide();
		System.exit(0);
	}

	

}
