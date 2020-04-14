package com.agilisys;

import com.agilisys.Configuration.ApplicationResourceConfig;
import com.agilisys.RestServices.BusinessService;
import org.eclipse.jetty.server.Server;

import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


public class Application extends javax.ws.rs.core.Application {

    private static final BusinessService repository = BusinessService.getInstance();
    private static Logger logger = Logger.getLogger("MoneyTransactionAPI");

    public static void main(String[] args) {

        try {

            /* Initialize businessService*/
            BusinessService.getInstance();

            //Initialize sample data
            repository.loadSampleData();

            /*Config the server*/
            ApplicationResourceConfig config = new ApplicationResourceConfig();

            // Start server
            Server server = new Server(8000);
            server.setHandler(config.getHandlers());
            server.start();
            //server.join();
            configLogger();
            logger.info("Application startup complete; url = " + server.getURI());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void configLogger() {

        logger.setLevel(Level.INFO);

        FileHandler fh = null;
        try {
            fh = new FileHandler("MoneyTransactionAPI.log");
        } catch (Exception e) {
            e.printStackTrace();
        }
        fh.setFormatter(new SimpleFormatter());
        fh.setLevel(Level.INFO);
        logger.addHandler(fh);
    }
}


