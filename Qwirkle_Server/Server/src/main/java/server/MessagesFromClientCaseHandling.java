package server;

import com.google.gson.GsonBuilder;

import client.Client;
import de.upb.swtpra1819interface.messages.*;
import de.upb.swtpra1819interface.models.ClientType;
import de.upb.swtpra1819interface.models.GameState;
import de.upb.swtpra1819interface.parser.Parser;

import game_elements.Game;
import adjusted_messages.GameListResponse;;

/**
 * This Class has the handling for the Switch-Cases in {@linkplain ServerMessageHandling}.
 *
 * @author Lukas
 *
 */
public class MessagesFromClientCaseHandling {

	Parser parser = new Parser();
	Server server;

	public MessagesFromClientCaseHandling(Server server) {
		this.server = server;
	}

	/**
	 * Method to handle GameListRequests
	 * @param clientToRespondto (Output)
	 */
	public void GameListRequest(Client client){
		//Checking if Client is already ingame
		if(client.getIsIngame() == true) {
			client.sendMessage(new NotAllowed("You are already participating in a game", 920));
			return;
		}

		//TODO IMPLEMENT TOURNAMENT

		client.sendMessage(new GameListResponse(server.getActiveGames()));

	}

	/**
	 * Method that handles Disconnects
	 * @param msg
	 * @throws InterruptedException
	 */
	/* @Deprecated
	public void DisconnectSignal(Message msg) throws InterruptedException {
		String dsimessage = parser.serialize(msg);
		DisconnectSignal dc = new GsonBuilder().create().fromJson(dsimessage, DisconnectSignal.class);
		String reason = dc.getReason();
		Client client = dc.getClient();
		System.out.println("Reason :" + reason);
		System.out.println("Client: " + client);

		Message dcr = new DisconnectSignal(client.getClientType() + " "+ client.getClientName() +  " has disconnected because : " + reason, client);
		server.sendAll(dcr);
	}
	*/

	/**
	 * Method to handle GameJoinRequests
	 * The Player-Check has been added for other Servers.
	 * @param clientToRespondto (Output)
	 * @param msg
	 */
	public void GameJoinRequest(Message msg, Client client){

		//Checking ClientType for Player
		if(client.getClientType() != ClientType.PLAYER) {
			client.sendMessage(new AccessDenied("You are not a Player! Please Reconnect as Player.", 900));
			return;
		}

		//Checking if Client is already ingame
//		if(client.isIngame() == true) {
//			client.sendMessage(new NotAllowed("You are already participating in a game"));
//		}

		String gjrmessage = parser.serialize(msg);
		GameJoinRequest gjr = new GsonBuilder().create().fromJson(gjrmessage, GameJoinRequest.class);

		//Checking for GameID
		if(server.hasGameID(gjr.getGameId())) {
			Game game = server.getGame(gjr.getGameId());
			if (game.getGameState() == GameState.NOT_STARTED){
				game.ConnectClient(client);
				return;
			}
			else{
				client.sendMessage(new AccessDenied("Game has already started", 900));
				return;
			}
		}else {
			client.sendMessage(new NotAllowed("Game does not exist.", 920));
			return;
		}
	}



	/**
	 * Method to handle SpectatorJoinRequests
	 * @param clientToRespondto (Output)
	 * @param msg
	 */
	public void SpectatorJoinRequest(Message msg, Client client){

		//Checking if Client is already ingame
		if(client.getIsIngame() == true) {
		client.sendMessage(new NotAllowed("You are already participating in a game", 920));
		return;
		}

		String sjrmessage = parser.serialize(msg);
		SpectatorJoinRequest sjr = new GsonBuilder().create().fromJson(sjrmessage, SpectatorJoinRequest.class);
		System.out.println(sjr.getGameId());
		//Checking for GameID
		if(server.hasGameID(sjr.getGameId())) {
			server.getGame(sjr.getGameId()).ConnectClient(client);
			return;

		}else {
			client.sendMessage(new NotAllowed("Game does not exist.", 920));
			return;
		}
	}

	/**
	 * Method to handle incoming Chat Messages
	 * Only implemented here for compatability with other SWTPRA clients.
	 * @param msg
	 */
	public void MessageSend(Message msg, Client client) {
			client.sendMessage(new NotAllowed("You are not participating in a Game.", 920));
			return;
	}

}
