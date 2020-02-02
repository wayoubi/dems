package ca.concordia.ginacody.comp6231.facade;

import ca.concordia.ginacody.comp6231.dao.Database;
import ca.concordia.ginacody.comp6231.dao.EventDAO;
import ca.concordia.ginacody.comp6231.enums.EventType;
import ca.concordia.ginacody.comp6231.exception.EventManagementServiceException;
import ca.concordia.ginacody.comp6231.services.EventManagementService;
import ca.concordia.ginacody.comp6231.vo.EventVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.RemoteException;

import static java.util.UUID.randomUUID;

public class EventManagementBusinessFacade implements EventManagementService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventManagementBusinessFacade.class);

    @Override
    public String login(String userName) throws EventManagementServiceException {
        LOGGER.info("User {} Logged in", userName);
        //return String.format("User %s logged in successfully, session-id %s", userName, randomUUID().toString());
        return String.format("User %s logged in successfully", userName);
    }

    @Override
    public String addEvent(String eventID, EventType eventType, int bookingCapacity) throws EventManagementServiceException {
        LOGGER.info("Creating Event, EventID {}, EventType {}, Booking Capacity {}", eventID, eventType, bookingCapacity);
        EventVO eventVO = new EventVO(eventID, eventType, bookingCapacity);
        EventDAO eventDAO = new EventDAO();
        return eventDAO.addEvent(eventVO);
    }

    @Override
    public String removeEvent(String eventID, EventType eventType) throws EventManagementServiceException {
        LOGGER.info("Removing Event, EventID {}, EventType {}", eventID, eventType);
        EventVO eventVO = new EventVO(eventID, eventType);
        EventDAO eventDAO = new EventDAO();
        return eventDAO.removeEvent(eventVO);
    }

    @Override
    public String listEventAvailability(EventType eventType) throws EventManagementServiceException {
        LOGGER.info("Listing Available Events, EventType {}", eventType);
        EventDAO eventDAO = new EventDAO();
        return eventDAO.selectAllEvents(eventType);
    }
}