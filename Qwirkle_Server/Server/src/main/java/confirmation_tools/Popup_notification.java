package confirmation_tools;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

//This Class creates an Pop-Up Display
public class Popup_notification {
	
	public static void display(String title, String message) {
		//Initialising the Window and its properties
		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		window.setMinWidth(300);
		
		//Adding title and Label to the Window
		window.setTitle(title);
		Label label = new Label();
		label.setText(message);
		
		//OK-Button - Confirmation that User has read the Message
		Button OkButton = new Button("Ok");
		OkButton.setOnAction(e -> window.close());
		
		//Creating Scene and Layout
		VBox layout = new VBox(10);
		layout.getChildren().addAll(OkButton,label);
		layout.setAlignment(Pos.CENTER);
		
		Scene scene = new Scene(layout, 300, 300);
		window.setScene(scene);
		window.showAndWait();
	}

}


