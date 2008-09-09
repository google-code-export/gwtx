package com.googlecode.gwtx.java.util.logging.client;

import java.util.logging.LogRecord;
import java.util.logging.Formatter;
import java.util.logging.Handler;

/**
 * Base for all console handlers. This implementation discards all logged messages.
 */
public class ConsoleHandlerImpl {
    public void init(final Handler handler) {
        // nothing
    }

    public void publish(final LogRecord record, final Formatter formatter) {
        // swallowed
    }
}
