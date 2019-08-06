package qwirkle.Desktop.View;

import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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
import qwirkle.Desktop.Main.Mainstarter;
import qwirkle.Desktop.Resources.Resources;
import qwirkle.Desktop.View.InGame.GameView;
import qwirkle.Desktop.enumeration.ClientType;
import qwirkle.Desktop.enumeration.GameState;
import qwirkle.Desktop.enumeration.SoundEffects;
import qwirkle.Desktop.enumeration.WrongMove;
import qwirkle.Desktop.Communication.Messages.messages_to_server.GameListRequest;
import qwirkle.Desktop.Controller.MainMenuController;
import qwirkle.Desktop.Controller.SelectGameController;
import qwirkle.Desktop.Game.Configuration;
import qwirkle.Desktop.Game.Game;

/*
 * Panel containing the elements of the SelectGame menu (i.e. tables, buttons,...).
 * 
 * @ author Youssef Ben Ameur
 */
public class SelectGameView extends Parent {

	TableView<Game> matchList = new TableView<Game>();
	ListView<String> rulesTable = new ListView<String>();
	private int currently_selected = 0;

	public Game selectedGame;
	public static ObservableList<Game> games = FXCollections.observableArrayList();


	@SuppressWarnings("restriction")
	public SelectGameView( ){

		// Loading style sheet
		getStylesheets().add(Resources.Css_SelectGame);

		// Defining elements of this menu
		// Defining buttons and their functionalities		
		Button btnJoin = new Button("Join");
		Button btnSpectate = new Button("Spectate");
		Button btnRefresh = new Button("Refresh");

		btnJoin.setMaxWidth(125);
		btnSpectate.setMaxWidth(125);
		btnRefresh.setMaxWidth(125);
		btnJoin.setMaxHeight(20);
		btnSpectate.setMaxHeight(20);
		btnRefresh.setMaxHeight(20);

		btnJoin.setOnAction(e -> { 
			SoundEffects.BUTTON_CLICK.play(Mainstarter.model.getSoundVol());
			try {
				selectedGame = matchList.getSelectionModel().getSelectedItem();
				SelectGameController.requestJoin(currently_selected);
			}catch(IOException e1) {
				e1.printStackTrace();
			}
		});
		btnSpectate.setOnAction(e -> {
			SoundEffects.BUTTON_CLICK.play(Mainstarter.model.getSoundVol());
			try {
				selectedGame = matchList.getSelectionModel().getSelectedItem();
				SelectGameController.requestSpectate(currently_selected);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		btnRefresh.setOnAction(e -> { 
			SoundEffects.BUTTON_CLICK.play(Mainstarter.model.getSoundVol());
			matchList.getItems().clear();
			try {
				SelectGameController.GameListRequest();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		
		// Making a RowFactory to make Columns clickable
		matchList.setRowFactory(tv -> {TableRow<Game> row = new TableRow<>();
		row.setOnMouseClicked(event -> { 
			if (event.getClickCount() == 1 && (!row.isEmpty())){
				System.out.println("One CLICK DETECTED! @Game with ID: " + row.getItem().getGameId());
				currently_selected = row.getItem().getGameId();
				Configuration config = row.getItem().getConfig();
				makeListView(config);
			}
		});
		return row;
		});

		// Game's name Column
		TableColumn<Game, Integer> idColumn = new TableColumn<>("GameID");
		idColumn.setResizable(false);
		idColumn.setCellValueFactory(new PropertyValueFactory<Game, Integer>("gameId"));
		idColumn.prefWidthProperty().bind(matchList.widthProperty().multiply(0.125));

		TableColumn<Game, String> nColumn = new TableColumn<>("Game's name");
		nColumn.setResizable(false);
		nColumn.setCellValueFactory(new PropertyValueFactory<Game, String>("gameName"));
		nColumn.prefWidthProperty().bind(matchList.widthProperty().multiply(0.5));

		// Game status (e.g. On-Going, Paused,...)
		TableColumn<Game, GameState> stateColumn = new TableColumn<>("GameState");
		stateColumn.setResizable(false);
		stateColumn.setCellValueFactory(new PropertyValueFactory<Game, GameState>("gameState"));
		stateColumn.prefWidthProperty().bind(matchList.widthProperty().multiply(0.25));

		TableColumn<Game, Boolean> tournamentColumn = new TableColumn<>("Tournament Mode");
		tournamentColumn.setResizable(false);	
		tournamentColumn.setCellValueFactory(new PropertyValueFactory<Game, Boolean>("isTournament"));
		tournamentColumn.prefWidthProperty().bind(matchList.widthProperty().multiply(0.125));

		matchList.prefWidthProperty().bind(Mainstarter.scene.widthProperty().multiply(0.7));
		matchList.prefHeightProperty().bind(Mainstarter.scene.heightProperty().multiply(0.55));
		matchList.getColumns().add(idColumn);		
		matchList.getColumns().add(nColumn);
		matchList.getColumns().add(stateColumn);
		matchList.getColumns().add(tournamentColumn);
		matchList.setItems(games);

		// Defining Containers
		//Container for buttons
		HBox btnContainer = new HBox(15);
		btnContainer.setAlignment(Pos.CENTER_RIGHT);
		if (Mainstarter.meClient.getClientType() == ClientType.SPECTATOR) {
			btnContainer.getChildren().addAll(btnRefresh, btnSpectate);
		}else {
			btnContainer.getChildren().addAll(btnRefresh, btnJoin);
		}
		
		//Container tables
		HBox tabContainer = new HBox(5);
		tabContainer.setAlignment(Pos.CENTER);
		tabContainer.getChildren().addAll(matchList, rulesTable);

		// defining mainContainer
		VBox mainContainer = new VBox(5);
		mainContainer.setId("MainContainer");
		mainContainer.setAlignment(Pos.CENTER_RIGHT);
		mainContainer.getChildren().addAll(tabContainer, btnContainer);
		mainContainer.maxWidthProperty().bind(Mainstarter.scene.widthProperty().multiply(0.85));

		// defining root container

		StackPane rootPane = new StackPane();
		rootPane.setAlignment(Pos.CENTER);
		rootPane.getChildren().add(mainContainer);
		rootPane.prefWidthProperty().bind(Mainstarter.scene.widthProperty());
		rootPane.prefHeightProperty().bind(Mainstarter.scene.heightProperty().multiply(0.55));
		rootPane.setId("TabContainer");


		// ab hier wird code von MainMenuView geschrieben------------
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
		TitlePane.maxWidthProperty().bind(Mainstarter.scene.widthProperty().multiply(0.2));

		//Defining Buttons
		Button btnSelectServer = new Button("Select Server");
		btnSelectServer.setMinWidth(75);
		Button btnSelectGame = new Button("Match list");
		btnSelectGame.setMinWidth(75);
		//		Button btnSinglePlayer = new Button("vs A.I.");
		//		btnSinglePlayer.setMinWidth(75);
		Button btnSettings = new Button("Settings");
		btnSettings.setMinWidth(75);
		Button btnQuit = new Button("Quit");
		btnQuit.setMinWidth(75);


		// Connect Buttons to MainController (Set Functionalities)
		btnSelectServer.setOnAction(e -> { 
			SoundEffects.BUTTON_CLICK.play(Mainstarter.model.getSoundVol());
			MainMenuController.switchSelectServer();
		});
		btnSelectGame.setOnAction(e -> { 
			SoundEffects.BUTTON_CLICK.play(Mainstarter.model.getSoundVol());
			try {
				SelectGameController.GameListRequest();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
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

		//setting Container for Settings
		VBox selectGameContainer = new VBox();
		selectGameContainer.setAlignment(Pos.CENTER);
		selectGameContainer.getChildren().add(rootPane);

		//Setting Container for all Components
		VBox topPane = new VBox(10);
		topPane.setAlignment(Pos.CENTER);
		topPane.getChildren().addAll(TitlePane, btnPane);	

		// Defining Border Pane as Top-level Container
		BorderPane mainPane = new BorderPane();
		mainPane.setPadding(new Insets(50, 0, 0, 0));
		mainPane.prefHeightProperty().bind(Mainstarter.scene.heightProperty());
		mainPane.prefWidthProperty().bind(Mainstarter.scene.widthProperty());
		mainPane.setId("rootPane");

		// Setting (auto scaling) Background Image for Border Pane
		Image imgBackground = new Image(Resources.bgImage2);
		BackgroundImage backgroundImage = new BackgroundImage(imgBackground, BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
				new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, true, true));
		Background background = new Background(backgroundImage);
		mainPane.setBackground(background);


		// Adding All Elements to Border Pane
		// Setting title and buttons on top
		mainPane.setTop(topPane);
		BorderPane.setMargin(topPane, new Insets(0, 0, 50, 0));

		//setting slectGameView in the middle
		mainPane.setCenter(selectGameContainer);
		BorderPane.setAlignment(selectGameContainer, Pos.CENTER);
		BorderPane.setMargin(selectGameContainer, new Insets(0,0,100,0));

		//setting quit button in the bottom
		mainPane.setBottom(qbtnContainer);
		BorderPane.setAlignment(qbtnContainer, Pos.BOTTOM_RIGHT);
		BorderPane.setMargin(qbtnContainer,new Insets(0, 25, 25, 0));

		this.getChildren().addAll(mainPane);
	}

	/**
	 * Method used to make a ListView for the Rules
	 * @param config
	 * @return
	 */
	public void makeListView(Configuration config){
		ObservableList<String> tempol = FXCollections.observableArrayList();

		// Adding all the Configs information
		tempol.add("Colors & Shapes: " + config.getColorShapeCount());
		tempol.add("Max. Tiles on Hand: " + config.getMaxHandTiles());
		tempol.add("Max. Players: " + config.getMaxPlayerNumber());
		tempol.add("Amount of Tiles: " + config.getTileCount());
		tempol.add("Turn Timer: " + config.getTurnTime());
		tempol.add("Faultly Move Penalty: " + config.getWrongMove());
		if (config.getWrongMove() == WrongMove.POINT_LOSS) {
			tempol.add("Point Penalty :" + config.getWrongMovePenalty());
		}
		tempol.add("Penalty when out of time : " + config.getSlowMovePenalty());

		rulesTable.setItems(tempol);

	}
}

