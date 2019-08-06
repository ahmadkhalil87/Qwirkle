package qwirkle.Desktop.View;


import java.io.IOException;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
import qwirkle.Desktop.Controller.MainMenuController;
import qwirkle.Desktop.Controller.SelectGameController;
import qwirkle.Desktop.Controller.SelectServerController;
import qwirkle.Desktop.Main.Mainstarter;
import qwirkle.Desktop.Resources.Resources;
import qwirkle.Desktop.View.InGame.ChatView;
import qwirkle.Desktop.View.InGame.GameView;
import qwirkle.Desktop.enumeration.SoundEffects;


/**
 * Panel containing the elements of the SelectServer menu (i.e. textFields, labels,...).
 * This is also the start menu.
 * 
 * @ author Youssef Ben Ameur
 * @ author Houman Mahtabi (minor changes)
 */

public class SelectServerView extends Parent{

	private Scene scene;
	private Button btnConnect = new Button("Play");
	private Button btnSpectate = new Button("Spectate");


	@SuppressWarnings("restriction")
	public SelectServerView(Scene scene) {

		// Initializing the scene and the model instance
		this.scene = scene;
		//	this.mainMenuView = parent;

		// Loading style sheets
		Font.loadFont(Resources.el_font, 16);
		getStylesheets().add(Resources.Css_SelectServer);

		// Defining Title
		Text title = new Text("Connect to Server");
		StackPane titlePane = new StackPane();
		titlePane.getChildren().addAll(title);
		title.setId("Title");

		// Defining Labels
		Label nLabel = new Label("Name: ");
		Label ipLabel = new Label("IP-Address: ");
		Label pLabel = new Label("Port: ");

		// Defining input fields
		TextField nField = new TextField();
		TextField ipField = new TextField();
		TextField pField = new TextField();
		nField.setText("Ace");
		ipField.setText("swtpra.cs.upb.de");
		pField.setText("33100");

		nField.setPrefWidth(350);
		nField.setPrefHeight(35);
		ipField.setPrefHeight(35);
		pField.setPrefHeight(35);

		// setting identifications for TextFields (for css files)
		nField.setId("Field1");
		ipField.setId("Field2");
		pField.setId("Field3");

		// Restricting text fields inputs according to selected field
		// Name field restrictions
		nField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> myField, String oldValue, String newValue) {
				String repPattern = "[^a-zA-Z0-9]";
				String pattern = "[a-zA-Z0-9]";

				if (!newValue.matches(pattern)) {
					if (newValue.length() > 14) {
						nField.setText(oldValue);
					}else {
						nField.setText(newValue.replaceAll(repPattern, ""));
					}
				}
				if (nField.getText().isEmpty()) {
					btnConnect.setDisable(true);
					btnSpectate.setDisable(true);
				}else if (!nField.getText().isEmpty() && !pField.getText().isEmpty()
						&&	ipField.getText().length() > 8) {
					btnConnect.setDisable(false);
					btnSpectate.setDisable(false);
				}
			}			
		});

		//IP-Address field restrictions
