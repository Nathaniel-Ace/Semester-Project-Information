package logs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Test {
    private static final Logger logger = LoggerFactory.getLogger(Test.class);

    // Methode für allgemeine Logs
    public static void logInfo(String message) {
        logger.info(message);
    }

    // Methode für Debug-Logs
    public static void logDebug(String message) {
        logger.debug(message);
    }

    // Methode für Warnungen
    public static void logWarn(String message) {
        logger.warn(message);
    }

    // Methode für Fehler-Logs
    public static void logError(String message, Exception e) {
        logger.error(message, e);
    }
    //IN DocumentMapper
}
