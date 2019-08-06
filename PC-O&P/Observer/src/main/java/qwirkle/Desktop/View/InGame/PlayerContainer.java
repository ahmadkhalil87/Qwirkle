package qwirkle.Desktop.View.InGame;


import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.HBox;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;

import qwirkle.Desktop.Resources.Resources;
import qwirkle.Desktop.entity.PlayerInGame;
import qwirkle.Desktop.entity.Tile;
import qwirkle.Desktop.enumeration.ClientType;

/**
 * This class makes the Container with the player's data (clientName, Score,is making a turn, ...) in the PlayerContainer.
 *
 * @author Houman Mahtabi
 */
public class PlayerContainer extends StackPane {
	
	private PlayerInGame playerInGame;
	private boolean playerMadeTurns; // Determines, if the player already made turns
	private int lastTurnX; // The coordinates of last tile placed
	private int lastTurnY; // The coordinates of last tile placed
	ObservableList<Node> childs;

	private Background Active;
	private Background inActive;
	
	public ObservableList<ImageView> obHand = FXCollections.observableArrayList();
	private ListView<ImageView> playerHand;
	
	private StringProperty score = new SimpleStringProperty();
	
	
	private Label lblPlayerName;
	public Label lblPlayerPoints;
	private ImageView imgPlayerContainer;
	private HBox lblPlayerNameBox;
	private HBox lblPlayerPointsBox;
	public VBox infoBox;
//	private StackPane containerFrame;
	VBox playerInfo;

	/**
	 * The constructor of the class.
	 *
	 * @param tmpView The actual GameView instance
	 * @param id The ID of the player
	 * @param name The name of the player
	 * @param type The type of the player
	 * @param points The score of the player
	 */
	public PlayerContainer(PlayerInGame playerInGame) {
		
		this.playerInGame = playerInGame;
		
		Image imgBackground = new Image(Resources.PLAYER_INACTIVE_IMG);
		BackgroundImage backgroundImage =
				new BackgroundImage(
						imgBackground,
						BackgroundRepeat.NO_REPEAT,
						BackgroundRepeat.NO_REPEAT,
						BackgroundPosition.DEFAULT,
						new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, true, true));
		inActive = new Background(backgroundImage);
		
		Image imgBackground2 = new Image(Resources.PLAYER_ACTIVE_IMG);
		BackgroundImage backgroundImage2 =
				new BackgroundImage(
						imgBackground2,
						BackgroundRepeat.NO_REPEAT,
						BackgroundRepeat.NO_REPEAT,
						BackgroundPosition.DEFAULT,
						new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, true, true));
		Active = new Background(backgroundImage2);
		
		this.playerInGame = playerInGame;
		
		// Load Font and Style Sheet
		Font.loadFont(Resources.el_font, 20);
		getStylesheets().add(Resources.Css_PlayerAdapter);
		
		// set up an icon_Player
		ImageView imageViewPlayer= new ImageView(Resources.ICON_PLAYER);
		imageViewPlayer.setFitHeight(30);
		imageViewPlayer.setFitWidth(30);
		
		ImageView imageViewPoints = new ImageView(Resources.ICON_POINTS);
		imageViewPoints.setFitHeight(30);
		imageViewPoints.setFitWidth(30);

		// Setup labels
		if (playerInGame.getPlayer().getClientType() == ClientType.ENGINE_PLAYER) {
			this.lblPlayerName = new Label("Bot " + playerInGame.getPlayer().getClientName());
			this.lblPlayerName.setStyle("-fx-font-size: 22px;");

		} else {
			this.lblPlayerName = new Label("  " + playerInGame.getPlayer().getClientName());
			this.lblPlayerName.setFont(new Font(Resources.el_font,20));
		}
		this.lblPlayerName.setFont(new Font(Resources.el_font,20));
	    this.lblPlayerName.setTextFill(Color.web("#000000"));
		this.lblPlayerName.setId("lblPlayerName");
		
		VBox lblBoxName = new VBox();
		lblBoxName.getChildren().add(lblPlayerName);
		lblBoxName.setPadding(new Insets(7,0,0,0));
		
		this.lblPlayerPoints = new Label();
		this.lblPlayerPoints.setFont(new Font(Resources.el_font,20));
	    this.lblPlayerPoints.setTextFill(Color.web("#000000"));
		this.lblPlayerPoints.setId("lblPlayerPoints");
		this.lblPlayerPoints.textProperty().bind(score);
		
		VBox lblBoxPoints = new VBox();
		lblBoxPoints.getChildren().add(lblPlayerPoints);
		lblBoxPoints.setPadding(new Insets(0,0,0,50));
		

		// Setup Background Image
		this.imgPlayerContainer = new ImageView(Resources.PLAYER_INACTIVE_IMG);
		this.imgPlayerContainer.setId("imgPlayerContainer");
		
		this.lblPlayerNameBox = new HBox(30);
		this.lblPlayerNameBox.getChildren().addAll(imageViewPlayer, lblBoxName);
		this.lblPlayerNameBox.setPadding(new Insets(5,0,0,10));
		
		this.lblPlayerPointsBox = new HBox(5);
		this.lblPlayerPointsBox.getChildren().addAll(imageViewPoints, lblBoxPoints);
		this.lblPlayerPointsBox.setPadding(new Insets(5,0,0,5));

		playerHand = new ListView<ImageView>();
	    playerHand.setOrientation(Orientation.HORIZONTAL);
	    playerHand.setPadding(new Insets(0, 3, 0, 3));
	    playerHand.setMaxHeight(40);
	    playerHand.setMinHeight(40);
	    playerHand.setItems(getObHand());

	    playerHand.setBackground(inActive);
	    playerHand.setId("playerHand");
	    

		this.infoBox = new VBox(5);
		this.infoBox.getChildren().addAll(lblPlayerNameBox
				,lblPlayerPointsBox
				, playerHand
				);	
		
		if(playerInGame.getPlayerTurn() == false){
			this.infoBox.setBackground(inActive);
		}else {
			this.infoBox.setBackground(Active);
		}
		
		this.infoBox.setMaxHeight(130);
		this.infoBox.setMinHeight(130);
		this.infoBox.setMaxWidth(200);
		this.infoBox.setMinWidth(200);
		
		this.setAlignment(Pos.CENTER);
		this.getChildren().addAll(this.infoBox);
	}

	// --- END CONSTRUCTOR ---
	
	public ObservableList<ImageView> getObHand(){
		
		for (Tile t : this.getPlayerInGame().getPlayerTiles()) {
			Image tileImage = new Image(Resources.getTileImage(t));
			ImageView tileView = new ImageView(tileImage);
			tileView.setFitHeight(20);
			tileView.setFitWidth(20);
			this.obHand.add(tileView);
		}
		return this.obHand;
	}
	
	
	public PlayerInGame getPlayerInGame() {
		return this.playerInGame;
	}


	public boolean getPlayerMadeTurns() {
		return playerMadeTurns;
	}

	public int getLastTurnX() {
		return lastTurnX;
	}

	public int getLastTurnY() {
		return lastTurnY;
	}

	public StringProperty getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score.set(score);
	}
	
	public Background getActive() {
		return this.Active;
	}
	
	public Background getInActive() {
		return this.inActive;
	}
	
	public ListView<ImageView> getPlayerHand(){
		return this.playerHand;
	}

}
