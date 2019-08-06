package qwirkle.Desktop.Communication.MessageHandlers;

import com.google.gson.*;

import qwirkle.Desktop.Communication.Messages.Exceptions.*;
import qwirkle.Desktop.Communication.Messages.messages_to_server.*;
import qwirkle.Desktop.Communication.Messages.messages_from_server.*;
import qwirkle.Desktop.Communication.Messages.gameMessages.*;
import qwirkle.Desktop.Communication.Messages.message_abstract.*;


import java.io.*;


public class Parser {

	public String serialize(Message msg) {
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String jsonString = gson.toJson(msg);
		return jsonString;
	}

	public Message deserialize (String jsonString) throws ParsingException{
		// initialise Gson
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		// parse String to jsonObject
		JsonParser jp = new JsonParser();
		JsonElement jsonTree = jp.parse(jsonString);
		jsonTree.isJsonObject();
		JsonObject jsonObject = jsonTree.getAsJsonObject();
		int id = 0;
		try {
			id = jsonObject.get("uniqueId").getAsInt();
		} catch (NullPointerException e) {
			throw new ParsingException(e);
		}
		Message message = null;
		switch (id) {
		// Client requests
		case 100:
			message = gson.fromJson(jsonTree, ConnectRequest.class);
			break;
		
		case 200:
			message = gson.fromJson(jsonTree, DisconnectSignal.class);
			break;
		
		case 300:
			message = gson.fromJson(jsonTree, GameListRequest.class);
			break;
		case 302:
			message = gson.fromJson(jsonTree, GameJoinRequest.class);
			break;
		case 304:
			message = gson.fromJson(jsonTree, SpectatorJoinRequest.class);
			break;
		case 306:
			message = gson.fromJson(jsonTree, MessageSend.class);
			break;
		case 405:
			message = gson.fromJson(jsonTree, LeavingRequest.class);
			break;
		case 411:
			message = gson.fromJson(jsonTree, TileSwapRequest.class);
			break;
		case 414:
			message = gson.fromJson(jsonTree, PlayTiles.class);
			break;
		case 417:
			message = gson.fromJson(jsonTree, ScoreRequest.class);
			break;
		case 419:
			message = gson.fromJson(jsonTree, TurnTimeLeftRequest.class);
			break;
		case 421:
			message = gson.fromJson(jsonTree, TotalTimeRequest.class);
			break;
		case 423:
			message = gson.fromJson(jsonTree, BagRequest.class);
			break;
		case 425:
			message = gson.fromJson(jsonTree, PlayerHandsRequest.class);
			break;
		case 498:
			message = gson.fromJson(jsonTree, GameDataRequest.class);
			break;
		// Server responses
		case 101:
			message = gson.fromJson(jsonTree, ConnectAccepted.class);
			break;
		case 301:
			message = gson.fromJson(jsonTree, GameListResponse.class);
			break;
		case 303:
			message = gson.fromJson(jsonTree, GameJoinAccepted.class);
			break;
		case 305:
			message = gson.fromJson(jsonTree, SpectatorJoinAccepted.class);
			break;
		case 307:
			message = gson.fromJson(jsonTree, MessageSignal.class);
			break;
		case 400:
			message = gson.fromJson(jsonTree, StartGame.class);
			break;
		case 401:
			message = gson.fromJson(jsonTree, EndGame.class);
			break;
		case 402:
			message = gson.fromJson(jsonTree, AbortGame.class);
			break;
		case 403:
			message = gson.fromJson(jsonTree, PauseGame.class);
			break;
		case 404:
			message = gson.fromJson(jsonTree, ResumeGame.class);
			break;
		case 406:
			message = gson.fromJson(jsonTree, LeavingPlayer.class);
			break;
		case 407:
			message = gson.fromJson(jsonTree, Winner.class);
			break;
		case 408:
			message = gson.fromJson(jsonTree, StartTiles.class);
			break;
		case 409:
			message = gson.fromJson(jsonTree, CurrentPlayer.class);
			break;
		case 410:
			message = gson.fromJson(jsonTree, SendTiles.class);
			break;
		case 412:
			message = gson.fromJson(jsonTree, TileSwapValid.class);
			break;
		case 413:
			message = gson.fromJson(jsonTree, TileSwapResponse.class);
			break;
		case 415:
			message = gson.fromJson(jsonTree, MoveValid.class);
			break;
		case 416:
			message = gson.fromJson(jsonTree, Update.class);
			break;
		case 418:
			message = gson.fromJson(jsonTree, ScoreResponse.class);
			break;
		case 420:
			message = gson.fromJson(jsonTree, TurnTimeLeftResponse.class);
			break;
		case 422:
			message = gson.fromJson(jsonTree, TotalTimeResponse.class);
			break;
		case 424:
			message = gson.fromJson(jsonTree, BagResponse.class);
			break;
		case 426:
			message = gson.fromJson(jsonTree, PlayerHandsResponse.class);
			break;
		case 499:
			message = gson.fromJson(jsonTree, GameDataResponse.class);
			break;
		case 900:
			message = gson.fromJson(jsonTree, AccessDenied.class);
			break;
		case 910:
			message = gson.fromJson(jsonTree, ParsingError.class);
			break;
		case 920:
			message = gson.fromJson(jsonTree, NotAllowed.class);
			break;
		default:
			break;
		}
		return message;
	}

	public Configuration loadConfig(String filePath) {
		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();
			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
			String everything = sb.toString();
			// initialise Gson
			GsonBuilder builder = new GsonBuilder();
			builder.setPrettyPrinting();
			Gson gson = builder.create();
			return gson.fromJson(everything, Configuration.class);
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + filePath + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + filePath + "'");
		}
		return null;
	}

	public void saveConfig(Configuration config, String name) {
		//initialise Gson
		GsonBuilder builder = new GsonBuilder();
		builder.setPrettyPrinting();
		Gson gson = builder.create();
		String jsonString = gson.toJson(config);
        File file = new File(name+".json");
        try {
        	file.createNewFile();
        }
        catch (IOException ex) {
			
		}
		try (PrintWriter out = new PrintWriter(name+".json")) {
		    out.println(jsonString);
		}
		catch (FileNotFoundException ex) {
			
		} 
	}
	public String parseTheMessage(String msg, String argument) throws ParsingException {
		JsonParser jp = new JsonParser();
		JsonElement jsonTree = jp.parse(msg);
		jsonTree.isJsonObject();
		JsonObject jsonObject = jsonTree.getAsJsonObject();
		String message = null;
		try {
			message = jsonObject.get(argument).getAsString();
		} catch (NullPointerException e) {
			throw new ParsingException(e);
		}
		return message;
	}
}
