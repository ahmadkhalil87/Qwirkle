package qwirkle.Desktop.Communication.Messages.gameMessages;

import java.util.ArrayList;
import java.util.List;
import qwirkle.Desktop.Communication.Messages.message_abstract.Message;
import qwirkle.Desktop.entity.TileOnPosition;

public class Update extends Message {
	
	private ArrayList<TileOnPosition> updates;
	private int numberTilesInBag;
	
	public static final int uniqueID = 416;
	
	public Update(ArrayList<TileOnPosition> updates, int numberTilesInBag) {
		super(uniqueID);
		this.updates = updates;
		this.numberTilesInBag = numberTilesInBag;
	}
	
	public ArrayList<TileOnPosition> getUpdates() {
		return updates;
	}

	public void setUpdates(ArrayList<TileOnPosition> updates) {
		this.updates = updates;
	}

	public int getNumberTilesInBag() {
		return numberTilesInBag;
	}

	public void setNumberTilesInBag(int numberTilesInBag) {
		this.numberTilesInBag = numberTilesInBag;
	}
	
}
