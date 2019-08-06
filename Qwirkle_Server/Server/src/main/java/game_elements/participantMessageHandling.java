package game_elements;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import com.google.gson.GsonBuilder;

import adjusted_messages.ClientMessage;
import adjusted_messages.GameDataResponse;
import adjusted_messages.PlayTiles;
import adjusted_messages.Update;
import client.Client;
import de.upb.swtpra1819interface.messages.AccessDenied;
import de.upb.swtpra1819interface.messages.MoveValid;
import de.upb.swtpra1819interface.messages.NotAllowed;
import de.upb.swtpra1819interface.messages.ScoreResponse;
import de.upb.swtpra1819interface.messages.TileSwapRequest;
import de.upb.swtpra1819interface.messages.TotalTimeResponse;
import de.upb.swtpra1819interface.messages.TurnTimeLeftResponse;
import de.upb.swtpra1819interface.models.ClientType;
import de.upb.swtpra1819interface.models.GameState;
import de.upb.swtpra1819interface.parser.Parser;
import gameValidation.CheckProcessObject;
import gameValidation.TileOnPositionEX;
import gameboard.Field;
import gameboard.TileOnPosition;


/**
 * This Class will run a Thread that once initiated will
 * Handle messages regarding Moves that players are initiating.
 * It acts as the games referee.
 * @author Lukas Tietz
 */
class participantMessageHandling implements Runnable {
	
	MessageToGameCaseHandling caseHandler;
	Game game;
	private Logger logger;
	
	public participantMessageHandling(Game game)
	{
		this.caseHandler = new MessageToGameCaseHandling(game);
		this.game = game;
		this.logger = game.logger;
	}
	
	public void run() {
		try {
			while (game.getGameState() != GameState.ENDED) {
			ClientMessage msg = game.getParticipantMessages().take();
			Client client = msg.getClient();
			logger.log(Level.getLevel("GAME"), this + " HANDLING MESSAGE: " + msg.getClientMessage() );
							
			int messageID = msg.getClientMessage().getUniqueId();
			switch(messageID) {
				
			//SwapTilesRequest
			case(411):
				if(client.getClientType() == ClientType.PLAYER)
				{
					if(client.getClientId() == game.getCurrentplayer().getClientId()) {
						caseHandler.SwapRequestSignal(msg.getClientMessage(), client);
						break;
					}
					else {
						client.sendMessage(new NotAllowed("Its not your turn!", messageID));
						break;
					}
					
				}
				else {
					client.sendMessage(new AccessDenied("You are not a player!", messageID));
					break;
				}
			//playTiles
			case(414):
				if(client.getClientType() == ClientType.PLAYER)
				{
					if(client.getClientId() == game.getCurrentplayer().getClientId()) {
						game.Timer.setinput("DONE");
						Parser parser = new Parser();
						String msgstring = parser.serialize(msg.getClientMessage());
						de.upb.swtpra1819interface.messages.PlayTiles ptm = new GsonBuilder().create().fromJson(msgstring, de.upb.swtpra1819interface.messages.PlayTiles.class);
						
						ArrayList<TileOnPositionEX> playtiles = new ArrayList<>();
						for(de.upb.swtpra1819interface.models.TileOnPosition tile : ptm.getTiles()) {
							playtiles.add(new TileOnPositionEX(tile.getCoordX(), tile.getCoordY(), tile.getTile()));
						}
						logger.log(Level.getLevel("GAME"), "TILES RECIEVED : " + playtiles  );
						CheckProcessObject result = this.caseHandler.PlayTilesSignal(playtiles, client);
						if(result.isValidity()) {
							game.nextPlayerCheck();
						}
						else {
							client.sendMessage(new MoveValid(false, "YOUR MOVE WAS INVALID"));
							game.nextPlayerCheck();
						}
					}
					else {
						client.sendMessage(new NotAllowed("Its not your turn!", messageID));
					}
				}
				break;
			
			// ScoreRequest
			case(417):
				if(game.getGameState() != GameState.NOT_STARTED) {
					Map<de.upb.swtpra1819interface.models.Client, Integer> ScoreBoardFormatted = new HashMap<de.upb.swtpra1819interface.models.Client, Integer>(); 
					for(Client player : game.getScoreBoard().keySet()) {
						de.upb.swtpra1819interface.models.Client curpl = player.clientformatter();
						int score = game.getScoreBoard().get(player);
						ScoreBoardFormatted.put(curpl, score);
					}
					client.sendMessage(new ScoreResponse(ScoreBoardFormatted));
				}
				break;
			// Turntimeleft
			case(419):
				client.sendMessage(new TurnTimeLeftResponse(game.Timer.getCurrent_turn_time()));
				break;
			
			// TotaltimeReq
			case(421):
				client.sendMessage(new TotalTimeResponse(game.Timer.getTotaltime()));
				break;
					
			//BagRequest
			case(423):
				if(client.getClientType()  == ClientType.SPECTATOR)
				{
					caseHandler.BagRequest(msg.getClientMessage(), client);
					break;
				}
				else { 
					client.sendMessage(new NotAllowed("You are a Player", messageID));
					break;
					}

					
			// PlayerHandsResponse
			case(425):
				if (client.getClientType() == ClientType.SPECTATOR) {
					caseHandler.playerHandsResponse(msg.getClientMessage(), client);
					break;
				}else {
					client.sendMessage(new NotAllowed("Trying to cheat I see. Bad Boy, Bad!", messageID));
					break;
				}

			// GameDataRequest
			case(498):
				caseHandler.GameDataRequest(msg.getClientMessage(), client);
				break;
			}
			}

		} catch (InterruptedException e) {
				e.printStackTrace();
		}finally {
			logger.log(Level.getLevel("GAME"), this + " has come to an end or encountered a Problem, shutting down" );
		}
	}
}

