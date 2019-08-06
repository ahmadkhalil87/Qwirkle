package client;

import java.io.IOException;
import java.util.ArrayList;

import de.upb.swtpra1819interface.messages.*;

//import java.util.ArrayList;

import de.upb.swtpra1819interface.messages.TileSwapResponse;
import de.upb.swtpra1819interface.messages.TileSwapValid;
import de.upb.swtpra1819interface.parser.Parser;
import game_elements.Game;

/**
 * This Class is used to Manage the Clients Messages
 * It runs on one Thread.
 * 
 * This class will constantly read the Messagequeue and
 * send the Messages to the right Methods or Handlers
 * 
 * @author Lukas
 *
 */
class ClientMessageHandler implements Runnable {
	Parser parser = new Parser();
	MessagesFromServerCaseHandling caseHandler = new MessagesFromServerCaseHandling();
	Client client;
	
	
	public ClientMessageHandler(Client client) {
		this.client = client;
	}
	
	@SuppressWarnings("unchecked")
	public void run() {
		while (client.isOnline()) {
			try {
				System.out.println(client.clientName + " MessageHandler: Waiting for new Message");
				Message msg = client.messages.take();
				if (msg == null)
					continue;
				
				int messageID = msg.getUniqueId();
				System.out.println(client.clientName + " MessageHandler: new Message -> " + msg);
				switch(messageID) {
				
				//@Deprecated DisconnectSignal
				/*
				case(200):
					DisconnectSignal ds = this.caseHandler.DisconnectSignal(msg);
					System.out.println(ds.getReason());
					break;
				*/
					
				//GameListRespond
				case(301):
					GameListResponse glr = this.caseHandler.GameListResponse(msg);
					ArrayList<Game> games = (ArrayList) glr.getGames();
					break;
					
					
				//GameJoinAccepted
				case(303):
					GameJoinAccepted gja = this.caseHandler.GameJoinAccepted(msg);
					System.out.println("Your Joinrequest as Player has been granted, joining Game #" + gja.getGame());
					break;
				
				//SpectatorJoinAccepted
				case(305):
					SpectatorJoinAccepted sja = this.caseHandler.SpectatorJoinAccepted(msg);
					System.out.println("Your Joinrequest as Spectator has been granted, joining Game #" + sja.getGameId()); // getGameId() returns the Game object!!!!
					break;
					
				//MessageSignal
				case(307):
					MessageSignal ms = this.caseHandler.MessageSignal(msg);
					System.out.println(ms.getMessage());
					break;
					
				//SwapTilesValid
				case(412):
					TileSwapValid tsv = this.caseHandler.SwapValidationSignal(msg);
					if(tsv.isValidation())
					{
						System.out.println("Your TileSwapRequest has been granted.");
					}
					else
					{
						System.out.println("Your TileSwapRequest has been denied.");
					}
					break;
					
				//SwapTilesResponse
				case(413):
					TileSwapResponse tsr = this.caseHandler.SwapResponseSignal(msg);
					System.out.println("Your new tiles are:");
					tsr.getTiles().forEach( (a) -> System.out.println(a));
					break;
				// AccessDenied
				case(900):	
					AccessDenied ad = (AccessDenied) msg;
					System.out.println(ad.getMessage());
					break;
				// ParsingError
				case(910):
					ParsingError pe = (ParsingError) msg;
					System.out.println(pe.getMessage());
					break;
				// Notallowed
				case(920):
					NotAllowed na = (NotAllowed) msg;
					System.out.println(na.getMessage());
					break;
				
				}
			} 
		
			catch (InterruptedException e) {
				e.printStackTrace();
			}
			catch (RuntimeException rte) {
				rte.printStackTrace();
				try {
					client.shutDown();
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
