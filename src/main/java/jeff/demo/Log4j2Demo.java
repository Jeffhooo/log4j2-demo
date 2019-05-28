package jeff.demo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

public class Log4j2Demo {

    private static final Logger logger = LogManager.getLogger(Log4j2Demo.class);

    public static void main(String[] args) {

        new Thread(() -> {
            // add appender
            Log4j2Util.addFileAppender("thread-1", logger);
            ThreadContext.put(Log4j2Util.DISCRIMINATOR_KEY, "thread-1");

            // start log
            logger.trace("thread-1: Hello World!");
            logger.debug("thread-1: How are you today?");
            logger.info("thread-1: I am fine.");
            logger.warn("thread-1: I love programming.");
            logger.error("thread-1: I am programming.");

            // remove appender
            ThreadContext.remove(Log4j2Util.DISCRIMINATOR_KEY);
            Log4j2Util.removeFileAppender("thread-1", logger);
        }).start();

        new Thread(() -> {
            // add appender
            Log4j2Util.addFileAppender("thread-2", logger);
            ThreadContext.put(Log4j2Util.DISCRIMINATOR_KEY, "thread-2");

            // start log
            logger.trace("thread-2: Hello World!");
            logger.debug("thread-2: How are you today?");
            logger.info("thread-2: I am fine.");
            logger.warn("thread-2: I love programming.");
            logger.error("thread-2: I am programming.");

            // remove appender
            ThreadContext.remove(Log4j2Util.DISCRIMINATOR_KEY);
            Log4j2Util.removeFileAppender("thread-2", logger);
        }).start();

        // add appender
        Log4j2Util.addFileAppender("main", logger);
        ThreadContext.put(Log4j2Util.DISCRIMINATOR_KEY, "main");

        // start log
        logger.trace("main: Hello World!");
        logger.debug("main: How are you today?");
        logger.info("main: I am fine.");
        logger.warn("main: I love programming.");
        logger.error("main: I am programming.");

        // remove appender
        ThreadContext.remove(Log4j2Util.DISCRIMINATOR_KEY);
        Log4j2Util.removeFileAppender("main", logger);
    }
}
