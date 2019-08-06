package qwirkle.Desktop.Game;

import qwirkle.Desktop.enumeration.WrongMove;
import qwirkle.Desktop.enumeration.SlowMove;

public class Configuration {
	
	private int colorShapeCount;
	private int tileCount;
	private int maxHandTiles;
	private long turnTime;
	private long timeVisualization;
	private WrongMove wrongMove;
	private int wrongMovePenalty;
	private SlowMove slowMove;
	private int slowMovePenalty;
	private int maxPlayerNumber;

	public Configuration(int colorShapeCount, int tileCount, int maxHandTiles, long turnTime, long timeVisualization,
			WrongMove wrongMove, int wrongMovePenalty, SlowMove slowMove, int slowMovePenalty, int maxPlayerNumber) {
		super();
		this.colorShapeCount = colorShapeCount;
		this.tileCount = tileCount;
		this.maxHandTiles = maxHandTiles;
		this.turnTime = turnTime;
		this.timeVisualization = timeVisualization;
		this.wrongMove = wrongMove;
		this.wrongMovePenalty = wrongMovePenalty;
		this.slowMove = slowMove;
		this.slowMovePenalty = slowMovePenalty;
		this.maxPlayerNumber = maxPlayerNumber;
	}

	public int getColorShapeCount() {
		return colorShapeCount;
	}

	public void setColorShapeCount(int colorShapeCount) {
		this.colorShapeCount = colorShapeCount;
	}

	public int getTileCount() {
		return tileCount;
	}

	public void setTileCount(int tileCount) {
		this.tileCount = tileCount;
	}

	public int getMaxHandTiles() {
		return maxHandTiles;
	}

	public void setMaxHandTiles(int maxHandTiles) {
		this.maxHandTiles = maxHandTiles;
	}

	public long getTurnTime() {
		return turnTime;
	}

	public void setTurnTime(long turnTime) {
		this.turnTime = turnTime;
	}

	public long getTimeVisualization() {
		return timeVisualization;
	}

	public void setTimeVisualization(long timeVisualization) {
		this.timeVisualization = timeVisualization;
	}

	public int getWrongMovePenalty() {
		return wrongMovePenalty;
	}

	public void setWrongMovePenalty(int wrongMovePenalty) {
		this.wrongMovePenalty = wrongMovePenalty;
	}

	public int getSlowMovePenalty() {
		return slowMovePenalty;
	}

	public void setSlowMovePenalty(int slowMovePenalty) {
		this.slowMovePenalty = slowMovePenalty;
	}

	@Override
	public String toString() {
		return "Configuration [colorShapeCount=" + colorShapeCount + ", tileCount=" + tileCount + ", maxHandTiles="
				+ maxHandTiles + ", turnTime=" + turnTime + ", timeVisualization=" + timeVisualization + ", wrongMove="
				+ wrongMove + ", wrongMovePenalty=" + wrongMovePenalty + ", slowMove=" + slowMove + ", slowMovePenalty="
				+ slowMovePenalty + "]";
	}

	public WrongMove getWrongMove() {
		return wrongMove;
	}

	public void setWrongMove(WrongMove wrongMove) {
		this.wrongMove = wrongMove;
	}

	public SlowMove getSlowMove() {
		return slowMove;
	}

	public void setSlowMove(SlowMove slowMove) {
		this.slowMove = slowMove;
	}

	public int getMaxPlayerNumber() {
		return maxPlayerNumber;
	}

	public void setMaxPlayerNumber(int maxPlayerNumber) {
		this.maxPlayerNumber = maxPlayerNumber;
	}
}
