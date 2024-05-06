package org.katarine.logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.ThreadContext;

/**
 * Provides methods for logging, which give more information about code execution, like class and line.
 */
public class Logger {
    private static LogLevel projectLogLevel;

    protected org.apache.logging.log4j.Logger logger;
    private LogLevel logLevel= LogLevel.PROD;
    private LoggerConfig config;

    public Logger() {
        logger = LogManager.getLogger(this.getClass());
        this.config = new LoggerConfig();
    }

    public Logger(LogLevel logLevel) {
        this();
        this.logLevel = logLevel;
    }

    public Logger(String loggerName) {
        logger = LogManager.getLogger(loggerName);
    }

    public Logger(LoggerConfig config) {
        this.config = config;
        if (config.loggerName == null)
            logger = LogManager.getLogger(this.getClass());
        else logger = LogManager.getLogger(config.loggerName);
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

    private void info(String message, StackTraceElement[] stackTrace, Throwable throwable) {
        if (logLevel==LogLevel.INFO || (logLevel == null && LoggingSettings.logLevels.contains(LogLevel.INFO))) {
            String[] className = stackTrace[2].getClassName().split("\\.");
            String caller = className[className.length-1];
            String callerMethod = throwable.getStackTrace()[1].getMethodName();
            String lineNum = String.valueOf(stackTrace[2].getLineNumber());

            ThreadContext.put("callerClass", caller);
            ThreadContext.put("lineNumber", lineNum);
            ThreadContext.put("callerMethod", callerMethod);

            logger.info(message);

            ThreadContext.clearAll();
        }
    }

    public void info(String message) {
        info(message, Thread.currentThread().getStackTrace(), new Throwable());
    }

    public void info(Object message) {
        info(message==null?"null":message.toString(), Thread.currentThread().getStackTrace(), new Throwable());
    }

    private void debug(String message, StackTraceElement[] stackTrace, Throwable throwable) {
        if (logLevel==LogLevel.DEBUG || (logLevel == null && LoggingSettings.logLevels.contains(LogLevel.DEBUG))) {
            String[] className = stackTrace[2].getClassName().split("\\.");
            String caller = className[className.length-1];
            String callerMethod = throwable.getStackTrace()[1].getMethodName();
            String lineNum = String.valueOf(stackTrace[2].getLineNumber());

            ThreadContext.put("callerClass", caller);
            ThreadContext.put("lineNumber", lineNum);
            ThreadContext.put("callerMethod", callerMethod);

            logger.debug(message);

            ThreadContext.clearAll();
        }
    }

    public void debug(String message) {
        debug(message, Thread.currentThread().getStackTrace(), new Throwable());
    }

    public void debug(Object message) {
        debug(message==null?"null":message.toString(), Thread.currentThread().getStackTrace(), new Throwable());
    }

    private void error(String message, StackTraceElement[] stackTrace, Throwable throwable) {
        if (logLevel==LogLevel.ERROR || (logLevel == null && LoggingSettings.logLevels.contains(LogLevel.ERROR))) {
            String[] className = stackTrace[2].getClassName().split("\\.");
            String caller = className[className.length-1];
            String callerMethod = throwable.getStackTrace()[1].getMethodName();
            String lineNum = String.valueOf(stackTrace[2].getLineNumber());

            ThreadContext.put("callerClass", caller);
            ThreadContext.put("lineNumber", lineNum);
            ThreadContext.put("callerMethod", callerMethod);

            logger.error(message);

            ThreadContext.clearAll();
        }
    }

    public void error(String message) {
        error(message, Thread.currentThread().getStackTrace(), new Throwable());
    }

    public void error(Object message) {
        error(message==null?"null":message.toString(), Thread.currentThread().getStackTrace(), new Throwable());
    }

    private void warn(String message, StackTraceElement[] stackTrace, Throwable throwable) {
        if (logLevel==LogLevel.WARN || (logLevel == null && LoggingSettings.logLevels.contains(LogLevel.WARN))) {
            String[] className = stackTrace[2].getClassName().split("\\.");
            String caller = className[className.length-1];
            String callerMethod = throwable.getStackTrace()[1].getMethodName();
            String lineNum = String.valueOf(stackTrace[2].getLineNumber());

            ThreadContext.put("callerClass", caller);
            ThreadContext.put("lineNumber", lineNum);
            ThreadContext.put("callerMethod", callerMethod);

            logger.warn(message);

            ThreadContext.clearAll();
        }
    }

    public void warn(String message) {
        warn(message, Thread.currentThread().getStackTrace(), new Throwable());
    }

    public void warn(Object message) {
        warn(message==null?"null":message.toString(), Thread.currentThread().getStackTrace(), new Throwable());
    }

    public static void setProjectLogLevel(LogLevel projectLogLevel) {
        Logger.projectLogLevel = projectLogLevel;
    }
}
