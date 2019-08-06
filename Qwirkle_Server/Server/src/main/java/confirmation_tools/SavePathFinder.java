package confirmation_tools;

import java.io.File;

import javax.swing.JFileChooser;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SavePathFinder {
	
	String filepath;

	
	//Initialising the Window and its properties
	public String display() {
		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		window.setMinWidth(300);
				
		//Adding title and Label to the Window
		window.setTitle("FileExplorer");
		Label label = new Label();
		label.setText("Selected File Path: NONE");
		
		TextField namingfield = new TextField();
		namingfield.setPromptText("Enter filename");
		namingfield.setDisable(true);
		
		Button confirmButton = new Button("Confirm Selection");
		confirmButton.setOnAction(e -> { 
			String filename = namingfield.getText();
			if(filename == null) {
				filename = "config";
			}
			filepath = filepath + "/" + filename + ".json";
			window.close();
		});
		confirmButton.setDisable(true);
		
		//OK-Button - Confirmation that User has read the Message
		Button pathButton = new Button("Choose Directory");
		pathButton.setOnAction(e -> {
			DirectoryChooser chooser = new DirectoryChooser();
			chooser.setTitle("JavaFX Projects");
			File selectedDirectory = chooser.showDialog(window);
			filepath = selectedDirectory.getAbsolutePath();
			if(filepath != null) {
				label.setText("Selected File Path: " + filepath);
				namingfield.setDisable(false);
				confirmButton.setDisable(false);
			}
			else {
				label.setText("Selected File Path: NONE");
				namingfield.setDisable(true);
				confirmButton.setDisable(true);
			}
		});
					
		HBox layout1 = new HBox(10);
		layout1.getChildren().addAll(namingfield, confirmButton);
		layout1.setAlignment(Pos.CENTER);
		//Creating Scene and Layout
		VBox layout2 = new VBox(10);
		layout2.getChildren().addAll(pathButton,label,layout1);
		layout2.setAlignment(Pos.CENTER);
				
		Scene scene = new Scene(layout2, 300, 300);
		window.setScene(scene);
		window.showAndWait();
		return filepath;
	}
}

