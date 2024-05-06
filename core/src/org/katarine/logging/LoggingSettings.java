package org.katarine.logging;

import java.util.HashSet;
import java.util.Set;

/**
 * Global logging config.
 */
public class LoggingSettings {
    public static Set<LogLevel> logLevels = new HashSet<>(Set.of(new LogLevel[]{LogLevel.DEBUG}));

    public static void setLogLevels(Set<LogLevel> logLevels) {
        LoggingSettings.logLevels = logLevels;
    }

    public static void addLogLevel(LogLevel logLevel) {
        logLevels.add(logLevel);
    }
}
