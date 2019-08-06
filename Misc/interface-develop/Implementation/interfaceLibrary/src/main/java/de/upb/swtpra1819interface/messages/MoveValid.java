package de.upb.swtpra1819interface.messages;

public class MoveValid extends Message {
	
	private boolean validation;
	private String message;
	
	public static final int uniqueID = 415;
	
	public MoveValid(boolean validation, String message) {
		super(uniqueID);
		this.validation = validation;
		this.message = message;
	}

	public boolean isValidation() {
		return validation;
	}

	public void setValidation(boolean validation) {
		this.validation = validation;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
