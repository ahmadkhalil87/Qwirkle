package confirmation_tools;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

//This Class creates an Yes/No-Confirmation Display
public class YN_confirmation {
	
	static Boolean answer = false;
	
	public static boolean display(String title, String message) {
		//Initialising the Window and its properties
		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		window.setMinWidth(300);
		
		//Adding title and Label to the Window
		window.setTitle(title);
		Label label = new Label();
		label.setText(message);
		
		//Yes-Button - Returns true as Boolean and closes window
		Button YesButton = new Button("Yes");
		YesButton.setOnAction(e -> {
			answer = true;
			window.close();
		});
		
		//No-Button - Returns false as Boolean and closes window
		Button NoButton = new Button("No");
		NoButton.setOnAction(e -> {
			answer = false;
			window.close();
		});
		
		//Creating Scene and Layout
		VBox layout = new VBox(10);
		layout.getChildren().addAll(YesButton,NoButton,label);
		layout.setAlignment(Pos.CENTER);
		
		Scene scene = new Scene(layout, 300, 300);
		window.setScene(scene);
		window.showAndWait();
		
		//Returning the answer (as boolean) that has been set through the buttons
		return answer;
	}

}
