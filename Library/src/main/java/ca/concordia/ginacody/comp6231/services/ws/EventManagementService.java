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
    public String removeEvent(@WebParam(name = "EventID") String eventID, @WebParam(name = "EventType")  EventType eventType) throws EventManagementServiceException;

    @WebMethod
    public String listEventAvailability(@WebParam(name = "EventType")  EventType eventType) throws EventManagementServiceException;

    @WebMethod
    public String bookEvent(@WebParam(name = "CustomerID") String customerID, @WebParam(name = "EventID") String eventID, @WebParam(name = "EventType")  EventType eventType) throws EventManagementServiceException;

    @WebMethod
    public String getBookingSchedule(@WebParam(name = "CustomerID") String customerID) throws EventManagementServiceException;

    @WebMethod
    public String cancelEvent(@WebParam(name = "CustomerID") String customerID, @WebParam(name = "EventID") String eventID, @WebParam(name = "EventType")  EventType eventType) throws EventManagementServiceException;

    @WebMethod
    public String swapEvent(@WebParam(name = "CustomerID") String customerID, @WebParam(name = "EventID") String eventID, @WebParam(name = "EventType")  String eventType, @WebParam(name = "OldEventID") String oldEventID, @WebParam(name = "OldEventType") String oldEventType) throws EventManagementServiceException;
}
