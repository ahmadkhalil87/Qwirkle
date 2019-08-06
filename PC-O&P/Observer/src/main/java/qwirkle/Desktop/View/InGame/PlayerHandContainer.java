package qwirkle.Desktop.View.InGame;

import java.util.ArrayList;

import javafx.scene.layout.StackPane;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.control.ListView;
import qwirkle.Desktop.Communication.Client;
import qwirkle.Desktop.Resources.Resources;
import qwirkle.Desktop.entity.Tile;

public class PlayerHandContainer extends StackPane {
	
	public ObservableList<ImageView> tiles = FXCollections.observableArrayList();
	public ListView<ImageView> tilesImage;
	public Label playerName;
	
	public PlayerHandContainer(Client client, ArrayList<Tile> hand){
		getStylesheets().add(Resources.Css_InGame);
		
		//read the Tiles from ArrayList and add them to observableList
		for (Tile tile : hand) {
			Image image = new Image(Resources.getTileImage(tile));
			ImageView tileImage = new ImageView();
			tileImage.setImage(image);
			tileImage.setFitHeight(70);
			tileImage.setFitWidth(70);
			tiles.add(tileImage);
		}
		
		// put the observableList into a ListView
		this.tilesImage = new ListView<ImageView>();
		this.tilesImage.setItems(tiles);
		this.tilesImage.setOrientation(Orientation.HORIZONTAL);
		this.tilesImage.setMaxWidth(600);
		this.tilesImage.setMinWidth(600);
		this.tilesImage.setMinHeight(80);
		this.tilesImage.setMaxHeight(80);
		this.tilesImage.setOnScroll(e->{return;});
		this.tilesImage.setId("tilesImage");
		HBox tilesBox = new HBox();
		tilesBox.getChildren().add(tilesImage);
		
		// make a lable for PlayersName
		this.playerName = new Label(client.getClientName() + ":");
		this.playerName.setPadding(new Insets(20,0,0,0));
		this.playerName.setId("playerName");
		HBox playerNameBox = new HBox();
		playerNameBox.getChildren().add(playerName);
		playerNameBox.setMaxWidth(80);
		playerNameBox.setMinWidth(80);

		// make a HBox and add the lable and the ListView to it
		HBox playerHandBox = new HBox(10);
		playerHandBox.getChildren().addAll(playerNameBox,tilesBox);
		playerHandBox.setMaxHeight(450);
		playerHandBox.setMaxWidth(700);
//		playerHandBox.setStyle("-fx-background-color: blue;");
		
		this.getChildren().add(playerHandBox);
	}

}
