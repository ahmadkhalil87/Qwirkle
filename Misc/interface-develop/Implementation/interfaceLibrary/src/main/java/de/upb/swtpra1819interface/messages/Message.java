package de.upb.swtpra1819interface.messages;

public abstract class Message {

	private final int uniqueID;

	public Message(int uniqueID) {
		this.uniqueID = uniqueID;
	}

	public int getUniqueID() {
		return this.uniqueID;
	}

}
