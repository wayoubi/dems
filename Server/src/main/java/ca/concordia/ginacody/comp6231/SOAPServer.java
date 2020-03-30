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
            Endpoint.publish(String.format("http://localhost:%s/demsservice", Configuration.HTTP_PORT), new EventManagementServiceImpl());
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        LOGGER.info(String.format("%s DEMS SOAP Service is published on port %s ...", Configuration.SERVER_LOCATION, Configuration.HTTP_PORT));
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