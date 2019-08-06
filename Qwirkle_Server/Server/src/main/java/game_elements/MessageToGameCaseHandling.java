package game_elements;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.GsonBuilder;

import client.Client;
import de.upb.swtpra1819interface.messages.*;
import adjusted_messages.*;
import adjusted_messages.GameDataResponse;
import adjusted_messages.PlayTiles;
import adjusted_messages.PlayerHandsResponse;
import adjusted_messages.Update;
import de.upb.swtpra1819interface.models.ClientType;
import de.upb.swtpra1819interface.models.Configuration;
import de.upb.swtpra1819interface.models.GameState;
import de.upb.swtpra1819interface.models.Tile;
import de.upb.swtpra1819interface.models.WrongMove;
import de.upb.swtpra1819interface.parser.Parser;
import gameValidation.CheckProcessObject;
import gameValidation.TileOnPositionEX;
import gameboard.TileOnPosition;

/**
 * This Class holds all the Responses for the Messages sent to the Bame by the Client.
 * The Messages that are being read are getting build into their respective classes and returned
 * for further use.
 * @author Lukas Tietz, Kai Kreiner
 *
 */
public class MessageToGameCaseHandling
{
	Game game;
	Configuration config;
	Parser parser = new Parser();
	Logger logger = LogManager.getLogger();

	public MessageToGameCaseHandling(Game game) {
		this.game = game;
		this.config = game.getConfig();
	}

	//##############################################################################################################################
													//GameMessages
	//##############################################################################################################################


	public void LeavingRequest(Client client) throws InterruptedException {
		game.sendAll(new LeavingPlayer(client.clientformatter()));
		game.disconnectClient(client);
		game.sendMessage(new ClientMessage(client, new MessageSend("left the Game")));
		if (game.getPlayers().size() <= 1) {
			game.EndGame();
		}
	}

	/**
	 * Method to handle incoming Chat Messages
	 * Only implemented here for compatability with other SWTPRA clients.
	 * @param msg
	 */
	public void MessageSend(Message msg, Client client) {
		//Checking if Client is ingame
		if (client.getIsIngame() == false) {
			client.sendMessage(new NotAllowed("You are not participating in a Game.", 0));
			return;
		}
		String msmessage = parser.serialize(msg);
		MessageSend ms = new GsonBuilder().create().fromJson(msmessage, MessageSend.class);
		String message = ms.getMessage();

		System.out.println(client + " " + message);
		MessageSignal messig = new MessageSignal(message, client.clientformatter());
		if (client.getClientType() != ClientType.SPECTATOR) {
			game.sendAll(messig);
		}
		else {
			for (Client spectator : game.getClients()) {
				if(spectator.getClientType() == ClientType.SPECTATOR) {
					spectator.sendMessage(messig);
				}
			}
		}
	}

	//##############################################################################################################################
												//ParticipantMessages
	//##############################################################################################################################

