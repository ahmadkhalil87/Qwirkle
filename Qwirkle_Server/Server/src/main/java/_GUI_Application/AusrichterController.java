package _GUI_Application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import com.google.gson.GsonBuilder;

import adjusted_messages.ConfigMessage;
import client.Client;
import confirmation_tools.LoadPathFinder;
import confirmation_tools.Popup_notification;
import confirmation_tools.SavePathFinder;
import confirmation_tools.YN_confirmation;
import de.upb.swtpra1819interface.messages.TileSwapValid;
import de.upb.swtpra1819interface.models.ClientType;
import de.upb.swtpra1819interface.models.Configuration;
import de.upb.swtpra1819interface.models.GameState;
import de.upb.swtpra1819interface.models.SlowMove;
import de.upb.swtpra1819interface.models.WrongMove;
import game_elements.Game;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import log4j.jfxAppender;
import server.StartServer;

public class AusrichterController {
	
	
	Game selected_game = null;
	Client selected_client = null;
	
	@FXML
	TextArea debug_console;
	@FXML
	TableView<Game> gamesTable = new TableView<Game>();
	@FXML
	TableView<Client> clientTable = new TableView<Client>();
	
	// Button declarations
	@FXML Button createGameButton; @FXML Button overviewButton; @FXML Button startGameButton; 
	@FXML Button abortGameButton; @FXML Button pauseGameButton; @FXML Button resumeGameButton; 
	@FXML Button serverButton; @FXML Button forcejoinButton;
	
	// Textfield declarations
	@FXML TextField PortField;
	
	// Label declarations
	@FXML Label serverLabel; @FXML Label debugLabel; @FXML Label portLabel; @FXML Label IPLabel;
	
	
	/**
	 * All items needed for the GameConfiguration Stackpane
	 */
	@FXML TextField playercount_Input = new TextField(); @FXML TextField turntimer_Input; @FXML TextField handtilecount_Input = new TextField(); @FXML TextField variety_Input = new TextField(); @FXML TextField gamename_Input = new TextField();
	@FXML TextField wrongmove_Input = new TextField(); @FXML TextField overtime_Input = new TextField(); @FXML ChoiceBox<String> wrongmovechoicebox = new ChoiceBox<String>();@FXML ChoiceBox<String> overtimechoicebox = new ChoiceBox<String>();
	@FXML TextField tilecount_Input = new TextField(); @FXML TextField timevisual_Input = new TextField();
	
	@FXML Button applyChangesButton; @FXML Button saveConfigButton; @FXML Button loadConfigButton;
	@FXML Button makeGameButton;
	
	@FXML ListView<String> gameconfigList = new ListView<String>();
	
	/**
	 * Stackpanes
	 */
	@FXML StackPane serverOverView = new StackPane();
	@FXML StackPane gameConfigurator = new StackPane();
	private boolean applied = false;
	
