package Controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import Model.ENGINE_Client;
import Model.Field;
import Model.TileOnPositionEX;
import de.upb.swtpra1819interface.messages.*;
import de.upb.swtpra1819interface.models.Configuration;
import de.upb.swtpra1819interface.models.Tile;
import de.upb.swtpra1819interface.models.TileOnPosition;
import de.upb.swtpra1819interface.parser.*;
import enumeration.ControlParameters;



//import java.util.ArrayList;


/**
 * This Class is used to Manage the Clients Messages
 * It runs on one Thread.
 * 
 * This class will constantly read the Messagequeue and
 * send the Messages to the right Methods or Handlers
 * 
 * @author Lukas Tietz
 *
 */
public class ClientMessageHandler implements Runnable {
	Parser parser = new Parser();
	MessagesFromServerCaseHandling caseHandler = new MessagesFromServerCaseHandling();
	ENGINE_Client client;
	Logger logger = LogManager.getLogger();
	Brain brain;
	
	
	public ClientMessageHandler(ENGINE_Client engine_Client) {
		this.client = engine_Client;
	}
	
	public void run() {
		logger.info("OPERATIONAL");
		while (!this.client.getConnectionSocket().isClosed()) {
			try {
				logger.info("WAITING FOR NEW MESSAGE");
				Message msg = client.getMessages().take();
				if (msg == null)
					continue;
				
				int messageID = msg.getUniqueId();

				switch(messageID) {
				
				
				/*DisconnectSignal
				case(200):
					this.caseHandler.DisconnectSignal(msg);
					break;*/
					
				//GameListRespond : Updates the Lobby View
				case(301):
					this.caseHandler.GameListResponse(msg);
					break;
					
				//GameJoinAccepted
				case(303):
					GameJoinAccepted gja = this.caseHandler.GameJoinAccepted(msg);
					logger.log(Level.getLevel("GAMEJOIN"), "JOINED A GAME :" + gja.getGame());
					logger.log(Level.getLevel("GAMEJOIN"), "PLAYING IN THIS GAME :" + gja.getGame().getGameName());
					logger.log(Level.getLevel("GAMEJOIN"), "PLAYING IN THIS GAME :" + gja.getGame().getGameName());
					logger.log(Level.getLevel("GAMEJOIN"), "PLAYING IN THIS GAME :" + gja.getGame().getGameName());
					logger.log(Level.getLevel("GAMEJOIN"), "PLAYING IN THIS GAME :" + gja.getGame().getGameName());
					logger.log(Level.getLevel("GAMEJOIN"), "PLAYING IN THIS GAME :" + gja.getGame().getGameName());
					logger.log(Level.getLevel("GAMEJOIN"), "PLAYING IN THIS GAME :" + gja.getGame().getGameName());
					logger.log(Level.getLevel("GAMEJOIN"), "PLAYING IN THIS GAME :" + gja.getGame().getGameName());
					break;
				
				//SpectatorJoinAccepted
				case(305):
					this.caseHandler.SpectatorJoinAccepted(msg);
					break;
					
				//MessageSignal
				case(307):
					this.caseHandler.MessageSignal(msg);
					break;
				
				// StartGame
				case(400):
					Configuration config = this.caseHandler.StartGame(msg);
					this.brain = new Brain(this.client, new Field(config.getWrongMovePenalty(), config.getColorShapeCount()));
					logger.log(Level.getLevel("GAME"), "GAME HAS STARTED" );
					break;
				
				// EndGame
				case(401):
					this.caseHandler.EndGame(msg);
					break;
				
				// AbortGame
				case(402):
					this.caseHandler.AbortGame(msg);
					break;
				
				// PauseGame
				case(403):
					this.caseHandler.PauseGame(msg);
					break;
				
				// ResumeGame	
				case(404):
					this.caseHandler.ResumeGame(msg);
					break;
				
				// LeavingPlayer	
				case(406):
					this.caseHandler.LeavingPlayer(msg);
					break;
				
				// Winner
				case(407):
					this.caseHandler.Winner(msg);
					break;
				
				// StartTiles
				case(408):
					ArrayList<Tile> startTiles = this.caseHandler.StartTiles(msg);
					brain.addHandTiles(startTiles);
					logger.log(Level.getLevel("GAME"), "RECIEVED STARTTILES :" + startTiles );
					// TODO start calculating hand internal rows (possible for future concepts)
					break;
				
				// CurrentPlayer	
				case(409):
					CurrentPlayer cp = this.caseHandler.CurrentPlayer(msg);
					logger.log(Level.getLevel("GAME"), "RECIEVED CURRENTPLAYER NOTIFICATION [NAME: " + cp.getClient().getClientName() + " | ID: " + cp.getClient().getClientId() );
					if(cp.getClient().getClientId() == client.getClientID()) {
						logger.log(Level.getLevel("GAME"), "I'M THE CURRENT PLAYER! TIME TO MAKE MY MOVE!");
						brain.calculateMove();
					}else {
						continue;
					}
					break;
				
				// SendTiles
				case(410):
					ArrayList<Tile> newTiles = (ArrayList<Tile>) this.caseHandler.SendTiles(msg);
					brain.addHandTiles(newTiles);
					break;	
					
				//SwapTilesValid
				case(412):
					TileSwapValid tsv = this.caseHandler.SwapValidationSignal(msg);
					if(tsv.isValidation())
					{
						logger.log(Level.getLevel("TILESWAP"), "THE TILESWAP WAS VALID" );
						brain.handtiles.clear();
					}
					else
					{
						logger.log(Level.getLevel("TILESWAP"), "THE TILESWAP WAS INVALID AND HAS BEEN DENIED" );
					}
					break;
					
				//SwapTilesResponse
				case(413):
					TileSwapResponse tsr = this.caseHandler.SwapResponseSignal(msg);
					logger.log(Level.getLevel("TILESWAP"), "THE TILESWAP GAVE ME THESE TILES: " + tsr.getTiles() );
				    ArrayList<Tile> swapTiles = (ArrayList<Tile>) tsr.getTiles();
				    brain.addHandTiles(swapTiles);
					break;
				
				// MoveValid
				case(415):
					MoveValid mv = (MoveValid) msg;
					if(mv.isValidation()) {
						logger.log(Level.getLevel("PLAYTILES"), "MY MOVE WAS VALID");
						Iterator<Tile> it = brain.handtiles.iterator();
						while(it.hasNext()) {
							Tile tile = it.next();
							int id = tile.getUniqueId();
							for(TileOnPositionEX top : brain.current_best_move) {
								if(top.getUniqueId() == id) {
									logger.log(Level.getLevel("PLAYTILES"), "TILE THAT I PLAYED [ " + "X-COORD = " + top.getX() + " AND Y-COORD = " + top.getY() + ", COLOR = " + top.getTile().getColor() + " SHAPE = " + top.getTile().getShape() + " UNIQUEID = " + top.getTile().getUniqueId() + " ]");
									it.remove();
								}
							}
						}
						brain.current_best_move.clear();
						logger.log(Level.getLevel("PLAYTILES"), "MY CURRENT HAND: " + brain.handtiles);
					}
					else {
						logger.log(Level.getLevel("PLAYTILES"), "MY MOVE WAS NOT VALID!!!");
						brain.addHandTiles(brain.used_hand_tiles);
						brain.current_best_move.clear();
					}
					break;
					
				// Update
				case(416):
					logger.log(Level.getLevel("UPDATE"), "RECIEVING UPDATE");
					Update update = this.caseHandler.Update(msg);
					ArrayList<TileOnPositionEX> boardupdate = new ArrayList<>();
					for(TileOnPosition top : update.getUpdates()) {
						boardupdate.add(new TileOnPositionEX(top.getCoordX(), top.getCoordY(), top.getTile()));
					}
					brain.field.processMove(boardupdate, ControlParameters.Server);
					break;
				
				// TurnTimeLeftResponse
				case(420):
					this.caseHandler.TurnTimeLeftResponse(msg);
					break;
				
				// TotalTimeResponse
				case(422):
					this.caseHandler.TotalTimeResponse(msg);
					break;
				
				//BagResponse
				case(424):
					this.caseHandler.BagResponse(msg);
					break;
				
				//PlayerHandsResponse
				case(426):
					this.caseHandler.PlayerHandsResponse(msg);
					break;
					
				
				//GameDataResponse
				case(499):
					this.caseHandler.GameDataResponse(msg);
					break;
					
				// AccessDenied
				case(900):
					System.out.println(msg);
					break;
				// ParsingError
				case(910):
					System.out.println(msg);
					break;
				// Notallowed
				case(920):
					brain.addHandTiles(brain.used_hand_tiles);
					brain.current_best_move.clear();
					System.out.println(msg);
					break;
				
				}
			} 
			
			catch (InterruptedException e) {
				e.printStackTrace();
			}
			catch (RuntimeException rte) {
				rte.printStackTrace();
				try {
					client.disconnect();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} 
			catch (Throwable t) {
				t.printStackTrace();
			}
		}
	}
}

