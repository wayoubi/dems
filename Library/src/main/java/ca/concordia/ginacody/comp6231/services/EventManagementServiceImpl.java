package ca.concordia.ginacody.comp6231.services;

import ca.concordia.ginacody.comp6231.delegate.EventManagementBusinessDelegate;
import ca.concordia.ginacody.comp6231.enums.EventType;
import ca.concordia.ginacody.comp6231.exception.EventManagementServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.RemoteException;

public class EventManagementServiceImpl implements  EventManagementService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventManagementServiceImpl.class);

    private EventManagementBusinessDelegate eventManagementBusinessDelegate = new EventManagementBusinessDelegate();

    public EventManagementServiceImpl() {
        super( );
    }

    @Override
    public String login(String userName) throws EventManagementServiceException {
        return eventManagementBusinessDelegate.login(userName);
    }

    @Override
    public String addEvent(String eventID, EventType eventType, int bookingCapacity) throws EventManagementServiceException {
        return eventManagementBusinessDelegate.addEvent(eventID, eventType, bookingCapacity);
    }
}
