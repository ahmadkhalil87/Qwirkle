package qwirkle.Desktop.Communication.MessageHandlers;

import java.io.IOException;
import java.util.ArrayList;

import qwirkle.Desktop.Communication.Client;
import qwirkle.Desktop.Communication.Messages.gameMessages.TileSwapResponse;
import qwirkle.Desktop.Communication.Messages.gameMessages.TileSwapValid;
import qwirkle.Desktop.Communication.Messages.message_abstract.Message;
import qwirkle.Desktop.Communication.Messages.messages_from_server.*;
import qwirkle.Desktop.Game.Game;
import qwirkle.Desktop.Main.Mainstarter;

//import java.util.ArrayList;


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
public class ClientMessageHandler implements Runnable {
	Parser parser = new Parser();
	MessagesFromServerCaseHandling caseHandler = new MessagesFromServerCaseHandling();
	Client client;
	
	
	public ClientMessageHandler(Client client) {
		this.client = client;
	}
	
	public void run() {
		while (this.client.isOnline())
			try {
				System.out.println(client.getClientName() + " MessageHandler: Waiting for new Message");
				Message msg = client.getMessages().take();
				if (msg == null)
					continue;
				
				int messageID = msg.getUniqueID();

				switch(messageID) {
				
				//DisconnectSignal
				case(200):
					this.caseHandler.DisconnectSignal(msg);
					break;
					
				//GameListRespond : Updates the Lobby View
				case(301):
					this.caseHandler.GameListResponse(msg);
					break;
					 	
				//GameJoinAccepted
				case(303):
					this.caseHandler.GameJoinAccepted(msg);
					// TODO INVOKE AN INFORMATION WINDOW
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
					this.caseHandler.StartGame(msg);
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
//				case(408):
//					this.caseHandler.StartTiles(msg);
//					break;
				
				// CurrentPlayer	
				case(409):
					this.caseHandler.CurrentPlayer(msg);
					break;
				
				// SendTiles
				case(410):
					this.caseHandler.SendTiles(msg);
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
				
				// MoveValid
				case(415):
					this.caseHandler.MoveValid(msg);
					break;
					
				// Update
				case(416):
					this.caseHandler.Update(msg);
					break;
				
				// ScoreResponse
				case(418):
					this.caseHandler.scoreResponse(msg);
					
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
					System.out.println(msg.getMessage());	
				// ParsingError
				case(910):
					System.out.println(msg.getMessage());
				// Notallowed
				case(920):
					System.out.println(msg.getMessage());
				
				
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
