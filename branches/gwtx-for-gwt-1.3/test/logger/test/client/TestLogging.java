package logger.test.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.*;

import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.logging.Handler;

/**
 * Crude test app to excersize java.util.logging code in GWTx.
 */
public class TestLogging implements EntryPoint {

    private static final Logger loggerP = Logger.getLogger("logger.test");
    private final Logger logger = Logger.getLogger(GWT.getTypeName(this));

    public void onModuleLoad() {
        appendLoggers(logger);
        RootPanel.get("out").add(new Label("==="));

        logger.finest("Finest");
        logger.finer("Finer");
        logger.fine("Fine");
        logger.config("Config");
        logger.info("Info");
        logger.warning("Warning");
        logger.severe("Severe");

        loggerP.warning("loggerP");

        RootPanel.get("out").add(new Label("Loaded: " + logger.getName() + " : " + logger.getLevel()));
        RootPanel.get("out").add(new Label("Loaded: " + loggerP.getName()));

        //Logger.getLogger("").getHandlers()[0].setLevel(Level.ALL);

        logger.finest("Finest before");
        logger.setLevel(Level.FINEST);
        RootPanel.get("out").add(new Label("FINER loggable: " + logger.isLoggable(Level.FINER)));
        logger.finest("Finest after");

        Address addr = new Address();
        addr.number = 101;
        addr.street = "Anonymous Dr.";
        addr.state = "Florida";
        addr.zip = 12345;

        Person p = new Person();
        p.name = "Joe Bob";
        p.age = 34;
        p.address = addr;

        logger.log(Level.INFO, "Created Person", new Object[] {p});

    }

    private void appendLoggers(final Logger logger) {
        if (logger == null) return;
        RootPanel.get("out").add(new Label("Logger: " + logger.getName() + " : " + logger.getLevel()));

        Handler[] handlers = logger.getHandlers();
        for (int i=0; i < handlers.length; i++) {
            Handler handler = handlers[i];
            RootPanel.get("out").add(new Label("Handler: " + GWT.getTypeName(handler) + " : " + handler.getLevel()));
        }

        appendLoggers(logger.getParent());
    }

    static class Person {
        String name;
        int age;
        Address address;
    }

    static class Address {
        int number;
        String street;
        String state;
        int zip;
    }
}
