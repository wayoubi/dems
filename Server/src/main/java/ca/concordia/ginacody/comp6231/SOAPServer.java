package ca.concordia.ginacody.comp6231;


import ca.concordia.ginacody.comp6231.config.Configuration;
import ca.concordia.ginacody.comp6231.services.ws.EventManagementServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.ws.Endpoint;

public class SOAPServer implements Runnable {

    /**
     *
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(SOAPServer.class);

    /**
     *
     */
    private boolean started;


    /**
     *
     */
    public SOAPServer() {
        this.setStarted(false);
    }

    @Override
    public void run() {
        try {
            this.setStarted(true);
            Endpoint.publish("http://localhost:9090/demsservice", new EventManagementServiceImpl());
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        LOGGER.info(String.format("DEMS SOAP Service %s is published ...", Configuration.SERVER_LOCATION));
    }

    /**
     * @param started
     */
    public void setStarted(boolean started) {
        this.started = started;
    }

    /**
     * @return
     */
    public boolean isStarted() {
        return started;
    }
}