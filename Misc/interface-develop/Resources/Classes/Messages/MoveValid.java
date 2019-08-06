public class MoveValid extends Message {
	private boolean validation;
	private String message;
	
	public MoveValid(boolean validation, String message) {
		this.setUniqueID(415);
		this.validation = validation;
		this.message = message;
	}
}
