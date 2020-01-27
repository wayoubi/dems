package ca.concordia.ginacody.comp6231.services;

import ca.concordia.ginacody.comp6231.delegate.EventManagementBusinessDelegate;
import ca.concordia.ginacody.comp6231.enums.EventType;
import ca.concordia.ginacody.comp6231.exception.EventManagementServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import static java.lang.Math.random;
import static java.util.UUID.randomUUID;

public class EventManagementServiceRMIImpl extends UnicastRemoteObject implements EventManagementService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventManagementServiceRMIImpl.class);

    private EventManagementBusinessDelegate eventManagementBusinessDelegate = new EventManagementBusinessDelegate();

    public EventManagementServiceRMIImpl() throws RemoteException {
        super( );
    }

    @Override
    public String login(String userName) throws EventManagementServiceException, RemoteException {
        return eventManagementBusinessDelegate.login(userName);
    }

    @Override
    public String addEvent(String eventID, EventType eventType, int bookingCapacity) throws EventManagementServiceException, RemoteException {
        return eventManagementBusinessDelegate.addEvent(eventID, eventType, bookingCapacity);
    }
}
