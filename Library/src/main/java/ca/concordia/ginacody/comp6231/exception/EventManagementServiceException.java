package ca.concordia.ginacody.comp6231.exception;

import java.rmi.RemoteException;

public class EventManagementServiceException extends Exception {
    public EventManagementServiceException(String message) {
        super(message);
    }
}