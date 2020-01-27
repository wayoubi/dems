package ca.concordia.ginacody.comp6231.services;

import ca.concordia.ginacody.comp6231.enums.EventType;
import ca.concordia.ginacody.comp6231.exception.EventManagementServiceException;

import java.rmi.RemoteException;

/**
 * Hello world!
 */
public interface EventManagementService extends java.rmi.Remote {

    public String login(String userName) throws EventManagementServiceException, RemoteException;

    public String addEvent(String eventID, EventType eventType, int bookingCapacity) throws EventManagementServiceException, RemoteException;

}