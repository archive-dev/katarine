package io.github.whoisamyy.logging;

import org.apache.logging.log4j.LogManager;

public class Logger {
    private static LogLevel projectLogLevel;

    protected org.apache.logging.log4j.Logger logger;
    private LogLevel logLevel;

    public Logger() {
        logger = LogManager.getLogger(this.getClass());
    }

    public Logger(String loggerName) {
        logger = LogManager.getLogger(loggerName);
    }

    public LogLevel getLogLevel() {
        return logLevel;
    }

    public Logger setLogLevel(LogLevel logLevel) {
        this.logLevel = logLevel;
        return this;
    }

    /**
     * Logs out message with current object logLevel.
     * @param message message content.
     */
    public void log(String message) {
        switch (logLevel) {
            case INFO -> info(message);
            case DEBUG -> debug(message);
            case WARN -> warn(message);
            case ERROR -> error(message);
            default -> {}
        }
    }

    /**
     * Logs out message with current object logLevel.
     * @param message message content. String is preferred.
     */
    public void log(Object message) {
        log(message.toString());
    }

    public void info(String message) {
        if (logLevel==LogLevel.INFO || (logLevel == null && LoggingSettings.logLevels.contains(LogLevel.INFO))) {
            logger.info(message);
        }
    }

    public void debug(String message) {
        if (logLevel==LogLevel.DEBUG || (logLevel == null && LoggingSettings.logLevels.contains(LogLevel.DEBUG))) {
            logger.debug(message);
        }
    }

    public void error(String message) {
        if (logLevel==LogLevel.ERROR || (logLevel == null && LoggingSettings.logLevels.contains(LogLevel.ERROR))) {
            logger.error(message);
        }
    }

    public void warn(String message) {
        if (logLevel==LogLevel.WARN || (logLevel == null && LoggingSettings.logLevels.contains(LogLevel.WARN))) {
            logger.warn(message);
        }
    }

    public static void setProjectLogLevel(LogLevel projectLogLevel) {
        Logger.projectLogLevel = projectLogLevel;
    }
}
