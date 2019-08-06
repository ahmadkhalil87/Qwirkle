package qwirkle.Desktop.View.InGame;

import javafx.animation.Timeline;
import javafx.animation.KeyFrame;

import java.util.ArrayList;

import javafx.animation.FadeTransition;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;

import javafx.scene.control.Label;
import javafx.scene.image.Image;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import qwirkle.Desktop.Game.Field;
import qwirkle.Desktop.Main.Mainstarter;
import qwirkle.Desktop.Resources.Resources;
import qwirkle.Desktop.entity.Tile;

/**
 * This class contains all elements of the game view. The grid on which the tiles will be placed 
 * is modelled by JavaFx GridPane.
 * This View contains the classes ControlBar, PlayerHand, PlayerAdapter, FieldView
 * 
 * @author Houman Mahtabi, Youssef Ameur
 *
 */
public class GameView extends Parent{

	public StackPane rootPane;
	public StackPane botLayer;
	private StackPane playField;

	private AnchorPane topLayer;
	
	private Field field;

	private StackPane startWaitPane;
	private StackPane pausePane;
	private StackPane winnerPane;
	
	private Label serverMsg;

	//	public SpectatorContainer spectators;
	public GridPane fieldGrid;
	public ControlBar controlBar;
	public PlayerAdapter playerAdapter; 
	public ChatView ChatView;

	private int fieldOrigin;
	private int fieldBounds;
	
	public Label lblGamePaused;

	private boolean animShowBag = false;
	
	public GameView(){

		getStylesheets().add(Resources.Css_InGame);

		// Board background
		Image imgBackground = new Image(Resources.GAMEBOARD_BACKGROUND_IMAGE);
		BackgroundSize bSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, true, true);
		Background background1 = new Background(new BackgroundImage(imgBackground,
				BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT,
				BackgroundPosition.CENTER,
				bSize));

		// Messages about the game state	
		// Pause label, declaration
		Label pauseLbl = new Label("Game's Paused");
		pauseLbl.setVisible(false);
		pauseLbl.setId("Pause");

		// Message that shows during the game
		Label pauseLable = new Label("Game is Paused");
		pauseLable.setId("pauseLable");
		
		VBox pauseBox = new VBox(10);
		pauseBox.getChildren().addAll(pauseLbl);
		pauseBox.setAlignment(Pos.CENTER);
		
		pausePane = new StackPane();
		pausePane.setPickOnBounds(true);
		pausePane.setId("winnerPane");
		pausePane.getChildren().add(pauseBox);
		
		// Message that shows before game start
		startWaitPane = new StackPane();
		startWaitPane.setPickOnBounds(true);
		startWaitPane.setId("winnerPane");
		
		Label startWaitLabel = new Label("Waiting for the server to start the game...");
		startWaitLabel.setId("WaitMessage");

		VBox waitLabels = new VBox(10);
		waitLabels.getChildren().addAll(startWaitLabel);
		waitLabels.setAlignment(Pos.CENTER);

		startWaitPane.prefHeightProperty().bind(Mainstarter.scene.heightProperty());
		startWaitPane.prefWidthProperty().bind(Mainstarter.scene.widthProperty());
		startWaitPane.getChildren().add(waitLabels);
		startWaitPane.setAlignment(Pos.CENTER);
		
		// Winner Pane
		winnerPane = new StackPane();
		winnerPane.setId("winnerPane");
		winnerPane.prefHeightProperty().bind(Mainstarter.scene.heightProperty());
		winnerPane.prefWidthProperty().bind(Mainstarter.scene.widthProperty());
		
		// Player container
		playerAdapter = new PlayerAdapter();
		playerAdapter.setPrefWidth(250);
		playerAdapter.setId("playerAdapter");

		// Center domain of the gameView 
		// initializing the Field Grid and its container
		field = new Field();
		
		fieldGrid = new GridPane();
		fieldGrid.setHgap(1);
		fieldGrid.setVgap(1);
		fieldGrid.setId("field");

		playField = new StackPane();
		playField.setAlignment(Pos.CENTER);
		playField.getChildren().add(fieldGrid);
		playField.setId("fieldContainer");

		// Creating instance of zoomablePane to add Zoom function to the field
		ZoomablePane zoomablePane = new ZoomablePane(playField);
		zoomablePane.setId("scrollPane");
		zoomablePane.setScaleValue(0.15);
		
