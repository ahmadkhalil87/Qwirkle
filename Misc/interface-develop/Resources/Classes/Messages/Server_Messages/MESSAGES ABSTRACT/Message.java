import com.google.gson.*;
public abstract class Message {
	
	private int uniqueID;
	
	public void setUniqueID(int uniqueID) {
		this.uniqueID = uniqueID;
	}
	
	public int getUniqueID() {
		return this.uniqueID;
	}
	
	public String getMessage() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}
}
