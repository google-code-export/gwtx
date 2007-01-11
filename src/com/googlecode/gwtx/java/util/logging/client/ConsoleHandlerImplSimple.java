package com.googlecode.gwtx.java.util.logging.client;

import java.util.logging.LogRecord;
import java.util.logging.Formatter;

/**
 * Provides a handlers that delegates to a log method.
 */
public abstract class ConsoleHandlerImplSimple extends ConsoleHandlerImpl {
    public void publish(final LogRecord record, final Formatter formatter) {
        log(formatter.format(record));
    }

    protected abstract void log(String msg);
}
