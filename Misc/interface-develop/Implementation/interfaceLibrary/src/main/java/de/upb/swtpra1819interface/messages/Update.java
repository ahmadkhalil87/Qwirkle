package de.upb.swtpra1819interface.messages;

import java.util.List;

public class Update extends Message {
	
	private List<TileOnPosition> updates;
	
	public static final int uniqueID = 416;
	
	public Update(List<TileOnPosition> updates) {
		super(uniqueID);
		this.updates = updates;
	}
	
	public List<TileOnPosition> getUpdates() {
		return updates;
	}

	public void setUpdates(List<TileOnPosition> updates) {
		this.updates = updates;
	}
}
