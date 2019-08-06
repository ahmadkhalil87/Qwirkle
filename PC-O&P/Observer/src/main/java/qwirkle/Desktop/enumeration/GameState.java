package qwirkle.Desktop.enumeration;
/**
 * this enumeration is for the game state
 * 
 * @author houman
 *
 */

public enum GameState {
	NOT_STARTED (0),
	IN_PROGRESS (1),
	PAUSED (2),
	ENDED(3);

	private final int state;
	
	GameState(int state){
		this.state = state;
	}
	
	public int getStateCode(){
		return this.state;
	}
}
