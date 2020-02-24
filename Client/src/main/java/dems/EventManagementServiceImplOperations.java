package dems;


/**
* dems/EventManagementServiceImplOperations.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from EventManagementService.idl
* Sunday, February 23, 2020 8:25:50 o'clock PM EST
*/

public interface EventManagementServiceImplOperations 
{
  String login(String userName);
  String addEvent(String eventID, String eventType, int bookingCapacity);
  String removeEvent(String eventID, String eventType);
  String listEventAvailability(String eventType);
  String bookEvent(String customerID, String eventID, String eventType);
  String getBookingSchedule(String customerID);
  String cancelEvent(String customerID, String eventID, String eventType);
  void shutdown();
} // interface EventManagementServiceImplOperations