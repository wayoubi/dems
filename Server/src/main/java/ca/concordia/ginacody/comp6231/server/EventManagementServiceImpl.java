package ca.concordia.ginacody.comp6231.server;



import ca.concordia.ginacody.comp6231.services.EventManagementServiceException;
import ca.concordia.ginacody.comp6231.services.EventManagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.lang.Math.random;
import static java.util.UUID.randomUUID;

public class EventManagementServiceImpl implements EventManagementService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventManagementServiceImpl.class);

    @Override
    public String login(String userName) throws EventManagementServiceException {
        LOGGER.info("User {} Logged in", userName);
        if (random() < 0.3) throw new EventManagementServiceException("Cab unavailable");
        return userName+randomUUID().toString();
    }
}
