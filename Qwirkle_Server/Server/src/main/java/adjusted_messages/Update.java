package adjusted_messages;

import java.util.Collection;
import de.upb.swtpra1819interface.messages.Message;
import gameboard.TileOnPosition;



public class Update extends Message {
	
	private Collection<TileOnPosition> updates;
	private int numberTilesInBag;
	public static final int uniqueID = 416;
	
	public Update(Collection<TileOnPosition> updates, int numberTilesInBag) {
		super(uniqueID);
		this.updates = updates;
		this.numberTilesInBag = numberTilesInBag;
	}

	public Collection<TileOnPosition> getUpdates() {
		return updates;
	}

	public int getNumberTilesInBag() {
		return numberTilesInBag;
	}
}
