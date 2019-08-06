package de.upb.swtpra1819interface.messages;

public class TileSwapValid extends Message {
	
	private boolean validation;
	
	public static final int uniqueID = 412;
	
	public TileSwapValid(boolean validation) {
		super(uniqueID);
		this.validation = validation;
	}

	public boolean isValidation() {
		return validation;
	}

	public void setValidation(boolean validation) {
		this.validation = validation;
	}

}
