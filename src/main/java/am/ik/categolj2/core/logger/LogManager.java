package am.ik.categolj2.core.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogManager {
    /**
     * Returns a Logger with the name of the calling class.
     *
     * @return The Logger for the calling class.
     */
    public static Logger getLogger() {
        return LoggerFactory.getLogger(getClassName(2));
    }

    /**
     * Gets the class name of the caller in the current stack at the given {@code depth}.
     *
     * @param depth a 0-based index in the current stack.
     * @return a class name
     */
    private static String getClassName(final int depth) {
        return new Throwable().getStackTrace()[depth].getClassName();
    }
}
