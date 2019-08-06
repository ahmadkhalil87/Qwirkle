package qwirkle.Desktop.View.InGame;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import qwirkle.Desktop.Resources.Resources;
import qwirkle.Desktop.entity.Tile;

public class BagView extends StackPane{

	private StackPane topContainer;
	private ListView<HBox> bagListView;
	private ObservableList<HBox> bagList;
	
	private HashMap<Integer, SimpleIntegerProperty> tilesCount;

	public BagView() {
		
		// setup style
		getStylesheets().add(Resources.Css_BagView);
		
		bagList = FXCollections.observableArrayList();
		tilesCount = new HashMap<>();
		
		bagListView = new ListView<>();
		bagListView.setOrientation(Orientation.VERTICAL);
		bagListView.setItems(getBagList());
		
		topContainer = new StackPane();
		topContainer.setPadding(new Insets(15, 0, 45, 0));
		topContainer.getChildren().add(bagListView);
		StackPane.setAlignment(bagListView, Pos.CENTER);
		topContainer.setId("BagView");
		
		topContainer.prefHeightProperty().bind(this.prefHeightProperty());
		bagListView.prefHeightProperty().bind(topContainer.prefHeightProperty().multiply(0.8));
		
		this.setCache(true);
		this.setCacheShape(true);
		this.setCacheHint(CacheHint.SPEED);
		
		this.setPrefHeight(0);
		this.setPrefWidth(0);
		this.getChildren().add(topContainer);		
	}

	public void initiateBagView(Collection<Tile> firstBag) {		
		for(Tile tile : firstBag) {
			if (!this.tilesCount.keySet().contains(tile.getColor()+tile.getShape()*12)) {
				Label lblCount = new Label();	
				tilesCount.put(tile.getColor()+tile.getShape()*12
						, new SimpleIntegerProperty(1));
				lblCount.textProperty().bind(this.tilesCount
						.get(tile.getColor()+tile.getShape()*12).asString());
				bagList.add(generateBox(tile, lblCount));
			}else {
				tilesCount.get(tile.getColor()+tile.getShape()*12)
				.set(tilesCount.get(tile.getColor()+tile.getShape()*12).get()+1);
			}
		}
	}
	
	public HBox generateBox(Tile tile, Label count) {
		HBox tileBox = new HBox(5);
		Image tileImage = new Image(Resources.getTileImage(tile));
		ImageView tileView = new ImageView(tileImage);
		tileView.setFitHeight(80);
		tileView.setFitWidth(80);
		
		Label labelX = new Label("X");

		tileBox.getChildren().addAll(tileView, labelX, count);
		tileBox.setAlignment(Pos.CENTER_LEFT);
		
		return tileBox;
	}

	public void updateBag(ArrayList<Tile> tiles) {
		
		for(int id : tilesCount.keySet()){
			int newCount = 0;
			for(Tile tile : tiles) {
				if (tile.getShape()*12 + tile.getColor() == id) {
					newCount++;
				}
			}
			tilesCount.get(id).set(newCount);
		}
	}
	
	public void clearBag() {
		this.bagList.clear();
		this.bagListView.setItems(this.bagList);
	}

	public ListView<HBox> getBagListView() {
		return bagListView;
	}

	public void setBagListView(ListView<HBox> bagListView) {
		this.bagListView = bagListView;
	}

	public ObservableList<HBox> getBagList() {
		return bagList;
	}

	public void setBagList(ObservableList<HBox> bagList) {
		this.bagList = bagList;
	}

	public HashMap<Integer, SimpleIntegerProperty> getTilesCount() {
		return tilesCount;
	}

	public void setTilesCount(HashMap<Integer, SimpleIntegerProperty> tilesCount) {
		this.tilesCount = tilesCount;
	}

}
