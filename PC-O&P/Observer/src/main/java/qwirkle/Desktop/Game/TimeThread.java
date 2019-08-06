package qwirkle.Desktop.Game;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import qwirkle.Desktop.enumeration.GameState;

/**
 * Class that hosts all the Timer Methods for a Game
 * @author Lukas
 *
 */
public class TimeThread{
	
	private Game game;
	private long totaltime;
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
		ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
		ScheduledFuture<?> full = ses.schedule(new Runnable() {
		    @Override
		    public void run() {
		        System.out.println("Oops! Time is up - try again.");
		    }
		}, playertimer, TimeUnit.SECONDS);

		// check
		if (input == "Done") {
		    full.cancel(true);
		    input = "NDONE";
		}
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
}
