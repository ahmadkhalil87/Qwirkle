package server;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import adjusted_messages.ConfigMessage;
import de.upb.swtpra1819interface.models.Configuration;
import de.upb.swtpra1819interface.models.GameState;
import de.upb.swtpra1819interface.models.Tile;
import game_elements.Bag;
import game_elements.Game;
import gameboard.Field;
import gameboard.TileOnPosition;

/**
 * Constantly tries to grab configurations from the Configuration Queue.
 * If a configuration is available, Instantiates a new Game with given configuration.
 * @author Lukas
 *
 */
 class GameMaker implements Runnable{

	 Server server;
	 Logger logger = LogManager.getLogger();

	 public GameMaker(Server server) {
		this.server = server;
	}

	public void run() {
		logger.log(Level.getLevel("GAME"), "GameMaker Thread: " + this + " initialized");
		while(server.online) {
			ConfigMessage gameConfig = null;
			try {
				gameConfig = server.configurations.take();
				logger.log(Level.getLevel("GAME"), "Recieved new GameConfig :" + gameConfig);
				Configuration config = gameConfig.getConfig();
				String GameName = gameConfig.getGameName();
				if (gameConfig == null)
					continue;

				int gameID = 0;
				while (gameID == 0 || server.hasGameID(gameID))
					gameID += 1;
				
				Game newGame = null;
				
				if(GameName == "null") {
					newGame = new Game(gameID, "Game #" + gameID, GameState.NOT_STARTED, false, config);
				}
				else {
					newGame = new Game(gameID, GameName, GameState.NOT_STARTED, false, config);
				}
				// initiate game bag
				if (newGame.getBag() == null){
					newGame.setBag(new Bag());
				}

				for (int shape=0; shape<newGame.getConfig().getColorShapeCount(); shape++){
					for (int color=0; color<newGame.getConfig().getColorShapeCount(); color++){
						for (int occ=0; occ<newGame.getConfig().getTileCount(); occ++){
							newGame.getBag().addTile(new Tile(color, shape, shape*12+color));
						}
					}
				}
				newGame.setPlayboard(new Field(newGame.getConfig().getWrongMovePenalty(), newGame.getConfig().getColorShapeCount()));

				logger.log(Level.getLevel("GAME"), "BAG HAS BEEN SET :" + newGame.getBag().getAllTiles() + " TILECOUNT = " + newGame.getBag().getSize());
				logger.log(Level.getLevel("GAME"), "FIELD INITIALIZED :" + newGame.getPlayboard());

				server.ActiveGames.add(newGame);
				server.getUpdater().newGameEvent(newGame);
				logger.log(Level.getLevel("GAME"), "New Game: { Name: " + newGame.getGameName() + " ID: " + newGame.getGameId() + " } has been made.");

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
 }
