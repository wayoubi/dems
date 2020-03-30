package ca.concordia.ginacody.comp6231.services.ws;

import ca.concordia.ginacody.comp6231.enums.EventType;
import ca.concordia.ginacody.comp6231.exception.EventManagementServiceException;
import ca.concordia.ginacody.comp6231.facade.EventManagementBusinessDelegate;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService(endpointInterface = "ca.concordia.ginacody.comp6231.services.ws.EventManagementService")
public class EventManagementServiceImpl implements EventManagementService {

    @WebMethod
    @Override
    public String login(String userName) throws EventManagementServiceException {
        EventManagementBusinessDelegate eventManagementBusinessDelegate = new EventManagementBusinessDelegate();
        return eventManagementBusinessDelegate.login(userName);
    }

    @WebMethod
    @Override
    public String addEvent(String eventID, EventType eventType, int bookingCapacity) throws EventManagementServiceException {
        EventManagementBusinessDelegate eventManagementBusinessDelegate = new EventManagementBusinessDelegate();
        return eventManagementBusinessDelegate.addEvent(eventID, eventType, bookingCapacity);
    }

    @WebMethod
    @Override
    public String removeEvent(String eventID, EventType eventType) throws EventManagementServiceException {
        EventManagementBusinessDelegate eventManagementBusinessDelegate = new EventManagementBusinessDelegate();
        return eventManagementBusinessDelegate.removeEvent(eventID, eventType);
    }

    @WebMethod
    @Override
    public String listEventAvailability(EventType eventType) throws EventManagementServiceException {
        EventManagementBusinessDelegate eventManagementBusinessDelegate = new EventManagementBusinessDelegate();
        return eventManagementBusinessDelegate.listEventAvailability(eventType);
    }

    @WebMethod
    @Override
    public String bookEvent(String customerID, String eventID, EventType eventType) throws EventManagementServiceException {
        EventManagementBusinessDelegate eventManagementBusinessDelegate = new EventManagementBusinessDelegate();
        return eventManagementBusinessDelegate.bookEvent(customerID, eventID, eventType);
    }

    @WebMethod
    @Override
    public String getBookingSchedule(String customerID) throws EventManagementServiceException {
        EventManagementBusinessDelegate eventManagementBusinessDelegate = new EventManagementBusinessDelegate();
        return eventManagementBusinessDelegate.getBookingSchedule(customerID);
    }

    @WebMethod
    @Override
    public String cancelEvent(String customerID, String eventID, EventType eventType) throws EventManagementServiceException {
        EventManagementBusinessDelegate eventManagementBusinessDelegate = new EventManagementBusinessDelegate();
        return eventManagementBusinessDelegate.cancelEvent(customerID, eventID, eventType);
    }

    @Override
    public String swapEvent(String customerID, String eventID, String eventType, String oldEventID, String oldEventType) throws EventManagementServiceException {
        EventManagementBusinessDelegate eventManagementBusinessDelegate = new EventManagementBusinessDelegate();
        return eventManagementBusinessDelegate.swapEvent(customerID, eventID, EventType.get(eventType), oldEventID, EventType.get(oldEventType));
    }
}
