package jeff.demo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.FileAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.filter.ThreadContextMapFilter;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.util.KeyValuePair;

public class Log4j2Util {

    private final static String PATTERN = "%d{HH:mm:ss.SSS} [%t] %p %c{2}: %m%n";
    public final static String DISCRIMINATOR_KEY = "logFileName";

    public synchronized static boolean addFileAppender(String logFileName, Logger logger) {
        try {
            final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
            final Configuration config = ctx.getConfiguration();

            // create layout
            Layout layout = PatternLayout.createLayout(PATTERN, null, config, null,
                    null,false, false, null, null);

            // create filter
            KeyValuePair kvp = KeyValuePair.newBuilder().setKey(DISCRIMINATOR_KEY).setValue(logFileName).build();
            KeyValuePair[] kvps = new KeyValuePair[] {kvp};
            Filter filter = ThreadContextMapFilter.createFilter(kvps, null, Filter.Result.ACCEPT, Filter.Result.DENY);
            filter.start();

            // create appender
            Appender appender = FileAppender.createAppender("log/" + logFileName +".log", "false", "false", logFileName, "true",
                    "false", "true", "4000", layout, filter, "false", null, config);
            appender.start();
            config.addAppender(appender);

            // add appender
            LoggerConfig loggerConfig = config.getLoggerConfig(logger.getName());
            loggerConfig.addAppender(appender, null, null);
            ctx.updateLoggers();
        } catch (Exception e) {
            logger.warn("Failed to add appender to log4j logger", e);
            return false;
        }
        return true;
    }

    public static void removeFileAppender(String logFileName, Logger logger) {
        final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        final Configuration config = ctx.getConfiguration();
        LoggerConfig loggerConfig = config.getLoggerConfig(logger.getName());
        loggerConfig.removeAppender(logFileName);
    }
}
