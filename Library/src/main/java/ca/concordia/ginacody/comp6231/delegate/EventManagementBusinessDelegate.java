package ca.concordia.ginacody.comp6231.delegate;

import ca.concordia.ginacody.comp6231.enums.EventType;
import ca.concordia.ginacody.comp6231.exception.EventManagementServiceException;
import ca.concordia.ginacody.comp6231.facade.EventManagementBusinessFacade;
import ca.concordia.ginacody.comp6231.services.EventManagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventManagementBusinessDelegate implements EventManagementService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventManagementBusinessDelegate.class);

    EventManagementBusinessFacade eventManagementBusinessFacade = new EventManagementBusinessFacade();

    @Override
    public String login(String userName) throws EventManagementServiceException {
        return eventManagementBusinessFacade.login(userName);
    }

    @Override
    public String addEvent(String eventID, EventType eventType, int bookingCapacity) throws EventManagementServiceException {
        return eventManagementBusinessFacade.addEvent(eventID, eventType, bookingCapacity);
    }
}