	/**
	 * Standard Game Configuration
	 */
	static String game_name = "null";
	static int player_count = 2;
	static int tile_count = 2;
	static int colour_variety = 6;
	static int symbol_variety = 6;
	static long turn_timer = 120;
	static long time_visual = 1000;
	static int tile_in_hand = 6;
	static String punishment_wrong_move = "None";
	static int punishment_wrong_move_lost = 1;
	static String punishment_overtime = "Point Loss";
	static int punishment_overtime_lost = 1;
	static ObservableList<String> items = FXCollections.observableArrayList();
	
//  ############################################ GENERAL SETUP
	@FXML
	public void initialize() {
		// Setting Labels
		IPLabel.setText("NONE");
		portLabel.setText("NONE");
		
		// Setting Textarea up to act as Debug Console
		jfxAppender.setTextArea(debug_console);
		
		// Setting up Textfields
		textfieldSetup();
		
		//Setting up Choiceboxes
		wrongmovechoicebox.getItems().addAll("Kick","None","Point Loss");
		wrongmovechoicebox.getSelectionModel().select(punishment_wrong_move);
		overtimechoicebox.getItems().addAll("Kick","Point Loss");
		overtimechoicebox.getSelectionModel().select(punishment_overtime);
		
		//Setting up ListView
		gameconfigList.setItems(items);
		
		//Giving functionality to the apply button
		applyChangesButton.setOnAction(e -> {
			Boolean answer = YN_confirmation.display(" ", "Are you sure?");
			if(answer) {
//				//Function that sets all Configurations
//				Popup_notification.display("", "Changes will be applied");
				applyChanges();
				applied = true;
			}
		});
		
		// ########################### COLUMNS FOR ClIENT LIST ########################################
		TableColumn<Client, Integer> pidColumn = new TableColumn<>("clientID");
		pidColumn.setResizable(false);
		pidColumn.setCellValueFactory(new PropertyValueFactory<Client, Integer>("clientId"));
		
				
		TableColumn<Client, String> pnColumn = new TableColumn<>("clientName");
		pnColumn.setResizable(false);
		pnColumn.setCellValueFactory(new PropertyValueFactory<Client, String>("clientName"));
		
				
		TableColumn<Client, String> ctColumn = new TableColumn<>("clientType");
		ctColumn.setResizable(false);
		ctColumn.setCellValueFactory(new PropertyValueFactory<Client, String>("clientType"));
		
				
		TableColumn<Client, Boolean> cigColumn = new TableColumn<>("Ingame");
		cigColumn.setResizable(false);
		cigColumn.setCellValueFactory(new PropertyValueFactory<Client, Boolean>("isIngame"));
		// ########################################################################################################
				
		clientTable.getColumns().add(pidColumn);	
		clientTable.getColumns().add(pnColumn);
		clientTable.getColumns().add(ctColumn);
		clientTable.getColumns().add(cigColumn);

		// ########################### COLUMNS FOR NOT STARTED GAMES LIST ########################################
		TableColumn<Game, Integer> idColumn = new TableColumn<>("GameID");
		idColumn.setResizable(false);
		idColumn.setCellValueFactory(new PropertyValueFactory<Game, Integer>("gameId"));
		
					
		TableColumn<Game, String> nColumn = new TableColumn<>("Game's name");
		nColumn.setResizable(false);
		nColumn.setCellValueFactory(new PropertyValueFactory<Game, String>("gameName"));
		
				
		// Game status (e.g. On-Going, Paused,...)
		TableColumn<Game, GameState> stateColumn = new TableColumn<>("Status");
		stateColumn.setResizable(false);
		stateColumn.setCellValueFactory(new PropertyValueFactory<Game, GameState>("gameState"));
		
		TableColumn<Game, String> playerColumn = new TableColumn<>("Players");
		playerColumn.setResizable(false);
		playerColumn.setCellValueFactory(new PropertyValueFactory<Game, String>("playerInGame"));
		// ########################################################################################################
		
		gamesTable.getColumns().add(idColumn);	
		gamesTable.getColumns().add(nColumn);
		gamesTable.getColumns().add(stateColumn);
		gamesTable.getColumns().add(playerColumn);
	
	
		gamesTable.setItems(Ausrichter.games);
		clientTable.setItems(Ausrichter.clients);
		
		gamesTable.setRowFactory(tv -> {TableRow<Game> row = new TableRow<>();
		row.setOnMouseClicked(event -> { 
			if (event.getClickCount() == 1 && (!row.isEmpty())){
				selected_game = row.getItem();
				System.out.println("SELECTED GAME: " + selected_game.toString());
				GameButtonControl();
				}
			});
			return row;
		});
		
		clientTable.setRowFactory(tv -> {TableRow<Client> row = new TableRow<>();
		row.setOnMouseClicked(event -> { 
			if (event.getClickCount() == 1 && (!row.isEmpty())){
				selected_client = row.getItem();
				System.out.println("SELECTED CLIENT: " + selected_client.toString());
				GameButtonControl();
				}
			});
			return row;
		});
		
		showOverView();
		
		/**
		 * Thread to keep tables up to date
		 * (1 Update every Second)
		 */
		Thread tablerefresher = new Thread("Auth. Thread") {
			public void run() {
				while(true) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					Platform.runLater(() -> {
						refreshTables();
					});
				}
			}
		};
		tablerefresher.setDaemon(true);
		tablerefresher.start();
	}
		
	
	
	
	private void textfieldSetup() {
		
		playercount_Input.setTextFormatter(new TextFormatter<>(c -> {
            if (c.isContentChange()) {
                if (c.getControlNewText().length() == 0) {
                    return c;
                }
                try {
                    Integer.parseInt(c.getControlNewText());
                    return c;
                } catch (NumberFormatException e) {
                }
                return null;
            }
            return c;
          
        }));
		
		turntimer_Input.setTextFormatter(new TextFormatter<>(c -> {
            if (c.isContentChange()) {
                if (c.getControlNewText().length() == 0) {
                    return c;
                }
                try {
                    Integer.parseInt(c.getControlNewText());
                    return c;
                } catch (NumberFormatException e) {
                }
                return null;
            }
            return c;
        }));
		
		variety_Input.setTextFormatter(new TextFormatter<>(c -> {
            if (c.isContentChange()) {
                if (c.getControlNewText().length() == 0) {
                    return c;
                }
                try {
                    Integer.parseInt(c.getControlNewText());
                    return c;
                } catch (NumberFormatException e) {
                }
                return null;
            }
            return c;
        }));
		
		handtilecount_Input.setTextFormatter(new TextFormatter<>(c -> {
            if (c.isContentChange()) {
                if (c.getControlNewText().length() == 0) {
                    return c;
                }
                try {
                    Integer.parseInt(c.getControlNewText());
                    return c;
                } catch (NumberFormatException e) {
                }
                return null;
            }
            return c;
        }));
		
		tilecount_Input.setTextFormatter(new TextFormatter<>(c -> {
            if (c.isContentChange()) {
                if (c.getControlNewText().length() == 0) {
                    return c;
                }
                try {
                    Integer.parseInt(c.getControlNewText());
                    return c;
                } catch (NumberFormatException e) {
                }
                return null;
            }
            return c;
        }));
		
		timevisual_Input.setTextFormatter(new TextFormatter<>(c -> {
            if (c.isContentChange()) {
                if (c.getControlNewText().length() == 0) {
                    return c;
                }
                try {
                    Integer.parseInt(c.getControlNewText());
                    return c;
                } catch (NumberFormatException e) {
                }
                return null;
            }
            return c;
        }));
		
		wrongmove_Input.setTextFormatter(new TextFormatter<>(c -> {
            if (c.isContentChange()) {
                if (c.getControlNewText().length() == 0) {
                    return c;
                }
                try {
                    Integer.parseInt(c.getControlNewText());
                    return c;
                } catch (NumberFormatException e) {
                }
                return null;
            }
            return c;
        }));
		
		wrongmovechoicebox.setOnAction(event -> {
	        if (wrongmovechoicebox.valueProperty().get().equals("Point Loss")) {
	        	wrongmove_Input.setVisible(true);
	        }
	        else {
	        	wrongmove_Input.setVisible(false);
	        }
	    });
		
		overtime_Input.setTextFormatter(new TextFormatter<>(c -> {
            if (c.isContentChange()) {
                if (c.getControlNewText().length() == 0) {
                    return c;
                }
                try {
                    Integer.parseInt(c.getControlNewText());
                    return c;
                } catch (NumberFormatException e) {
                }
                return null;
            }
            return c;
        }));

		//Show the punishment_overtime_lost Input_field when the choise is 'Punktabzug'
		overtimechoicebox.setOnAction(event -> {
	        if (overtimechoicebox.valueProperty().get().equals("Point Loss")) {
	        	overtime_Input.setVisible(true);
	        }
	        else {
	        	overtime_Input.setVisible(false);
	        }
	    });
		
	}


	@FXML
	private void showOverView() {
		gameConfigurator.setVisible(false);
		gameConfigurator.setDisable(true);
		
		serverOverView.setVisible(true);
		serverOverView.setDisable(false);
	}
	
	@FXML
	private void showcreateGame() {
		gameConfigurator.setVisible(true);
		gameConfigurator.setDisable(false);
		
		serverOverView.setVisible(false);
		serverOverView.setDisable(true);
	}
	
	@FXML
	public void refreshTables() {
		gamesTable.refresh();
		clientTable.refresh();
	}
	
	//Used to close the Program properly
	public void closeProgramm() {
		boolean answer = YN_confirmation.display(" ","Are you sure you want to Quit?");
		if(answer) {
			Popup_notification.display(" ", "Goodbye!");
			System.exit(0);
		}
	}

