package io.github.whoisamyy.logging;

import org.apache.logging.log4j.LogManager;

public abstract class Logger {
    private static LogLevel projectLogLevel;

    protected org.apache.logging.log4j.Logger logger;
    private LogLevel logLevel = LogLevel.DEBUG;

    public Logger() {
        logger = LogManager.getLogger(this.getClass());
    }

    public LogLevel getLogLevel() {
        return logLevel;
    }

    public static void setProjectLogLevel(LogLevel projectLogLevel) {
        Logger.projectLogLevel = projectLogLevel;
    }
}
