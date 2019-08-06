package New_KI.KI;

import java.io.IOException;
import java.util.Random;

import Model.ENGINE_Client;
import de.upb.swtpra1819interface.models.ClientType;
import de.upb.swtpra1819interface.models.GameState;
import de.upb.swtpra1819interface.parser.ParsingException;

/**
 * Class to be called to initiate the KI and connect it to the Server
 * @author Lukas
 *
 */
public class Initiate_ENGINEPLAYER{
	public static void main(String[] args) throws IOException, ParsingException, InterruptedException {
		ENGINE_Client client = new ENGINE_Client("swtpra.cs.upb.de", 33100);
		client.connect();
	}
	
}