//  ############################################ [END] GENERAL SETUP
	
//  ############################################ SERVEROVERVIEW
	@FXML
	private void serverButtonInvoke() throws UnknownHostException {
		if (Ausrichter.server_online == false) {
			serverButton.setDisable(true);
			int server_port = Integer.parseInt(PortField.getText());
			if (server_port < 65535 | server_port > 1) {
				debugLabel.setText("Initialising Server { Port: " + server_port + " }" );
				StartServer start = new StartServer(server_port);
				Ausrichter.server = start.getServer();
				Ausrichter.server.start();
				Ausrichter.server.setUpdater(new AusrichterUpdater());
				
				if (Ausrichter.server.isOnline()) {
					Ausrichter.server_online = true;
					debugLabel.setText("Server is now online");
					IPLabel.setText(InetAddress.getLocalHost().toString());
					portLabel.setText(PortField.getText());
					createGameButton.setDisable(false);
					serverButton.setDisable(false);
				    serverButton.setText("Disconnect Server");
				    return;
				}
				else {
					serverButton.setDisable(false);
					debugLabel.setText("Server did not successfully start");
					return;
				}
		    }
			else {
				serverButton.setDisable(false);
				debugLabel.setText("Invalid Port");
				return;
			}
			
		}
		
		else {
			new YN_confirmation();
			boolean answer = YN_confirmation.display("Server Shutdown", "Are you sure?");
			if (answer == true) {
				try {
					Ausrichter.server.shutDown();
					Ausrichter.server_online = false;
					debugLabel.setText("Server has been shut down");
					createGameButton.setDisable(true);
				    serverButton.setText("Start Server");
				    IPLabel.setText("NONE");
					portLabel.setText("NONE");
				    
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
	
	@FXML
	public void PauseGame() throws InterruptedException {
		Game selected = Ausrichter.server.getGame(selected_game.getGameId());
		selected.PauseGame();
		GameButtonControl();
	}
	
	@FXML
	public void ResumeGame() throws InterruptedException {
		Game selected = Ausrichter.server.getGame(selected_game.getGameId());
		selected.ResumeGame();
		GameButtonControl();
	}
	
	@FXML
	public void AbortGame() {
		Game selected = Ausrichter.server.getGame(selected_game.getGameId());
		//TODO: insert disconnect function - (isingame(false), isplayer(false),...)
		/*
		for (Client client : selected.getClients()) {
			client.setIsIngame(false);
		}
		*/
		selected.AbortGame();
		GameButtonControl();
	}
	
	/**
	 * Method used to Start a game
	 */
	@FXML
	public void EventStartGame() {
		Game selected = Ausrichter.server.getGame(selected_game.getGameId());
		try {
			selected.StartGame();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		GameButtonControl();
		return;
	}
	
	/**
	 * Method used to forcejoin clients into game
	 */
	@FXML
	public void forcejoin() {
		if (selected_game == null) {
			Popup_notification.display("Warning", "No game selected");
		}
		else {
			Game selected = Ausrichter.server.getGame(selected_game.getGameId());
			selected.ConnectClient(selected_client);
			GameButtonControl();
			return;
		}
		return;
	}
	
	/**
	 * Method used to control the availability of buttons .
	 */
	@FXML
	private void GameButtonControl() {
		if(selected_game == null) {
			forcejoinButton.setDisable(true);
			startGameButton.setDisable(true);
			abortGameButton.setDisable(true);
			pauseGameButton.setDisable(true);
			resumeGameButton.setDisable(true);
			return;
		}
		if(selected_game.getGameState() == GameState.NOT_STARTED){
			if(selected_game.getPlayers().size() >= 2) {
				startGameButton.setDisable(false);
			}
			if(selected_client == null) {forcejoinButton.setDisable(true);}
			else{
				if(selected_client.getClientType() == ClientType.PLAYER) {
					if(selected_client.getIsIngame() == true) {
						forcejoinButton.setDisable(true);
						return;
					}
					if(selected_game.getPlayers().size() < selected_game.getConfig().getMaxPlayerNumber()) {
						forcejoinButton.setDisable(false);
					} else {
						forcejoinButton.setDisable(true);
					}
				} else {
					forcejoinButton.setDisable(false);
				}
			}
			abortGameButton.setDisable(false);
			pauseGameButton.setDisable(true);
			resumeGameButton.setDisable(true);
			return;
			
		} 
		
		if(selected_game.getGameState() != GameState.ENDED){
			if(selected_client != null) {
				if(selected_client.getClientType() == ClientType.PLAYER || selected_client.getIsIngame() == true) {
					forcejoinButton.setDisable(true);
				} else {
					forcejoinButton.setDisable(false);
				}
			}
			if(selected_game.getGameState() == GameState.PAUSED) {
				pauseGameButton.setDisable(true);
				resumeGameButton.setDisable(false);
			}
			startGameButton.setDisable(true);
			abortGameButton.setDisable(false);
			pauseGameButton.setDisable(false);
			resumeGameButton.setDisable(true);
			return;
		} 
		
		else {
			forcejoinButton.setDisable(true);
			startGameButton.setDisable(true);
			abortGameButton.setDisable(true);
			pauseGameButton.setDisable(true);
			resumeGameButton.setDisable(true);
			return;
		}
		
	}
//  ############################################ [END] SERVEROVERVIEW
	
//  ############################################ GAME CONFIGURATOR
	@FXML
	private void applyChanges() {
		try {
			int temp1 = Integer.parseInt(playercount_Input.getText());
			if (temp1>1) {
				player_count = temp1;
			}
			else {
				player_count = 2;
				Popup_notification.display("Warning", "player_count False Input" + "\nValue will be reset to default (" + player_count + ")");
			}
		}
		catch (NumberFormatException e) {
			player_count = 2;
		}
		try {
			int temp2 = Integer.parseInt(turntimer_Input.getText());
			if (temp2>0) {
				turn_timer = temp2;
			}
			else {
				turn_timer = 120;
				Popup_notification.display("Warning", "turn_timer False Input" + "\nValue will be reset to default (" + turn_timer + ")");
			}
		}
		catch (NumberFormatException e) {
			turn_timer = 120;
		}
		try {
			int temp3 = Integer.parseInt(variety_Input.getText());
			if (temp3>1 && temp3<12) {
				colour_variety = temp3;
				symbol_variety = temp3;
			}
			else {
				colour_variety = 6;
				symbol_variety = 6;
				Popup_notification.display("Warning", "colour & symbol val False Input" + "\nValue will be reset to default (" + colour_variety + ")");
			}
		}
		catch (NumberFormatException e) {
			colour_variety = 6;
			symbol_variety = 6;
		}
		try {
			int temp4 = Integer.parseInt(handtilecount_Input.getText());
			if (temp4>0) {
				tile_in_hand = temp4;
			}
			else {
				tile_in_hand = 6;
				Popup_notification.display("Warning", "tile_in_hand False Input" + "\nValue will be reset to default (" + tile_in_hand + ")");
			}
		}
		catch (NumberFormatException e) {
			tile_in_hand = 6;
		}
		try {
			int temp8 = Integer.parseInt(timevisual_Input.getText());
			if (temp8>0) {
				time_visual = temp8;
			}
			else {
				time_visual = 5000;
				Popup_notification.display("Warning", "time_visual False Input" + "\nValue will be reset to default (" + time_visual + ")");
			}
		}
		catch (NumberFormatException e) {
			time_visual = 5000;
		}
		try {
			int temp9 = Integer.parseInt(tilecount_Input.getText());
			if (temp9>0) {
				tile_count = temp9;
			}
			else {
				tile_count = 6;
				Popup_notification.display("Warning", "tile_count False Input" + "\nValue will be reset to default (" + tile_count + ")");
			}
		}
		catch (NumberFormatException e) {
			tile_count = 6;
		}
		String temp5 = gamename_Input.getText();//temp5!="" | temp5!=null | 
		if (gamename_Input.getLength()>1) {
			if (temp5.length()<15) {
				game_name = temp5;
			}
			else {
				game_name=temp5.substring(0, 14);
			}
		}
		else {
			game_name = "null";
		}
		punishment_wrong_move = wrongmovechoicebox.getValue();
		punishment_overtime = overtimechoicebox.getValue();

		items.clear();
		String line;
		items.add("Current Settings:");
		line = "Game Name: " + game_name;
		items.add(line);
		line = "Playercount : " + player_count;
		items.add(line);
		line = "Turntimer : " + turn_timer;
		items.add(line);
		line = "colour variety : " + colour_variety;
		items.add(line);
		line = "symbol variety : " + symbol_variety;
		items.add(line);
		line = "Count of Tiles in the Hand: " + tile_in_hand;
		items.add(line);
		line = "Tile Frequency: " + tile_count;
		items.add(line);
		line = "Time Visualization: " + time_visual;
		items.add(line);
		if (punishment_wrong_move == "Point Loss") {
			try {
				int temp6 = Integer.parseInt(wrongmove_Input.getText());
				if (temp6>0) {
				punishment_wrong_move_lost = temp6;
				}
				else {
				punishment_wrong_move_lost = 1;
			}
		}
		catch (NumberFormatException e) {
			punishment_wrong_move_lost = 1;
		}
		line = "Punishment (WM): " + punishment_wrong_move_lost + " " + punishment_wrong_move;
		items.add(line);
	}
	else {
		line = "Punishment (WM) : " + punishment_wrong_move;
		items.add(line);
	}
	if (punishment_overtime == "Point Loss") {
		try {
			int temp7 = Integer.parseInt(overtime_Input.getText());
			if (temp7>0) {
				punishment_overtime_lost = temp7;
			}
			else {
				punishment_overtime_lost = 1;
			}
		}
		catch (NumberFormatException e) {
			punishment_overtime_lost = 1;
		}
		line = "Punishment (OT) : " + punishment_overtime_lost + " " + punishment_overtime;
		items.add(line);
	}
	else {
		line = "Punishment (OT) : " + punishment_overtime;
		items.add(line);
	}
	setTexts();
	saveConfigButton.setDisable(false);
	}
	
	@FXML
	public void makeGame() {
		if(applied  == false) {applyChanges();}
		Boolean answer = YN_confirmation.display("Confirmation", "Are you sure you want to create a Game?");
	
		if(answer) {
			
			try {
				Configuration config = createConfig();
				ConfigMessage configtoserver = new ConfigMessage(config, game_name);
				Ausrichter.server.getConfig().add(configtoserver);
				Popup_notification.display("Success", "The Game has been made.");
			} catch (Exception e2) {
				Popup_notification.display("Warning", "The Game can't be made./n"+e2.getMessage());
			}
		}
		else {
			Popup_notification.display("Warning", "you don't want to make a Game!!!");
		}
		
	}
	/**
	 * Method used to visualize the Changes to the User
	 */
	public void setTexts() {
		tilecount_Input.setText(Integer.toString(tile_count));
		handtilecount_Input.setText(Integer.toString(tile_in_hand));
		overtimechoicebox.getSelectionModel().select(punishment_overtime);
		if(punishment_overtime == "Point Loss") {
			overtime_Input.setText(Integer.toString(punishment_overtime_lost));
		}
		wrongmovechoicebox.getSelectionModel().select(punishment_wrong_move);
		if(punishment_wrong_move == "Point Loss") {
			wrongmove_Input.setText(Integer.toString(punishment_overtime_lost));
		}
		variety_Input.setText(Integer.toString(colour_variety));
		playercount_Input.setText(Integer.toString(player_count));
		turntimer_Input.setText(Long.toString(turn_timer));
		timevisual_Input.setText(Long.toString(time_visual));
		gamename_Input.setText(game_name);
	}
	
	@FXML
	public void saveConfig() {
		System.out.println("PRESSED SAVE CONFIG BUTTON");
		String filepath = new SavePathFinder().display();
		Configuration configtosave = createConfig();
		String objtosave = new GsonBuilder().create().toJson(configtosave);
		try (FileWriter file = new FileWriter(filepath)){
			file.write(objtosave);
		} catch (IOException e) {
				// TODO Auto-generated catch block
			e.printStackTrace();
		};
			
	}
	
	@FXML
	public void loadConfig() throws FileNotFoundException {
		System.out.println("PRESSED LOAD CONFIG BUTTON");
		String filepath = new LoadPathFinder().display();
		File file = new File(filepath);
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String content = null;
		try {
			content = reader.readLine();
			setupConfig(content);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return;
	}
	
	@FXML
	public Configuration createConfig() {
		if (!items.isEmpty()) {
			long t1 = turn_timer;
			long t2 = time_visual;
			WrongMove t3 = WrongMove.NOTHING;
			switch (punishment_wrong_move) {
			case "None":
				t3 = WrongMove.NOTHING;
				break;
			case "Point Loss":
				t3 = WrongMove.POINT_LOSS;
				break;
			case "Kick":
				t3 = WrongMove.KICK;
				break;
			}
			SlowMove t4 = SlowMove.POINT_LOSS;
			switch (punishment_overtime) {
			case "Point Loss":
				t4 = SlowMove.POINT_LOSS;
				break;
			case "Kick":
				t4 = SlowMove.KICK;
				break;
			}
			Configuration config = new Configuration(symbol_variety, tile_count, tile_in_hand,
						t1, t2, t3, punishment_wrong_move_lost, t4, punishment_overtime_lost, player_count);
			return config;
		}
		else {
			Popup_notification.display("Warning", "No Game Configurations");
			return null;
		}
		
	}

	
	/**
	 * Setup Config
	 */
	public void setupConfig(String configjson) {
		Configuration configuration = new GsonBuilder().create().fromJson(configjson, Configuration.class);
		player_count = configuration.getMaxPlayerNumber();
		colour_variety = configuration.getColorShapeCount();
		symbol_variety = configuration.getColorShapeCount();
		turn_timer = configuration.getTurnTime();
		tile_in_hand = configuration.getMaxHandTiles();
		tile_count = configuration.getTileCount();
		time_visual = configuration.getTimeVisualization();
		punishment_overtime = configuration.getSlowMove().toString();
		punishment_overtime_lost = configuration.getSlowMovePenalty();
		punishment_wrong_move = configuration.getWrongMove().toString();
		punishment_wrong_move_lost = configuration.getWrongMovePenalty();
		setTexts();
	}
//  ############################################ [END] GAME CONFIGURATOR
}// [END] Class
