public class TileSwapValid extends Message {
	private boolean validation;
	
	public TileSwapValid(boolean validation) {
		this.setUniqueID(412);
		this.validation = validation;
	}
}
