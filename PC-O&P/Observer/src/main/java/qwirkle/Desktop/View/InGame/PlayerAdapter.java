package qwirkle.Desktop.View.InGame;

import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Map;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import qwirkle.Desktop.Communication.Client;
import qwirkle.Desktop.Controller.GameController;
import qwirkle.Desktop.Main.Mainstarter;
import qwirkle.Desktop.Resources.Resources;
import qwirkle.Desktop.enumeration.ClientType;
import qwirkle.Desktop.entity.PlayerInGame;
import qwirkle.Desktop.entity.Tile;

public class PlayerAdapter extends StackPane{

	private ObservableList<PlayerContainer> players = FXCollections.observableArrayList();
	private ListView<PlayerContainer> container; // The container for all graphic elements

	/**
	 * The constructor of the class.
	 *
	 * @param tmpView The actual GameView instance
	 */
	public PlayerAdapter() {

		getStylesheets().add(Resources.Css_PlayerAdapter);

		// Setup container
		container = new ListView<PlayerContainer>();
		container.setOrientation(Orientation.VERTICAL);
		container.setOnScroll(e->{return;});
		container.setItems(getPlayers());
		//		container.setPadding(new Insets(10,0,0,0));

		// Setup Background Image
		Image imgBackground = new Image(Resources.BACKGROUND_PLAYERADAPTER_IMAGE);
		BackgroundImage backgroundImage =
				new BackgroundImage(
						imgBackground,
						BackgroundRepeat.NO_REPEAT,
						BackgroundRepeat.NO_REPEAT,
						BackgroundPosition.DEFAULT,
						new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, true, true));
		Background background = new Background(backgroundImage);
		container.setBackground(background);

		// Put all together
		this.prefHeightProperty().bind(Mainstarter.scene.heightProperty());

		VBox topContainer = new VBox();
		topContainer.getChildren().add(container);

		this.getChildren().add(container);
	}

	public ObservableList<PlayerContainer> getPlayers(){
		return this.players;
	}

	public void updatePlayers(Collection<Client> players) {
		this.container.getItems().clear();

		for (Client pl : players) {
			players.add(pl);
		}
		this.container.setItems(getPlayers());
	}

	public void updatePlayerHands(Map<Client, Collection<Tile>> playersHands) {
		try {
			Platform.runLater(()->{
				for (Client serverClient : playersHands.keySet()) {
					PlayerContainer tmpContainer = getPlayerContainer(serverClient.getClientId());
					tmpContainer.getPlayerInGame().setPlayerTiles((ArrayList<Tile>) playersHands.get(serverClient));

					tmpContainer.obHand.clear();
					tmpContainer.getPlayerHand().getItems().clear();
					for (Tile tile : tmpContainer.getPlayerInGame().getPlayerTiles()) {
						Image tileImage = new Image(Resources.getTileImage(tile));
						ImageView tileView = new ImageView(tileImage);
						tileView.setFitHeight(20);
						tileView.setFitWidth(20);
						tmpContainer.obHand.add(tileView);
					}
					tmpContainer.getPlayerHand().setItems(tmpContainer.obHand);
				}
			});

		}catch(NullPointerException npe) {
			System.out.println("I am Going to reset the players");
			GameController.playersSet = false;
			GameController.setPlayerHands(playersHands);
		}catch(ConcurrentModificationException ce) {
			System.out.println("##############\nError Modification Things\n ##########");
		}	
	}

	public void resetPlayers() {
		this.players = FXCollections.observableArrayList();
		this.container.setItems(players);
		GameController.playersSet = false;
	}

	public void removePlayer(int id){
		PlayerContainer plCon = getPlayerContainer(id);
		if(plCon != null) {
			players.remove(plCon);
			container.setItems(getPlayers());
		}
	}

	public PlayerContainer getPlayerContainer(int id) {
		for(PlayerContainer plCon : players) {
			if(plCon.getPlayerInGame().getPlayer().getClientId() == id) {
				return plCon;
			}
		}
		return null;
	}
}