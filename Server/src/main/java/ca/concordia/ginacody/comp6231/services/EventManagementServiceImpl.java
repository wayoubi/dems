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

public class EventManagementServiceImpl extends UnicastRemoteObject implements EventManagementService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventManagementServiceImpl.class);

    private EventManagementBusinessFacade eventManagementBusinessFacade = new EventManagementBusinessFacade();

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
        int m=0;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            stringBuilder.append(eventManagementBusinessFacade.listEventAvailability(eventType));
        }catch(EventManagementServiceException e) {
            stringBuilder.append(String.format("%s from local server %s", e.getMessage(), Configuration.SERVER_LOCATION));
            stringBuilder.append(System.lineSeparator());
        }
        RequestProcessor requestProcessor = new RequestProcessor(String.format("listEventAvailability:%s:", eventType.getName()));
        requestProcessor.setName(String.format("Request Processor - %s", requestProcessor.hashCode()));
        requestProcessor.start();
        try {
            requestProcessor.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stringBuilder.append(requestProcessor.getReplyMessage());
        return stringBuilder.toString();
    }
}
