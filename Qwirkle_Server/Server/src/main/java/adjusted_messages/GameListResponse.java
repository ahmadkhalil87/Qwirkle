package adjusted_messages;

import java.util.Collection;
import java.util.Objects;

import de.upb.swtpra1819interface.messages.Message;
import game_elements.Game;

public class GameListResponse extends Message {
	
	private Collection<Game> games;
	
	public static final int uniqueID = 301;
	
	public GameListResponse(Collection<Game> games) {
		super(uniqueID);
		this.games = games;
	}

	public Collection<Game> getGames() {
		return games;
	}
	   
	public boolean equals(Object o)	{
		if (this == o) {
			return true;
		}
	    if (!(o instanceof GameListResponse)) {
		      return false;
	    }
	    GameListResponse that = (GameListResponse)o;
	    return Objects.equals(getGames(), that.getGames());
	}

}