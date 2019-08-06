package log4j;


import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TestLogger {

	private static final Logger LOG = LogManager.getLogger(TestLogger.class);

	public static void main(String[] args) {
		
		LOG.debug("This Will Be Printed On Debug");
		LOG.info("This Will Be Printed On Info");
		LOG.warn("This Will Be Printed On Warn");
		LOG.error("This Will Be Printed On Error");
		LOG.fatal("This Will Be Printed On Fatal");
		LOG.log(Level.getLevel("CONNECT"), "Connect");
		LOG.info("Appending string: {}.", "Hello, World");
	}
}
