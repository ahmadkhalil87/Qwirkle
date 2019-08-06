public class Configuration {
	private int colorShapeCount;
	private int tileCount;
	private int maxHandTiles;
	private long turnTime;
	private long timeVisualization;
	private String wrongMove;
	private int wrongMovePenalty;
	private String slowMove;
	private int slowMovePenalty;
	
	public Configuration(int colorShapeCount, int tileCount, int maxHandTiles, long turnTime, long timeVisualization, String wrongMove, int wrongMovePenalty, String slowMove, int slowMovePenalty) {
		this.colorShapeCount = colorShapeCount;
		this.tileCount = tileCount;
		this.maxHandTiles = maxHandTiles;
		this.turnTime = turnTime;
		this.timeVisualization = timeVisualization;
		this.wrongMove = wrongMove;
		this.wrongMovePenalty = wrongMovePenalty;
		this.slowMove = slowMove;
		this.slowMovePenalty = slowMovePenalty;
	}
}
