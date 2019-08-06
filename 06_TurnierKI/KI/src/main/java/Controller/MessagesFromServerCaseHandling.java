package Controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.GsonBuilder;

import de.upb.swtpra1819interface.messages.*;
import de.upb.swtpra1819interface.models.Client;
import de.upb.swtpra1819interface.models.Configuration;
import de.upb.swtpra1819interface.models.Tile;
import de.upb.swtpra1819interface.parser.*;

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
	
	public void GameListResponse(Message msg) {
		String glmessage = parser.serialize(msg);
		GameListResponse glr = new GsonBuilder().create().fromJson(glmessage, GameListResponse.class);
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
		String message = ms.getMessage();
		return ms;
	}
	
	
	/*
	public DisconnectSignal DisconnectSignal(Message msg) {
		String messageString = parser.serialize(msg);
		DisconnectSignal dcmessage = new GsonBuilder().create().fromJson(messageString, DisconnectSignal.class);
		return dcmessage;
	}*/
	
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
	
	public  Configuration StartGame(Message msg){
		String messageString = parser.serialize(msg);
		StartGame sg = new GsonBuilder().create().fromJson(messageString,  StartGame.class);
		return sg.getConfig();
	}
	
	public void AbortGame(Message msg) {
		System.out.println("Game has been aborted");
		
	}
	
	public void EndGame(Message msg) {
		String messageString = parser.serialize(msg);
		StartGame sg = new GsonBuilder().create().fromJson(messageString,  StartGame.class);
	}
	
	public void PauseGame(Message msg) {
		String messageString = parser.serialize(msg);
		PauseGame pg = new GsonBuilder().create().fromJson(messageString,  PauseGame.class);
	}
	
	public void ResumeGame(Message msg) {
		String messageString = parser.serialize(msg);
		ResumeGame rg = new GsonBuilder().create().fromJson(messageString,  ResumeGame.class);
	}
	
	public void LeavingPlayer(Message msg) {
		String messageString = parser.serialize(msg);
		LeavingPlayer rg = new GsonBuilder().create().fromJson(messageString,  LeavingPlayer.class);
		de.upb.swtpra1819interface.models.Client leavingClient = rg.getClient();
		
	}
	
	public void Winner(Message msg) {
		String messageString = parser.serialize(msg);
		Winner w = new GsonBuilder().create().fromJson(messageString,  Winner.class);
		de.upb.swtpra1819interface.models.Client winner = w.getClient();
		int winnerscore = w.getScore();
		Map<de.upb.swtpra1819interface.models.Client, Integer> scoreboard = w.getLeaderboard();
		
		// TODO IMPLEMENT
	}
	
	public ArrayList<Tile> StartTiles(Message msg) {
		String messageString = parser.serialize(msg);
		StartTiles st = new GsonBuilder().create().fromJson(messageString,  StartTiles.class);
		ArrayList<Tile> personalhand = (ArrayList<Tile>) st.getTiles();
		return personalhand;

	}



	public void GameDataResponse(Message msg) {
		String messageString = parser.serialize(msg);
		GameDataResponse gdr = new GsonBuilder().create().fromJson(messageString,  GameDataResponse.class);
		Collection<de.upb.swtpra1819interface.models.TileOnPosition> Board = gdr.getBoard();
		de.upb.swtpra1819interface.models.Client currentplayer = gdr.getCurrentClient();
		// IF CLIENT IS PLAYER ELSE NULL
		ArrayList<Tile> personalhand = (ArrayList<Tile>) gdr.getOwnTiles();
		
		// TODO IMPLEMENT
	}



	public CurrentPlayer CurrentPlayer(Message msg) {
		String messageString = parser.serialize(msg);
		CurrentPlayer cp = new GsonBuilder().create().fromJson(messageString,  CurrentPlayer.class);
		return cp;
		
	}



	public Collection<Tile> SendTiles(Message msg) {
		String messageString = parser.serialize(msg);
		SendTiles st = new GsonBuilder().create().fromJson(messageString,  SendTiles.class);
		return st.getTiles();
		
	}

	public void TotalTimeResponse(Message msg) {
		// TODO Auto-generated method stub
		
	}



	public void TurnTimeLeftResponse(Message msg) {
		// TODO Auto-generated method stub
		
	}



	public void BagResponse(Message msg) {
		String bagResponseMsg = parser.serialize(msg);
		BagResponse bagResponse = new GsonBuilder().create().fromJson(bagResponseMsg, BagResponse.class);
		System.out.println("Bag Set!");
		
	}



	public void PlayerHandsResponse(Message msg) {
		String messageString = parser.serialize(msg);
		PlayerHandsResponse phr = new GsonBuilder().create().fromJson(messageString, PlayerHandsResponse.class);
	}



	public Update Update(Message msg) {
		String messageString = parser.serialize(msg);
		Update u = new GsonBuilder().create().fromJson(messageString,  Update.class);
		return u;
		
	}
}
