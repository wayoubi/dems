package ca.concordia.ginacody.comp6231.facade;

import ca.concordia.ginacody.comp6231.delegate.EventManagementBusinessDelegate;
import ca.concordia.ginacody.comp6231.enums.EventType;
import ca.concordia.ginacody.comp6231.exception.EventManagementServiceException;
import ca.concordia.ginacody.comp6231.services.EventManagementService;
import ca.concordia.ginacody.comp6231.vo.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.RemoteException;

import static java.lang.Math.random;
import static java.util.UUID.randomUUID;

public class EventManagementBusinessFacade implements EventManagementService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventManagementBusinessFacade.class);

    @Override
    public String login(String userName) throws EventManagementServiceException {
        LOGGER.info("User {} Logged in", userName);
        if (random() < 0.3) {
            throw new EventManagementServiceException("Booking unavailable");
        }
        return userName+randomUUID().toString();
    }

    @Override
    public String addEvent(String eventID, EventType eventType, int bookingCapacity) throws EventManagementServiceException {
        LOGGER.info("Creating Event, EventID {}, EventType {}, Booking Capacity {}", eventID, eventType, bookingCapacity);
        Event event = new Event();
        event.setId(eventID);
        event.setEventType(eventType);
        event.setCapacity(bookingCapacity);
        return "Event Created Successfully";
    }
}