	/**
	 * Player wants to swap tiles from his hand
	 * @param msg
	 * @param client
	 * @throws InterruptedException
	 */
	public void SwapRequestSignal(Message msg, Client client) throws InterruptedException
	{
		String tSRMsg = parser.serialize(msg);
		TileSwapRequest tsr = new GsonBuilder().create().fromJson(tSRMsg, TileSwapRequest.class);
		ArrayList<Tile> oldTiles = (ArrayList<Tile>) tsr.getTiles();

		if(this.game.getBagSize()>= 1 && oldTiles.size() > 0 && oldTiles.size() <= this.game.getConfig().getMaxHandTiles())
		{
			int toDraw = oldTiles.size();
			Collection<Tile> newTiles = new ArrayList<Tile>();
			Client SwapClient = null;
			ArrayList<Tile> SwapClientHand = new ArrayList<Tile>();
			
			// Finding out, what the Players hand is, and the Key
			for(Client c : this.game.getPlayerHands().keySet()) {
				if(c.getClientId() == client.getClientId()) {
					SwapClient = c;
					break;
				}
			}
			// Taking tiles from Playerhand
			for(Tile tile : this.game.getPlayerHands().get(SwapClient)) {
				SwapClientHand.add(tile);
			}
			logger.info("SWAPCLIENTHAND SIZE = " + this.game.getPlayerHands().get(SwapClient).size());
			
			Iterator<Tile> tileit = SwapClientHand.iterator();
			while(tileit.hasNext()) {
				Tile tile = tileit.next();
				int tileid = tile.getUniqueId();
				for(Tile oldtile : oldTiles) {
					if(tileid == oldtile.getUniqueId()) {
						tileit.remove();
						break;
					}
				}
			}
						
			// Putting swap Tiles back into Bag
			this.game.getBag().addTile(oldTiles);
			
			// Drawing New Tiles
			try
			{
				for(Tile tile : this.game.getBagTiles(toDraw)) {
					newTiles.add(tile);
				}
			}
			catch(NullPointerException e)
			{
				TileSwapValid invalid = new TileSwapValid(false);
				client.sendMessage(invalid);
			}
			// Putting new drawn Tiles into Players hand
			SwapClientHand.addAll(newTiles);
			this.game.getPlayerHands().put(SwapClient, SwapClientHand);
			
			// Sending Response and Validity to Client
			TileSwapValid valid = new TileSwapValid(true);
			client.sendMessage(valid);
			
			TileSwapResponse response = new TileSwapResponse(newTiles);
			client.sendMessage(response);
			
			// next player
			game.nextPlayerCheck();
			
		}
		else
		{
			// TileSwap was not Valid to do
			System.out.println("IS THE BAG SIZE GREATER THAN 1? : " + (this.game.getBagSize()>= 1));
			System.out.println("IS THE OLD TILES SIZE GREATER THAN 0? : " + (oldTiles.size() > 0));
			System.out.println("IS THE OLD TILES SIZE LESSER THAN THE MAX HAND TILES? : " + (oldTiles.size() <= this.game.getConfig().getMaxHandTiles()));
			TileSwapValid invalid = new TileSwapValid(false);
			client.sendMessage(invalid);
			
			// Dishing out punishment
			Configuration config = this.game.getConfig();
			WrongMove punishment = config.getWrongMove();

			if (punishment == WrongMove.NOTHING) {
				return;
			}

			if (punishment == WrongMove.POINT_LOSS) {
				int penalty = config.getWrongMovePenalty();
				game.getScoreBoard().put(client, game.getScoreBoard().get(client)-penalty);
			}
			else {
				game.disconnectClient(client);
			}
			game.nextPlayerCheck();
		}
	}

