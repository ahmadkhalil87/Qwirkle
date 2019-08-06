package _GUI_Application;
	
import client.Client;
import confirmation_tools.Popup_notification;
import confirmation_tools.YN_confirmation;
import game_elements.Game;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import server.Server;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;


public class Ausrichter extends Application {
	
	static Server server;
	static Boolean server_online = false;
	
	static ObservableList<Game> games = FXCollections.observableArrayList();
	static ObservableList<Client> clients= FXCollections.observableArrayList();
	
	@Override
	public void start(Stage primaryStage) {
		try {
			BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("Ausrichter.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			// Stopping user from accidently closing the Programm
			primaryStage.setOnCloseRequest(e -> {
					e.consume();
					closeProgramm();
				});
			primaryStage.setScene(scene);
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	/**
	 * Closes the programm properly
	 * Can be used to send scripts etc.
	 */
	public void closeProgramm() {
		boolean answer = YN_confirmation.display(" ","Are you sure you want to Quit?");
		if(answer) {
			Popup_notification.display(" ", "Goodbye!");
			System.exit(0);
		}
	}
}
