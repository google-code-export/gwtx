package com.googlecode.gwtx.java.util.logging.client;

import java.util.logging.LogRecord;
import java.util.logging.Formatter;

/**
 * A ConsoleHandler that discards all messages.
 */
class ConsoleHandlerImplNull extends ConsoleHandlerImpl {
    public void init() {
    }

    public void publish(final LogRecord record, final Formatter formatter) {
        // swallowed
    }
}
