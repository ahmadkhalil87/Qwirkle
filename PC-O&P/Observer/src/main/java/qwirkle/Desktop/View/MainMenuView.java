package qwirkle.Desktop.View;

import qwirkle.Desktop.Communication.Messages.messages_to_server.GameListRequest;
import qwirkle.Desktop.Controller.MainMenuController;
import qwirkle.Desktop.Main.Mainstarter;
import qwirkle.Desktop.Resources.Resources;
import qwirkle.Desktop.View.InGame.GameView;
import qwirkle.Desktop.enumeration.*;

import java.io.IOException;

import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 * All elements of the main menu (i.e. Title, buttons) will be defined in this class. 
 * It contains a constructor that defines the view of the main menu, an instance of 
 * this class will be created in Mainstarter class, so that the user directly finds 
 * himself in the main menu at the start of the game.
 * 
 * @author Youssef Ben Ameur
 *
 */
public class MainMenuView extends Parent{

	private Scene scene;
	private Button btnSelectServer;
	private Button btnSelectGame;
	private Button btnSettings;
	private Button btnQuit;

	@SuppressWarnings("restriction")
	public MainMenuView (Scene tmpScene){
		this.scene = tmpScene;

		// Load Font and Style Sheet
		Font.loadFont(Resources.el_font, 16);
		getStylesheets().add(Resources.Css_Mmview);

		// Defining Menu Title 
		Text menuText = new Text("Qwirkle");
		menuText.setId("menuText");

		// Setting container for Title
		StackPane TitlePane = new StackPane();
		TitlePane.setPadding(new Insets(10, 10, 10, 10));
		TitlePane.setId("TitlePane");
		TitlePane.getChildren().addAll(menuText);
		TitlePane.maxWidthProperty().bind(scene.widthProperty().multiply(0.2));


		//Defining Buttons
		btnSelectServer = new Button("Select Server");
		btnSelectServer.setMinWidth(75);
		btnSelectServer.setTranslateX(-150);

		btnSelectGame = new Button("Match list");
		btnSelectGame.setMinWidth(75);
		btnSelectGame.setTranslateX(-50);

		btnSettings = new Button("Settings");
		btnSettings.setMinWidth(75);
		btnSettings.setTranslateX(-100);

		btnQuit = new Button("Quit");
		btnQuit.setMinWidth(75);
		btnQuit.setTranslateX(-50);


		// Connect Buttons to MainController (Set Functionalities)
		btnSelectServer.setOnAction(e -> { 
			SoundEffects.BUTTON_CLICK.play(Mainstarter.model.getSoundVol()); 
			if (Mainstarter.meClient.isOnline()) {
				try {
					Mainstarter.meClient.disconnect();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			MainMenuController.switchSelectServer();
		});
		btnSelectGame.setOnAction(e -> { 
			SoundEffects.BUTTON_CLICK.play(Mainstarter.model.getSoundVol());
			if(Mainstarter.meClient !=null) {
				Mainstarter.meClient.sendMessage(new GameListRequest());
			}
			MainMenuController.switchMatchListView(); 
		});
		btnQuit.setOnAction(e -> { 
			SoundEffects.BUTTON_CLICK.play(Mainstarter.model.getSoundVol());
			MainMenuController.closeAll();
		});
		btnSettings.setOnAction(e -> {
			SoundEffects.BUTTON_CLICK.play(Mainstarter.model.getSoundVol());
			MainMenuController.switchSettings();
		});

		// Setting Container for Buttons
		HBox btnPane = new HBox(20);
		btnPane.setAlignment(Pos.TOP_CENTER);
		btnPane.getChildren().addAll(btnSelectGame, btnSettings, btnSelectServer);
		btnPane.setId("buttonPane");

		VBox qbtnContainer = new VBox();
		qbtnContainer.setAlignment(Pos.BOTTOM_RIGHT);
		qbtnContainer.getChildren().addAll(btnQuit);

		//Setting Container for all Components
		VBox topPane = new VBox(10);
		topPane.setAlignment(Pos.CENTER);
		topPane.getChildren().addAll(TitlePane, btnPane);	

		// Defining Border Pane as Top-level Container
		BorderPane mainPane = new BorderPane();
		mainPane.setPadding(new Insets(50, 0, 0, 0));
		mainPane.prefHeightProperty().bind(scene.heightProperty());
		mainPane.prefWidthProperty().bind(scene.widthProperty());
		mainPane.setId("rootPane");

		// Setting (auto scaling) Background Image for Border Pane
		Image imgBackground = new Image(Resources.bgImage2);
		BackgroundImage backgroundImage = new BackgroundImage(imgBackground, BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
				new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, true, true));
		Background background = new Background(backgroundImage);
		mainPane.setBackground(background);


		// Adding All Elements to Border Pane
		// Setting title
		mainPane.setTop(topPane);
		BorderPane.setMargin(topPane, new Insets(0, 0, 50, 0));

		//setting quit button in the bottom
		mainPane.setBottom(qbtnContainer);
		BorderPane.setAlignment(qbtnContainer, Pos.BOTTOM_RIGHT);
		BorderPane.setMargin(qbtnContainer,new Insets(0, 25, 25, 0));

		// Adding Border Pane to Scene
		this.getChildren().add(mainPane);		
	}

	public void Tadaaaa() {

		Button[] buttonArray = new Button[]{null, this.btnSelectGame,
				this.btnSettings, this.btnSelectServer, this.btnQuit};

		for (int i=0; i<buttonArray.length; i++) {
			TranslateTransition tt = new TranslateTransition();
			tt.setNode(buttonArray[i]);
			tt.setDuration(new Duration(35*i));

			tt.setFromX(-50 * i);
			tt.setToX(0);

			tt.play();
		}
	}
}
