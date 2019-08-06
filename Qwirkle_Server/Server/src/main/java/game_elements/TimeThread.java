package game_elements;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import de.upb.swtpra1819interface.models.GameState;


/**
 * Class that hosts all the Timer Methods for a Game
 * @author Lukas
 *
 */
public class TimeThread{
	
	private Game game;
	private long totaltime;
	private long current_turn_time;
	private long playertimer;
	private String input = "NDONE";

	public TimeThread(Game game) {
		this.game = game;
		this.playertimer = game.getConfig().getTurnTime();
	}
	
	/**
	 * Times the Games complete elapsed time
	 */
	public void startTimer() {
		Thread GameTimer = new Thread("Game: " + game.getGameId() + " timer Thread") {
			public void run(){
				while (game.getGameState() != GameState.ENDED) {
					setTotaltime(System.currentTimeMillis() / 1000);
				}
			}
		};
		GameTimer.setDaemon(true);
		GameTimer.start();
	}
	/**
	 * Times the TurnTimer
	 */
	public void startTurnTimer() {
		long playtime = playertimer;
		Thread turntimer = new Thread("TimerThread"){
			public void run() {
				setCurrent_turn_time(0);
				for(long time = (int) playtime; time >= 0; time--) {
					if(input.equals("NDONE")){
						try {
							Thread.sleep(1000);
							setCurrent_turn_time(time);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					else if(input.equals("DONE")) {
						return;
					}
					else {
					try {
						game.Overtime();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					return;
					}
				}
			}
		};
		turntimer.setDaemon(true);
		turntimer.start();
		
	}
	
	public void turncomplete() {
		input = "DONE";
	}

	public long getTotaltime() {
		return totaltime;
	}

	public void setTotaltime(long totaltime) {
		this.totaltime = totaltime;
	}

	public void setinput(String string) {
		this.input = string;
		
	}

	public long getCurrent_turn_time() {
		return current_turn_time;
	}

	public void setCurrent_turn_time(long current_turn_time) {
		this.current_turn_time = current_turn_time;
	}
}
