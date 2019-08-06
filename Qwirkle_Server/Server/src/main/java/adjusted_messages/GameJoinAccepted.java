package adjusted_messages;

import java.util.Objects;
import de.upb.swtpra1819interface.messages.Message;
import game_elements.Game;

public class GameJoinAccepted extends Message {
	
	public static final int uniqueID = 303;
	private Game game;
	
	public GameJoinAccepted(Game game) {	
		super(uniqueID);
		this.game = game;
	}

	public Game getGame() {
		return game;
	}

	public boolean equals(Object o) {
		
		if (this == o) {
			return true;
		}
		if (!(o instanceof GameJoinAccepted)) {
			return false;
	    }
		GameJoinAccepted that = (GameJoinAccepted)o;
		return Objects.equals(this.game, that.game);
	}
}