	/**
	 * Player wants to put Tiles on the Field
	 * @param playtiles
	 * @param client
	 * @return 
	 * @throws InterruptedException
	 */
	public CheckProcessObject PlayTilesSignal(ArrayList<TileOnPositionEX> playtiles, Client client) throws InterruptedException
	{
			ArrayList<TileOnPositionEX> copyplaytile = new ArrayList<>();
			for(TileOnPositionEX playtile : playtiles) {
				copyplaytile.add(playtile);
			}
			// Testing, if Move was valid
			CheckProcessObject testresult = this.game.playTile(copyplaytile);
			if(testresult.isValidity()) {
			// Move was valid
			
			// Determining Players Hand
			ArrayList<Tile> playerhandtoremove = new ArrayList<>();
			Client playingclient = null;
			
			
			logger.info("PLAYTILES -> Trying to Find the PLAYER AND HIS HAND");
			for(Client hashclient : this.game.getPlayerHands().keySet()) {
				if(client.getClientId() == hashclient.getClientId()) {
					playerhandtoremove = this.game.getPlayerHands().get(hashclient);
				
					playingclient = hashclient;
					break;
					// ------------------------ First Loop Finished -> Determined the Hand in the Hashmap and the Key
				}
			}
			
			logger.info("FOUND PLAYER AND HIS HAND : [" + playingclient + " || " + playerhandtoremove + " ]");
			Iterator<Tile> it = playerhandtoremove.iterator();
			while(it.hasNext()) {
				Tile tile = it.next();
				int id = tile.getUniqueId();
				boolean removal = false;
				for(TileOnPositionEX playedTile : playtiles) {
					if(playedTile.getUniqueId() == id) {
						logger.info("FOUND A PLAYTILE THAT HAS TO BE REMOVED! : " + playedTile + " & " + tile);
						removal = true;
						break;
					}
				}
				if(removal) {
					it.remove();
				}
			}
			// -------------------------- Second Loop Finished -> Determined, what Tiles had to be Removed from playerhand
			// Putting hand with removed tiles back into the Hashmap
			
			// UPDATING SCORE OF THE PLAYER
			Iterator<Client> it2 = this.game.getScoreBoard().keySet().iterator();
			while(it2.hasNext()) {
				Client ScoreUpdateClient = it2.next();
				int id = ScoreUpdateClient.getClientId();
				if(id == client.getClientId()) {
					int score = this.game.getScoreBoard().get(ScoreUpdateClient);
					this.game.getScoreBoard().put(ScoreUpdateClient, score + testresult.getScore());
					logger.info("[SCOREBOARD] UPDATED SCORE OF PLAYER : " + ScoreUpdateClient.getClientName() + " SCOREBOARD NOW: " + this.game.getScoreBoard());
				}
			}
			this.game.getPlayerHands().put(playingclient, playerhandtoremove);
			
			Collection<TileOnPosition> send_to_clients = new ArrayList<>();
			for(TileOnPositionEX playtile_to_convert : playtiles) {
				send_to_clients.add(playtile_to_convert.toOwnTileOnPosition());
			}
			
			client.sendMessage(new MoveValid(true, "Move was valid"));
			game.sendAll(new Update(send_to_clients, game.getBagSize()));
			return testresult;
			}
			else {
				Iterator<Client> it2 = this.game.getScoreBoard().keySet().iterator();
				while(it2.hasNext()) {
					Client ScoreUpdateClient = it2.next();
					int id = ScoreUpdateClient.getClientId();
					if(id == client.getClientId()) {
						int score = this.game.getScoreBoard().get(ScoreUpdateClient);
						this.game.getScoreBoard().put(ScoreUpdateClient, score + testresult.getScore());
						logger.info("[SCOREBOARD] UPDATED SCORE OF PLAYER : " + ScoreUpdateClient.getClientName() + " SCOREBOARD NOW: " + this.game.getScoreBoard());
					}
				}
				return testresult;
			}
	}

	/**
	 * Spectator requests to see the bag
	 * @param msg
	 * @param client
	 */
	public void BagRequest(Message msg, Client client) {
		BagResponse br = new BagResponse(this.game.getAllBagTiles());
		client.sendMessage(br);

	}

	/**
	 * Client requests the Games data
	 * @param msg
	 * @param client
	 */
	public void GameDataRequest(Message msg, Client client) {
		Collection<TileOnPosition> converted_board = new ArrayList<>();
		for(TileOnPositionEX boardtile : game.getPlayboard().getTilesOnBoard()) {
			converted_board.add(boardtile.toOwnTileOnPosition());
		}
		
		if (client.isPlayer() == true) {
			client.sendMessage(new GameDataResponse(converted_board, game.getCurrentplayer(), game.getPlayerHands().get(client), game.getGameState()));
		}
		else {
			client.sendMessage(new GameDataResponse(converted_board, game.getCurrentplayer(), null, game.getGameState() ));
		}
	}

	public void playerHandsResponse(Message msg, Client client) {
		client.sendMessage(new PlayerHandsResponse(game.getPlayerHands()));
	}

	/**
	 * Client requests the Elapsed time of the game
	 * @param client
	 */
	public void TotalTimeRequest(Client client) {
		client.sendMessage(new TotalTimeResponse(game.Timer.getTotaltime()));
	}

}
