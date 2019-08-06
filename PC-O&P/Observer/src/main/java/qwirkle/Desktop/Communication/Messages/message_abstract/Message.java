package qwirkle.Desktop.Communication.Messages.message_abstract;

import com.google.gson.Gson;

public abstract class Message {

	private final int uniqueId;

	public Message(int uniqueID) {
		this.uniqueId = uniqueID;
	}

	public int getUniqueID() {
		return this.uniqueId;
	}
	public String getMessage() {
		Gson gson = new Gson();
		return gson.toJson(this); //Serialises the Message
	}
}
