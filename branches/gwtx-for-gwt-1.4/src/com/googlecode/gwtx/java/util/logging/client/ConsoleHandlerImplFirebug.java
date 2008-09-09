package com.googlecode.gwtx.java.util.logging.client;

import com.google.gwt.core.client.JavaScriptObject;

import java.util.logging.LogRecord;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.Handler;

/**
 * Loggs messages to Firebug's logging methods.
 *
 * @see <a href="http://getfirebug.com/">http://getfirebug.com/</a>
 */
public class ConsoleHandlerImplFirebug extends ConsoleHandlerImpl {

    private static final FirebugLevel ERROR = new FirebugLevel("error");
    private static final FirebugLevel WARN = new FirebugLevel("warn");
    private static final FirebugLevel INFO = new FirebugLevel("info");
    private static final FirebugLevel LOG = new FirebugLevel("log");


    public void init(final Handler handler) {
        super.init(handler);
    }

    public void publish(final LogRecord record, final Formatter formatter) {

        final FirebugLevel level;
        if (record.getLevel().intValue() >= Level.SEVERE.intValue()) {
            level = ERROR;
        } else if (record.getLevel().intValue() >= Level.WARNING.intValue()) {
            level = WARN;
        } else if (record.getLevel().intValue() >= Level.INFO.intValue()) {
            level = INFO;
        } else {
            level = LOG;
        }

        level.publish(formatter.format(record), record.getParameters());
    }

    private static class FirebugLevel {
        private final String level;

        public FirebugLevel(final String level) {
            this.level = level;
        }

        private static native void publish(String method, String msg) /*-{
          $wnd.console[method](msg);
        }-*/;

        private static native void publish(String method, String msg, Object p0) /*-{
          $wnd.console[method](msg, p0);
        }-*/;

        private static native void publish(String method, String msg, Object p0, Object p1) /*-{
          $wnd.console[method](msg, p0, p1);
        }-*/;

        private static native void publish(String method, String msg, Object p0, Object p1, Object p2) /*-{
          $wnd.console[method](msg, p0, p1, p2);
        }-*/;

        private static native void publish(String method, String msg, Object p0, Object p1, Object p2, Object p3) /*-{
          $wnd.console[method](msg, p0, p1, p2, p3);
        }-*/;

        private static native void publish(String method, String msg, Object p0, Object p1, Object p2, Object p3, Object p4) /*-{
          $wnd.console[method](msg, p0, p1, p2, p3, p4);
        }-*/;

        private static native void publish(String method, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) /*-{
          $wnd.console[method](msg, p0, p1, p2, p3, p4, p5);
        }-*/;

        private static native void publish(String method, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) /*-{
          $wnd.console[method](msg, p0, p1, p2, p3, p4, p5, p6);
        }-*/;

        private static native void publish(String method, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) /*-{
          $wnd.console[method](msg, p0, p1, p2, p3, p4, p5, p6, p7);
        }-*/;

        private static native void publish(String method, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) /*-{
          $wnd.console[method](msg, p0, p1, p2, p3, p4, p5, p6, p7, p8);
        }-*/;

        private static native void publish(String method, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) /*-{
          $wnd.console[method](msg, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
        }-*/;

        public void publish(final String msg, final Object[] p) {
            if (p == null || p.length == 0) {
                publish(level, msg);
            } else if (p.length == 1) {
                publish(level, msg, p[0]);
            } else if (p.length == 2) {
                publish(level, msg, p[0], p[1]);
            } else if (p.length == 3) {
                publish(level, msg, p[0], p[1], p[2]);
            } else if (p.length == 4) {
                publish(level, msg, p[0], p[1], p[2], p[3]);
            } else if (p.length == 5) {
                publish(level, msg, p[0], p[1], p[2], p[3], p[4]);
            } else if (p.length == 6) {
                publish(level, msg, p[0], p[1], p[2], p[3], p[4], p[5]);
            } else if (p.length == 7) {
                publish(level, msg, p[0], p[1], p[2], p[3], p[4], p[5], p[6]);
            } else if (p.length == 8) {
                publish(level, msg, p[0], p[1], p[2], p[3], p[4], p[5], p[6], p[7]);
            } else if (p.length == 9) {
                publish(level, msg, p[0], p[1], p[2], p[3], p[4], p[5], p[6], p[7], p[8]);
            } else if (p.length == 10) {
                publish(level, msg, p[0], p[1], p[2], p[3], p[4], p[5], p[6], p[7], p[8], p[9]);
            } else {
                publish(level, msg, p);
            }
        }
    }
}
