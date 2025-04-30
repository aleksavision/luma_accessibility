package tools;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class InfoLogger {

    private static final Logger logger = LogManager.getLogger(PageTools.class);

    public static void logInfo(String message) {
        logger.info(message);
    }

    public static void logDebug(String message) {
        logger.debug(message);
    }

    public static void logError(String message) {
        logger.error(message);
    }

    public static void logFatal(String message) {
        logger.fatal(message);
    }
}
