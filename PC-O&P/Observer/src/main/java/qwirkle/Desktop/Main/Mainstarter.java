
package qwirkle.Desktop.Main;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import qwirkle.Desktop.Communication.Client;
import qwirkle.Desktop.Controller.GameController;
import qwirkle.Desktop.Controller.MainMenuController;
import qwirkle.Desktop.Controller.SelectGameController;
import qwirkle.Desktop.Controller.SelectServerController;
import qwirkle.Desktop.Controller.SettingsController;
import qwirkle.Desktop.Model.Model;
import qwirkle.Desktop.Resources.Resources;
import qwirkle.Desktop.View.MainMenuView;
import qwirkle.Desktop.View.SelectGameView;
import qwirkle.Desktop.View.SelectServerView;
import qwirkle.Desktop.View.SettingsView;
import qwirkle.Desktop.View.InGame.GameView;

/**
 * This is the main class. Start the applications.
 * (Optional: starts with a loading screen with the Logo of the group)
 * 
 * @author Youssef Ben Ameur
 *
 */

public class Mainstarter extends Application{
	public static Stage mainStage;
	public static Scene scene;
	public StackPane rootPane;
	public static Parent firstParent;
	public static Model model;
	
	public static SelectServerView selectServerView;
	public static MainMenuView mainMenuView;
	public static SelectGameView selectGameView;
	public static SettingsView settingsView;
	public static GameView gameView;
	public static Client meClient;
	
	@Override
	
	public void start(Stage tmpMainStage){
		this.mainStage = tmpMainStage;
		
		// Instantiate Model
		 model = new Model();
		
		// Music
//		final URL resource = getClass().getResource("src/main/resources/music/wind_walker_theme.wav");
//	    final Media media = new Media(resource.toString());
	    Media bgMusic = new Media(Resources.MUSIC_RESONANT_CHAMBER_THEME);
	    model.setMediaPlayerMusic(new MediaPlayer(bgMusic));
	    model.getMediaPlayerMusic().setCycleCount(MediaPlayer.INDEFINITE);
	    model.getMediaPlayerMusic().setVolume(model.getMusicVol());
	    model.getMediaPlayerMusic().play();
		
		// Setup Scene
		this.scene = new Scene(new Region(), 800, 650);

		// Method to start the game with a Loading screen

		// Instantiate View for SelectserverView(first scene) MainMenu,SettingsView and SelectGame 	
		selectServerView = new SelectServerView(scene);
//		model.setSeletcServerController(new SelectServerController());
		
//		model.setSelectGameController(new SelectGameController());
		
		mainMenuView = new MainMenuView(scene);		
//		model.setMainMenuController(new MainMenuController());	
		
		settingsView = new SettingsView(scene);
		settingsView.rootParent = mainMenuView;
//		model.setSettingsController(new SettingsController(scene));
		
		gameView = new GameView();
//		model.setGameController(new GameController());
		
		//Start with Main Menu
		scene.setRoot(this.selectServerView);
		
		// Setup Primary Stage
		mainStage.setTitle("Qwirkel");
		mainStage.setScene(scene);
		mainStage.setFullScreen(true);
		mainStage.show();
//		mainStage.setMinHeight(650);
//		mainStage.setMinWidth(800);
	}

//	public static void loadingScreen(Model model){
//	
//	Image PreLoader = new Image(Resources.LoadingScreen);
//    BackgroundImage backgroundImage = new BackgroundImage(PreLoader, BackgroundRepeat.NO_REPEAT,
//	        BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
//	        new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, true, true));
//	Background background = new Background(backgroundImage);
//	
//	BorderPane filler = new BorderPane();
//	filler.setPrefHeight(600);
//	filler.setPrefWidth(600);
//	filler.setBackground(background);
//
//	FadeTransition fadein = new FadeTransition(Duration.seconds(1), filler);
//	FadeTransition fadeout = new FadeTransition(Duation.seconds(1), filler);
//	scene.setRoot(filler);
//	
//	fadein.setFromValue(0.0);
//	fadein.setToValue(1.0);
//	fadein.play();
//	
//	fadein.setOnFinished(e ->{
//		fadeout.setFromValue(1.0);
//		fadeout.setToValue(0.0);
//		fadeout.play();
//	});
//	
//	fadeout.setOnFinished(e ->{
//		return;
//	});
//	
//}
	
	public static void main(String[] args){
		launch(args);
	}

}
