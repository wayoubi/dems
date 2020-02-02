package ca.concordia.ginacody.comp6231.services;

import ca.concordia.ginacody.comp6231.config.Configuration;
import ca.concordia.ginacody.comp6231.enums.EventType;
import ca.concordia.ginacody.comp6231.exception.EventManagementServiceException;
import ca.concordia.ginacody.comp6231.facade.EventManagementBusinessFacade;
import ca.concordia.ginacody.comp6231.processors.RequestProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 */
public class EventManagementServiceImpl extends UnicastRemoteObject implements EventManagementService {

    /**
     *
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(EventManagementServiceImpl.class);

    /**
     *
     */
    private EventManagementBusinessFacade eventManagementBusinessFacade = new EventManagementBusinessFacade();

    /**
     *
     * @throws RemoteException
     */
    public EventManagementServiceImpl() throws RemoteException {
        super( );
    }

    @Override
    public String login(String userName) throws EventManagementServiceException, RemoteException {
        return eventManagementBusinessFacade.login(userName);
    }

    @Override
    public String addEvent(String eventID, EventType eventType, int bookingCapacity) throws EventManagementServiceException, RemoteException {
        return eventManagementBusinessFacade.addEvent(eventID, eventType, bookingCapacity);
    }

    @Override
    public String removeEvent(String eventID, EventType eventType) throws EventManagementServiceException, RemoteException {
        return eventManagementBusinessFacade.removeEvent(eventID, eventType);
    }

    @Override
    public String listEventAvailability(EventType eventType) throws EventManagementServiceException, RemoteException {

        LOGGER.info("Listing Available Events from local server, EventType {}", eventType);

        StringBuilder stringBuilder = new StringBuilder();
        try {
            stringBuilder.append(eventManagementBusinessFacade.listEventAvailability(eventType));
        }catch(EventManagementServiceException e) {
            stringBuilder.append(String.format("%s from local server %s", e.getMessage(), Configuration.SERVER_LOCATION));
            stringBuilder.append(System.lineSeparator());
        }

        LOGGER.info("Listing Available Events from remote locations, EventType {}", eventType);
        Configuration.UDP_SERVERS_PORTS.keySet().stream().forEach(location -> {
            if(!Configuration.SERVER_LOCATION.equals(location)) {
                System.out.println(String.format(">>>>>>>>>> remote %s local %s", location ,Configuration.SERVER_LOCATION));
                LOGGER.info("Listing Available Events from {}, EventType {}", location, eventType);
                RequestProcessor requestProcessor = new RequestProcessor(location, String.format("%s:listEventAvailability:%s:", Configuration.SERVER_LOCATION, eventType.getName()));
                requestProcessor.setName(String.format("Request Processor - %s", requestProcessor.hashCode()));
                requestProcessor.start();
                try {
                    LOGGER.info("Waiting for Listing Available Events from {}, EventType {}", location, eventType);
                    requestProcessor.join();
                } catch (InterruptedException intex) {
                    LOGGER.error("{} caused by {}", intex.getMessage(), intex.getCause().getMessage());
                }
                LOGGER.info("Adding response - Listing Available Events from {}, EventType {}", location, eventType);
                stringBuilder.append(requestProcessor.getReplyMessage());
            }
        });
        return stringBuilder.toString();
    }
}
