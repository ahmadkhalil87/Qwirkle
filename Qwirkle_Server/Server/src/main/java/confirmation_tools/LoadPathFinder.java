package confirmation_tools;

import java.io.File;
import java.util.Collection;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class LoadPathFinder{
	
	static String filepath;
	
	public String display() {

		
		//Initialising the Window and its properties
		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		window.setMinWidth(300);
		
		//Adding title and Label to the Window
		window.setTitle("FileExplorer");
		Label label = new Label();
		label.setText("Selected File Path: ");
		
		Button goButton = new Button("Confirm Selection");
		goButton.setOnAction(e -> window.close());
		goButton.setDisable(true);
		
		// Button to initiate pathfinding
		Button chooseButton = new Button("Choose Filepath");
		chooseButton.setOnAction(e -> {
			FileChooser chooser = new FileChooser();
			chooser.getExtensionFilters().addAll( new ExtensionFilter("json Files", "*.json"),
																new ExtensionFilter("Text Files", "*.txt"));
			File file = chooser.showOpenDialog(window);
			if (file!= null) {
				filepath = file.toString();
				label.setText("Selected File Path: " + filepath);
				goButton.setDisable(false);
			}
			else {
				label.setText("Selected File Path: ");
				goButton.setDisable(true);
			}
		});
		

			
		
		//Creating Scene and Layout
		VBox layout = new VBox(10);
		layout.getChildren().addAll(chooseButton,label,goButton);
		layout.setAlignment(Pos.CENTER);
		
		Scene scene = new Scene(layout, 300, 300);
		window.setScene(scene);
		window.showAndWait();
		return filepath;
	}
}