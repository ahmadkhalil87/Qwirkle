package client;

import com.google.gson.GsonBuilder;

import de.upb.swtpra1819interface.messages.*;
import de.upb.swtpra1819interface.parser.Parser;

/**
 * This Class holds all the Responses for the Messages sent to the Client by the Server.
 * The Messages that are being read are getting build into their respective classes and returned
 * for further use.
 * @author Lukas
 *
 */
public class MessagesFromServerCaseHandling {

	Parser parser = new Parser();
	
//##############################################################################################################################
												//ServerMessages
//##############################################################################################################################

	public GameListResponse GameListResponse(Message msg) {
		String glmessage = parser.serialize(msg);
		GameListResponse glr = new GsonBuilder().create().fromJson(glmessage, GameListResponse.class);
		return glr;
	}
	
	public GameJoinAccepted GameJoinAccepted(Message msg) {
		String gjmessage = parser.serialize(msg);
		GameJoinAccepted gj = new GsonBuilder().create().fromJson(gjmessage, GameJoinAccepted.class);
		return gj;
	}
	
	public SpectatorJoinAccepted SpectatorJoinAccepted(Message msg) {
		String sjmessage = parser.serialize(msg);
		SpectatorJoinAccepted ga = new GsonBuilder().create().fromJson(sjmessage, SpectatorJoinAccepted.class);
		return ga;
	}
	
	public MessageSignal MessageSignal(Message msg) {
		String msmessage = parser.serialize(msg);
		MessageSignal ms = new GsonBuilder().create().fromJson(msmessage, MessageSignal.class);
		return ms;
	}
	
	@Deprecated
	/*
	public DisconnectSignal DisconnectSignal(Message msg) {
		String messageString = parser.serialize(msg);
		DisconnectSignal dcmessage = new GsonBuilder().create().fromJson(messageString, DisconnectSignal.class);
		return dcmessage;
	}
	*/
	
	public TileSwapValid SwapValidationSignal(Message msg)
	{
		String messageString = parser.serialize(msg);
		TileSwapValid tsv = new GsonBuilder().create().fromJson(messageString,  TileSwapValid.class);
		return tsv;
	}
	
	public TileSwapResponse SwapResponseSignal(Message msg)
	{
		String messageString = parser.serialize(msg);
		TileSwapResponse tsr = new GsonBuilder().create().fromJson(messageString,  TileSwapResponse.class);
		return tsr;
	}
	
//##############################################################################################################################
												//GameMessages
//##############################################################################################################################
	
	public void StartGame(Message msg){
		
	}
	
	public void EndGame(Message msg) {
		
	}
	
	public void PauseGame(Message msg) {
		
	}
	
	public void ResumeGame(Message msg) {
		
	}
	
	public void LeavingPlayer(Message msg) {
		
	}
	
	public void Winner(Message msg) {
		
	}
	
	public void StartTiles(Message msg) {
		
	}
}
