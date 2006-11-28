package logger.test.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.*;

import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: sandymac
 * Date: Nov 28, 2006
 * Time: 3:48:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestLogging implements EntryPoint {

    private static final Logger logger = Logger.getLogger(GWT.getTypeName(TestLogging.class));

    public void onModuleLoad() {

        logger.finest("Finest");
        logger.finer("Finer");
        logger.fine("Fine");
        logger.config("Config");
        logger.info("Info");
        logger.warning("Warning");
        logger.severe("Severe");

        RootPanel.get().add(new Label("Loaded!"));
    }
}
