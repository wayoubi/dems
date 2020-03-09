package ca.concordia.ginacody.comp6231.services.ws;

import ca.concordia.ginacody.comp6231.enums.EventType;
import ca.concordia.ginacody.comp6231.exception.EventManagementServiceException;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.rmi.RemoteException;

@WebService
public interface EventManagementService {

    @WebMethod
    public String login(String userName) throws EventManagementServiceException;

    @WebMethod
    public String addEvent(@WebParam(name = "EventID") String eventID, @WebParam(name = "EventType") EventType eventType,@WebParam(name = "BookingCapacity") int bookingCapacity) throws EventManagementServiceException;

    @WebMethod
    public String removeEvent(String eventID, EventType eventType) throws EventManagementServiceException;

    @WebMethod
    public String listEventAvailability(EventType eventType) throws EventManagementServiceException;

    @WebMethod
    public String bookEvent(String customerID, String eventID, EventType eventType) throws EventManagementServiceException;

    @WebMethod
    public String getBookingSchedule(String customerID) throws EventManagementServiceException;

    @WebMethod
    public String cancelEvent(String customerID, String eventID, EventType eventType) throws EventManagementServiceException;

    @WebMethod
    public String swapEvent(String customerID, String eventID, String eventType, String oldEventID, String oldEventType) throws EventManagementServiceException;
}
