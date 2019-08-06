package adjusted_messages;

import java.util.Collection;
import de.upb.swtpra1819interface.messages.Message;
import gameboard.TileOnPosition;

public class PlayTiles extends Message {

	private Collection<TileOnPosition> tiles;
	
	public static final int uniqueID = 414;

	public PlayTiles(Collection<TileOnPosition> tiles) {
		super(uniqueID);
		this.tiles = tiles;
	}

	public Collection<TileOnPosition> getTiles() {
		return tiles;
	}

}
