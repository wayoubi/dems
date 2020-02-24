package ca.concordia.ginacody.comp6231.services;

import ca.concordia.ginacody.comp6231.enums.EventType;
import ca.concordia.ginacody.comp6231.exception.EventManagementServiceException;
import ca.concordia.ginacody.comp6231.facade.EventManagementBusinessDelegate;
import dems.EventManagementServiceImplPOA;
import org.omg.CORBA.ORB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventManagementServiceCORBAImpl extends EventManagementServiceImplPOA {

    /**
     *
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(EventManagementServiceCORBAImpl.class);

    /**
     *
     */
    private ORB orb;

     /**
     * @param orb_val
     */
    public void setORB(ORB orb_val) {
        orb = orb_val;
    }

    @Override
    public String login(String userName) {
        try {
            EventManagementBusinessDelegate eventManagementBusinessDelegate = new EventManagementBusinessDelegate();
            return eventManagementBusinessDelegate.login(userName);
        } catch(EventManagementServiceException e) {
            return e.getMessage();
        }


    }

    @Override
    public String addEvent(String eventID, String eventType, int bookingCapacity) {
        try {
            EventManagementBusinessDelegate eventManagementBusinessDelegate = new EventManagementBusinessDelegate();
            return eventManagementBusinessDelegate.addEvent(eventID, EventType.get(eventType), bookingCapacity);
        } catch(EventManagementServiceException e) {
            return e.getMessage();
       }
    }

    @Override
    public String removeEvent(String eventID, String eventType) {
        try {
            EventManagementBusinessDelegate eventManagementBusinessDelegate = new EventManagementBusinessDelegate();
            return eventManagementBusinessDelegate.removeEvent(eventID, EventType.get(eventType));
        } catch(EventManagementServiceException e) {
            return e.getMessage();
        }
    }

    @Override
    public String listEventAvailability(String eventType) {
        try {
            EventManagementBusinessDelegate eventManagementBusinessDelegate = new EventManagementBusinessDelegate();
            return eventManagementBusinessDelegate.listEventAvailability(EventType.get(eventType));
        } catch(EventManagementServiceException e) {
            return e.getMessage();
        }
    }

    @Override
    public String bookEvent(String customerID, String eventID, String eventType) {
        try {
            EventManagementBusinessDelegate eventManagementBusinessDelegate = new EventManagementBusinessDelegate();
            return eventManagementBusinessDelegate.bookEvent(customerID, eventID, EventType.get(eventType));
        } catch(EventManagementServiceException e) {
            return e.getMessage();
        }
    }

    @Override
    public String getBookingSchedule(String customerID) {
        try {
            EventManagementBusinessDelegate eventManagementBusinessDelegate = new EventManagementBusinessDelegate();
            return eventManagementBusinessDelegate.getBookingSchedule(customerID);
        } catch(EventManagementServiceException e) {
            return e.getMessage();
        }
    }

    @Override
    public String cancelEvent(String customerID, String eventID, String eventType) {
        try {
            EventManagementBusinessDelegate eventManagementBusinessDelegate = new EventManagementBusinessDelegate();
            return eventManagementBusinessDelegate.cancelEvent(customerID, eventID, EventType.get(eventType));
        } catch(EventManagementServiceException e) {
            return e.getMessage();
        }
    }

    @Override
    public void shutdown() {
        orb.shutdown(false);
    }
}