		// Ensure main table container has same size as zoomable ScrollPane
		playField
		.minWidthProperty()
		.bind(
				Bindings.createDoubleBinding(
						() -> zoomablePane.getViewportBounds().getWidth(),
						zoomablePane.viewportBoundsProperty()));
		playField
		.minHeightProperty()
		.bind(
				Bindings.createDoubleBinding(
						() -> zoomablePane.getViewportBounds().getHeight(),
						zoomablePane.viewportBoundsProperty()));

		// Container for the Field View 
		StackPane centerPane = new StackPane();
		centerPane.getChildren().addAll(zoomablePane); // + debugView
		
		//setup chatView
		ChatView = new ChatView();
		ChatView.setMaxHeight(200);
		ChatView.setMinHeight(200);
		ChatView.setMaxWidth(800);
		ChatView.setMinWidth(800);
		ChatView.setTranslateY(-165);
		// chat is schön

		// StackPane 
		StackPane topPane = new StackPane();
		topPane.setAlignment(Pos.CENTER);
		topPane.getChildren().addAll(ChatView);
		topPane.prefWidthProperty().bind(Mainstarter.scene.widthProperty());
		topPane.setPickOnBounds(false);

		// Bottom part of this view 
		controlBar = new ControlBar();
		StackPane bottomPane = new StackPane();
		bottomPane.setAlignment(Pos.BOTTOM_CENTER);
		bottomPane.getChildren().addAll(controlBar);
		bottomPane.prefWidthProperty().bind(Mainstarter.scene.widthProperty());
		bottomPane.setPickOnBounds(false);
		zoomablePane.setPadding(new Insets(0, 0, controlBar.getHeight()+30, 0));

		
		// Container for the controlBar and playerHand 
		topLayer = new AnchorPane();
		topLayer.getChildren().addAll(bottomPane, topPane);
		topLayer.setPickOnBounds(false);
		AnchorPane.setBottomAnchor(bottomPane, 0.0);

		// Bottom Layer in which PlayerAdpater class and GameField will be modelled
		BorderPane botPane = new BorderPane();
		botPane.prefHeightProperty().bind(Mainstarter.scene.heightProperty());
		botPane.prefWidthProperty().bind(Mainstarter.scene.widthProperty());
		botPane.setBackground(background1);
		botPane.setCenter(centerPane);
		botPane.setLeft(playerAdapter);
		
		// Server Messages Label
		serverMsg = new Label();
		serverMsg.setId("serverMsg");
		serverMsg.setPadding(new Insets(0, 0, 150, 0));

		// Container for Field and playerAdapter
		botLayer = new StackPane();
		botLayer.prefHeightProperty().bind(Mainstarter.scene.heightProperty());
		botLayer.prefWidthProperty().bind(Mainstarter.scene.widthProperty());
		botLayer.getChildren().addAll(botPane, serverMsg);

		// rootPane which contains all Elements of this view 
		rootPane = new StackPane();
		rootPane.getChildren().addAll(botLayer, topLayer); // +startWaitPane

		this.getChildren().add(rootPane);
	}


	public int getFieldOrigin() {
		return this.fieldOrigin;
	}

	public void setFieldOrigin(int fieldOrigin) {
		this.fieldOrigin = fieldOrigin;
	}

	public int getFieldBounds() {
		return this.fieldBounds;
	}
	
	public void setFieldBounds(int fieldBounds) {
		this.fieldBounds = fieldBounds;
	}
	
	public void setTopLayer(AnchorPane topLayer) {
		this.topLayer = topLayer;
	}
	
	public AnchorPane getTopLayer() {
		return this.topLayer;
	}
	
	
	public void setServerMsg(String msg) {
		this.serverMsg.setText(msg);
	}
	
	public Label getServerMsg() {
		return this.serverMsg;
	}

	public boolean isAnimShowBag() {
		return this.animShowBag;
	}
	
	public void setAnimShowBag(boolean animShowBag) {
		this.animShowBag = animShowBag;
	}
	
	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}

	public StackPane getPlayField() {
		return playField;
	}

	public void setPlayField(StackPane playField) {
		this.playField = playField;
	}

	public void setGameIsPaused(boolean paused) {
		this.lblGamePaused.setVisible(paused);
	}
	
	/**
	 * @return the startWaitPane
	 */
	public StackPane getStartWaitPane() {
		return startWaitPane;
	}

	/**
	 * @return the winnerPane
	 */
	public StackPane getWinnerPane() {
		return winnerPane;
	}

	/**
	 * @return the pausePane
	 */
	public StackPane getPausePane() {
		return pausePane;
	}

}

