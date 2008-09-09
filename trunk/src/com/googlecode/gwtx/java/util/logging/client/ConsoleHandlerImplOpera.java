package com.googlecode.gwtx.java.util.logging.client;

import java.util.logging.LogRecord;
import java.util.logging.Formatter;

/**
 * Logs messages to <code>window.opera.postError(String)</code>.
 */
public class ConsoleHandlerImplOpera extends ConsoleHandlerImplSimple {
    protected native void log(String msg) /*-{
      $wnd.opera.postError(msg);
    }-*/;
}
