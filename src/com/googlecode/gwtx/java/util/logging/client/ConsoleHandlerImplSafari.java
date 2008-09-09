package com.googlecode.gwtx.java.util.logging.client;

import java.util.logging.LogRecord;
import java.util.logging.Formatter;
import java.util.logging.Handler;

/**
 * Logs messages to <code>window.console.log(String)</code>.
 */
public class ConsoleHandlerImplSafari extends ConsoleHandlerImplSimple {

    protected native void log(String msg) /*-{
      $wnd.console.log(msg);
    }-*/;
}
