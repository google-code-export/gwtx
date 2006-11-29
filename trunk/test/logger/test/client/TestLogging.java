package logger.test.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.*;

import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.logging.Handler;

/**
 * Created by IntelliJ IDEA.
 * User: sandymac
 * Date: Nov 28, 2006
 * Time: 3:48:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestLogging implements EntryPoint {

    private static final Logger loggerP = Logger.getLogger("logger.test");
    private final Logger logger = Logger.getLogger(GWT.getTypeName(this));

    public void onModuleLoad() {

        logger.finest("Finest");
        //logger.finer("Finer");
        //logger.fine("Fine");
        logger.config("Config");
        logger.info("Info");
        //logger.warning("Warning");
        //logger.severe("Severe");

        loggerP.warning("loggerP");

        RootPanel.get().add(new Label("Loaded: " + logger.getName() + " : " + logger.getLevel()));
        RootPanel.get().add(new Label("Loaded: " + loggerP.getName()));

        Logger.getLogger("").getHandlers()[0].setLevel(Level.ALL);

        logger.finest("Finest before");
        logger.setLevel(Level.FINEST);
        RootPanel.get().add(new Label("FINER loggable: " + logger.isLoggable(Level.FINER)));
        logger.finest("Finest after");


        appendLoggers(logger);
    }

    private void appendLoggers(Logger logger) {
        if (logger == null) return;
        RootPanel.get().add(new Label("Logger: " + logger.getName() + " : " + logger.getLevel()));

        Handler[] handlers = logger.getHandlers();
        for (int i=0; i < handlers.length; i++) {
            Handler handler = handlers[i];
            RootPanel.get().add(new Label("Handler: " + GWT.getTypeName(handler) + " : " + handler.getLevel()));
        }

        appendLoggers(logger.getParent());
    }
}
