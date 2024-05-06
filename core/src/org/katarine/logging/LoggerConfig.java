package org.katarine.logging;

// does not work do not use
public class LoggerConfig {
    public boolean showCaller = true, showLine = true, showMethod = true;
    public LogLevel logLevel = LogLevel.DEBUG;
    public String loggerName = null;

    public LoggerConfig() { }

    public LoggerConfig(boolean showCaller, boolean showLine, boolean showMethod, LogLevel logLevel) {
        this.showCaller = showCaller;
        this.showLine = showLine;
        this.showMethod = showMethod;
        this.logLevel = logLevel;
    }

    public LoggerConfig setShowCaller(boolean showCaller) {
        this.showCaller = showCaller;
        return this;
    }

    public LoggerConfig setShowLine(boolean showLine) {
        this.showLine = showLine;
        return this;
    }

    public LoggerConfig setShowMethod(boolean showMethod) {
        this.showMethod = showMethod;
        return this;
    }

    public LoggerConfig setLogLevel(LogLevel logLevel) {
        this.logLevel = logLevel;
        return this;
    }

    public LoggerConfig setLoggerName(String loggerName) {
        this.loggerName = loggerName;
        return this;
    }
}
