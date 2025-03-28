package de.schoenfeld.chesskit.core;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class SimpleClassNameFormatter extends Formatter {
    @Override
    public String format(LogRecord record) {
        String simpleClassName = record.getSourceClassName();
        if (simpleClassName != null && simpleClassName.contains(".")) {
            simpleClassName = simpleClassName.substring(simpleClassName.lastIndexOf('.') + 1);
        }
        return String.format("[%4$s][%1$tF %1$tT] %2$s.%3$s - %5$s%n",
                record.getMillis(),
                simpleClassName, // Only the class name
                record.getSourceMethodName(),
                record.getLevel(),
                formatMessage(record));
    }
}