/*		ipField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> myField, String oldValue, String newValue) {
				String pattern = makePartialIPRegex();

				if(newValue.equals("")) {
					btnConnect.setDisable(true);
					btnSpectate.setDisable(true);
					ipField.setText(newValue);
				}else if (!newValue.matches(pattern)) {
					ipField.setText(oldValue);
				}
				if (ipField.getText().isEmpty()) {
					btnConnect.setDisable(true);
					btnSpectate.setDisable(true);
				}else if (!nField.getText().isEmpty() && !pField.getText().isEmpty()
						&&	ipField.getText().length() > 8) {
					btnConnect.setDisable(false);
					btnSpectate.setDisable(false);
				}
			}
		});	
*/
		// Port field restrictions 
		pField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> myField, String oldValue, String newValue) {
				String pattern = "[0-9]{1,5}";

				if(newValue.equals("")) {
					pField.setText(newValue);				
				}else if (!newValue.matches(pattern)) { 
					pField.setText(oldValue);
				}

				if (pField.getText().isEmpty()) {
					btnConnect.setDisable(true);
					btnSpectate.setDisable(true);
				}else if (!nField.getText().isEmpty() && !pField.getText().isEmpty()
						&&	ipField.getText().length() > 8) {
					btnConnect.setDisable(false);
					btnSpectate.setDisable(false);
				}
			}
		});

		// Creating the tool tip and the image on which it will appear
		ImageView askMe = new ImageView();
		askMe.setImage(new Image(Resources.AskMe));
		askMe.setFitHeight(35);
		askMe.setFitWidth(35);

		Image toolTipText = new Image(Resources.AskMeText);
		Tooltip nameTip = new Tooltip();
		nameTip.setGraphic(new ImageView(toolTipText));
		Tooltip.install(askMe, nameTip);

		// Defining Containers for Labels and Fields
		// Defining labels container
		VBox labelContainer = new VBox(10);
		labelContainer.setAlignment(Pos.CENTER_LEFT);
		labelContainer.getChildren().addAll(nLabel, ipLabel, pLabel);

		// Container for name field and the tooltip image
		HBox nFieldContainer = new HBox(5);
		nFieldContainer.getChildren().addAll(nField, askMe);

		// Defining text fields Containers
		VBox fieldContainer = new VBox(10);
		fieldContainer.setAlignment(Pos.CENTER_LEFT);
		fieldContainer.getChildren().addAll(nFieldContainer, ipField, pField);

		// Container for labels and Fields
		HBox lfContainer = new HBox(15);
		lfContainer.setAlignment(Pos.CENTER);
		lfContainer.getChildren().addAll(labelContainer, fieldContainer);

		// Defining connect button
		Button btnOffline = new Button("Single Player");
		Button btnQuit = new Button("Quit");
		btnConnect.setDisable(false);
		btnSpectate.setDisable(false);
		btnQuit.setId("btnQuit");

		// Defining buttons functionalities 
		btnConnect.setOnAction(e -> {
			SoundEffects.BUTTON_CLICK.play(Mainstarter.model.getSoundVol());
			try {
				SelectServerController.connectToServer(nField.getText(), ipField.getText(), pField.getText(), false);
				if (Mainstarter.meClient.isOnline()) {
					MainMenuController.switchMatchListView();
				}
			}catch(IOException e1){
				e1.printStackTrace();
				return;
			}

		});
		btnSpectate.setOnAction(e -> {
			SoundEffects.BUTTON_CLICK.play(Mainstarter.model.getSoundVol()); 
			try {
				SelectServerController.connectToServer(nField.getText(), ipField.getText(), pField.getText(), true);
				if (Mainstarter.meClient.isOnline()) {
					MainMenuController.switchMatchListView();
					Mainstarter.selectGameView.matchList.getItems().clear();
					try {
						SelectGameController.GameListRequest();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}catch(IOException e1){
				e1.printStackTrace();
				return;
			}
			

		});
		btnOffline.setOnAction(e -> { 
			SoundEffects.BUTTON_CLICK.play(Mainstarter.model.getSoundVol()); 
			GameView singlePlayerView = new GameView();
			singlePlayerView.controlBar.setTimeLeft(10);
			singlePlayerView.controlBar.startTimer();
			Mainstarter.scene.setRoot(singlePlayerView);

		});
		btnQuit.setOnAction(e -> { SoundEffects.BUTTON_CLICK.play(Mainstarter.model.getSoundVol()); 
		MainMenuController.closeAll();
		});


		// Defining Button Container
		HBox btnContainer = new HBox(15);
		btnContainer.setAlignment(Pos.CENTER);
		btnContainer.getChildren().addAll(btnSpectate, btnConnect, 	btnOffline);

		// Defining Main Container 
		VBox mainContainer = new VBox(30);
		mainContainer.setPadding(new Insets(20, 20, 20, 20));
		mainContainer.setAlignment(Pos.CENTER);
		mainContainer.getChildren().addAll(titlePane, lfContainer, btnContainer);
		mainContainer.maxHeightProperty().bind(scene.heightProperty().multiply(0.3));
		mainContainer.maxWidthProperty().bind(scene.widthProperty().multiply(0.5));
		mainContainer.setId("mainContainer");

		StackPane sizeContainer = new StackPane();
		sizeContainer.getChildren().addAll(mainContainer);
		sizeContainer.setId("sizeContainer");
		sizeContainer.maxHeightProperty().bind(scene.heightProperty().multiply(0.5));

		// Defining root container 
		BorderPane rootPane = new BorderPane();
		rootPane.setPadding(new Insets(25, 0, 25, 0));
		rootPane.prefHeightProperty().bind(scene.heightProperty());
		rootPane.prefWidthProperty().bind(scene.widthProperty());
		rootPane.setId("rootPane");

		// Positioning the connect to server pane
		rootPane.setCenter(sizeContainer);
		BorderPane.setAlignment(sizeContainer, Pos.CENTER);

		// Positioning the quit Button 
		rootPane.setBottom(btnQuit);
		BorderPane.setAlignment(btnQuit, Pos.CENTER_RIGHT);
		BorderPane.setMargin(btnQuit, new Insets(0, 25, 0, 0));

		// Setting (auto scaling) Background Image for Border Pane
		Image imgBackground = new Image(Resources.bgImage2);
		BackgroundImage backgroundImage = new BackgroundImage(imgBackground, BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
				new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, true, true));
		Background background = new Background(backgroundImage);
		rootPane.setBackground(background);

		// setting the content of the view
		this.getChildren().add(rootPane);
		
		
	}

	// Method that creates the regular expression to restrict input in the IP-Address field
	private String makePartialIPRegex() {
		String partialBlock = "(([01]?[0-9]{0,2})|(2[0-4][0-9])|(25[0-5]))" ;
		String subsequentPartialBlock = "(\\."+partialBlock+")" ;
		String ipAddress = partialBlock+"?"+subsequentPartialBlock+"{0,3}";
		return "^"+ipAddress ;
	}
}
