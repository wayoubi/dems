package ca.concordia.ginacody.comp6231.client.controller;

import ca.concordia.ginacody.comp6231.client.cache.Session;
import ca.concordia.ginacody.comp6231.client.cache.UserType;
import ca.concordia.ginacody.comp6231.client.shell.ShellHelper;
import ca.concordia.ginacody.comp6231.enums.EventTimeSlot;
import ca.concordia.ginacody.comp6231.enums.EventType;
import ca.concordia.ginacody.comp6231.exception.EventManagementServiceException;
import ca.concordia.ginacody.comp6231.services.ws.EventManagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

/**
 *
 */
@ShellComponent
public class EventManagerController {

    /**
     *
     */
    private static Logger log = LoggerFactory.getLogger(EventManagerController.class);

    @Autowired
    private ShellHelper shellHelper;

    @Autowired
    private Session session;

    @Value( "${dems.pattern.username}" )
    private String usernamePattern;

    @Value( "${dems.pattern.eventid}" )
    private String eventIDPattern;

    @Value( "${dems.pattern.eventdate}" )
    private String eventDatePattern;

    /**
     *
     * @param userName
     * @return
     */
    @ShellMethod("Login to the system")
    public String login(@ShellOption(value = { "-user" }) String userName) {
        log.debug("inside login , username {}", userName);
        log.debug("checking username to be in valid format {} {}", usernamePattern, userName);
        Pattern pattern = Pattern.compile(usernamePattern);
        if(!pattern.matcher(userName).matches()) {
            String message  = "Invalid Username";
            session.getUserActivityLogger().log(String.format("action [login], param userName [%s], result [%s]", userName, message));
            return shellHelper.getErrorMessage(message);
        }
        String result = null;
        try {
            session.init(userName);
            URL addURL = null;
            if("MTL".equals(session.getLocation())) {
                addURL = new URL(String.format("http://localhost:%s/demsservice?wsdl","9090"));
            } else if("SHE".equals(session.getLocation())) {
                addURL = new URL(String.format("http://localhost:%s/demsservice?wsdl","9091"));
            } else if("QUE".equals(session.getLocation())) {
                addURL = new URL(String.format("http://localhost:%s/demsservice?wsdl","9092"));
            }
            QName addQName = new QName("http://ws.services.comp6231.ginacody.concordia.ca/", "EventManagementServiceImplService");
            Service service = Service.create(addURL, addQName);
            EventManagementService eventManagementService = service.getPort(EventManagementService.class);
            result = shellHelper.getSuccessMessage(eventManagementService.login(userName));

        } catch (EventManagementServiceException e) {
            session.getUserActivityLogger().log(String.format("action [login], param userName [%s], error [%s]", userName, e.getMessage()));
            return shellHelper.getErrorMessage(e.getMessage());
        } catch (BeansException e) {
            session.getUserActivityLogger().log(String.format("action [login], param userName [%s], error [%s]", userName, e.getMessage()));
            return shellHelper.getErrorMessage(e.getMessage());
        } catch (Exception e) {
            session.getUserActivityLogger().log(String.format("action [login], param userName [%s], error [%s]", userName, e.getMessage()));
            return shellHelper.getErrorMessage(e.getMessage());
        }
        session.getUserActivityLogger().log(String.format("action [login], param userName [%s], result [%s]", userName, result));
        return result;
    }


    /**xz
     *
     * @param eventID
     * @param eventType
     * @param capacity
     * @return
     */
    @ShellMethod("Create Event")
    public String createEvent(@ShellOption(value = { "-id" }) String eventID,
                              @ShellOption(value = { "-type" }) String eventType,
                              @ShellOption(value = { "-capacity" }) String capacity
                              ) {
        log.debug("inside createEvent , eventID {}, eventType {}, capacity {}", eventID, eventType, capacity);
        log.debug("checking if there is a logged in user {}", session.isActive());
        if(!session.isActive()) {
            return shellHelper.getErrorMessage("No Logged in user, Please login");
        }

        log.debug("checking if session user {} is a Manager", session.getUserName());
        if(!UserType.EVENT_MANAGER.equals(session.getUserType())) {
            String msg = String.format("User %s is not authorized to perform this action", session.getUserName());
            session.getUserActivityLogger().log(String.format("action [createEvent], param eventID [%s], eventType [%s], capacity [%s] error [%s]",eventID, eventType, capacity, msg));
            return shellHelper.getErrorMessage(msg);
        }

        log.debug("checking session user is authorized to create passed event. username {}, eventid {}", session.getUserName(), eventID);
        if(!session.getLocation().equals(eventID.substring(0,3))) {
            String msg =String.format("Invalid eventID, User %s is not allowed to create event %s eventid, location mismatch", session.getUserName(), eventID);
            session.getUserActivityLogger().log(String.format("action [createEvent], param eventID [%s], eventType [%s], capacity [%s] error [%s]",eventID, eventType, capacity, msg));
            return shellHelper.getErrorMessage(msg);
        }

        log.debug("checking eventID to be in valid format {} {}", eventIDPattern, eventID);
        Pattern pattern = Pattern.compile(eventIDPattern);
        if(!pattern.matcher(eventID).matches()) {
            String msg = "Invalid eventID";
            session.getUserActivityLogger().log(String.format("action [createEvent], param eventID [%s], eventType [%s], capacity [%s] error [%s]",eventID, eventType, capacity, msg));
            return shellHelper.getErrorMessage(msg);
        }

        Integer capacityValue = Integer.parseInt(capacity);
        log.debug("checking capacity value {}", capacity);
        if(capacityValue <= 0) {
            String msg =String.format("Invalid capacity value %s", capacity);
            session.getUserActivityLogger().log(String.format("action [createEvent], param eventID [%s], eventType [%s], capacity [%s] error [%s]",eventID, eventType, capacity, msg));
            return shellHelper.getErrorMessage(msg);
        }

        log.debug("checking event type {}", eventType);
        if(EventType.get(eventType) == null) {
            String msg =String.format("Invalid event type %s", eventType);
            session.getUserActivityLogger().log(String.format("action [createEvent], param eventID [%s], eventType [%s], capacity [%s] error [%s]",eventID, eventType, capacity, msg));
            return shellHelper.getErrorMessage(msg);
        }

        String dateStr = eventID.substring(4);
        log.debug("checking event Date {}", dateStr);
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(eventDatePattern);
            simpleDateFormat.parse(dateStr);
        } catch (ParseException e) {
            String msg =String.format("Invalid event Date %s", dateStr);
            session.getUserActivityLogger().log(String.format("action [createEvent], param eventID [%s], eventType [%s], capacity [%s] error [%s]",eventID, eventType, capacity, msg));
            return shellHelper.getErrorMessage(msg);
        }

        char timeSlot = eventID.charAt(3);
        log.debug("checking event Time Slot {}", timeSlot);
        if(EventTimeSlot.get(Character.toString(timeSlot)) == null) {
            String msg =String.format("Invalid Event time slot %s", timeSlot);
            session.getUserActivityLogger().log(String.format("action [createEvent], param eventID [%s], eventType [%s], capacity [%s] error [%s]",eventID, eventType, capacity, msg));
            return shellHelper.getErrorMessage(msg);
        }

        String result = null;
        try {
            URL addURL = null;
            if("MTL".equals(session.getLocation())) {
                addURL = new URL(String.format("http://localhost:%s/demsservice?wsdl","9090"));
            } else if("SHE".equals(session.getLocation())) {
                addURL = new URL(String.format("http://localhost:%s/demsservice?wsdl","9091"));
            } else if("QUE".equals(session.getLocation())) {
                addURL = new URL(String.format("http://localhost:%s/demsservice?wsdl","9092"));
            }
            QName addQName = new QName("http://ws.services.comp6231.ginacody.concordia.ca/", "EventManagementServiceImplService");
            Service service = Service.create(addURL, addQName);
            EventManagementService eventManagementService = service.getPort(EventManagementService.class);
            result = shellHelper.getSuccessMessage(eventManagementService.addEvent(eventID, EventType.get(eventType), capacityValue));
        } catch (EventManagementServiceException e) {
            session.getUserActivityLogger().log(String.format("action [createEvent], param eventID [%s], eventType [%s], capacity [%s] error [%s]", eventID, eventType, capacity, e.getMessage()));
            return shellHelper.getErrorMessage(e.getMessage());
        } catch (BeansException e) {
            session.getUserActivityLogger().log(String.format("action [createEvent], param eventID [%s], eventType [%s], capacity [%s] error [%s]", eventID, eventType, capacity, e.getMessage()));
            return shellHelper.getErrorMessage(e.getMessage());
        } catch (Exception e) {
            session.getUserActivityLogger().log(String.format("action [createEvent], param eventID [%s], eventType [%s], capacity [%s] error [%s]", eventID, eventType, capacity, e.getMessage()));
            return shellHelper.getErrorMessage(e.getMessage());
        }
        session.getUserActivityLogger().log(String.format("action [createEvent], eventID [%s], eventType [%s], capacity [%s], result [%s]", eventID, eventType, capacity, result));
        return result;
    }

    /**
     *
     * @param eventID
     * @param eventType
     * @return
     */
    @ShellMethod("Remove Event")
    public String removeEvent(@ShellOption(value = { "-id" }) String eventID,
                              @ShellOption(value = { "-type" }) String eventType) {
        log.debug("inside removeEvent , eventID {}, eventType {}", eventID, eventType);
        log.debug("checking if there is a logged in user {}", session.isActive());
        if(!session.isActive()) {
            return shellHelper.getErrorMessage("No Logged in user, Please login");
        }

        log.debug("checking if session user {} is a Manager", session.getUserName());
        if(!UserType.EVENT_MANAGER.equals(session.getUserType())) {
            String msg = String.format("User %s is not authorized to perform this action", session.getUserName());
            session.getUserActivityLogger().log(String.format("action [removeEvent], param eventID [%s], eventType [%s] error [%s]",eventID, eventType, msg));
            return shellHelper.getErrorMessage(msg);
        }

        log.debug("checking if removed event is local eventID {} current location {}", eventID, session.getLocation());
        if(!eventID.startsWith(session.getLocation())) {
            String msg = String.format("User %s is not authorized to remove remote events eventID %s current location %s", session.getUserName(), eventID, session.getLocation());
            session.getUserActivityLogger().log(String.format("User [%s] is not authorized to remove remote events eventID [%s] current location [%s]",eventID, eventType, session.getLocation()));
            return shellHelper.getErrorMessage(msg);
        }

        log.debug("checking eventID to be in valid format {} {}", usernamePattern, eventID);
        Pattern pattern = Pattern.compile(eventIDPattern);
        if(!pattern.matcher(eventID).matches()) {
            String msg = "Invalid eventID";
            session.getUserActivityLogger().log(String.format("action [removeEvent], param eventID [%s], eventType [%s], error [%s]",eventID, eventType, msg));
            return shellHelper.getErrorMessage(msg);
        }

        log.debug("checking event type {}", eventType);
        if(EventType.get(eventType) == null) {
            String msg = String.format("Invalid event type %s", eventType);
            session.getUserActivityLogger().log(String.format("action [removeEvent], param eventID [%s], eventType [%s], error [%s]",eventID, eventType, msg));
            return shellHelper.getErrorMessage(msg);
        }

        String dateStr = eventID.substring(4);
        log.debug("checking event Date {}", dateStr);
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(eventDatePattern);
            simpleDateFormat.parse(dateStr);
        } catch (ParseException e) {
            String msg = String.format("Invalid event Date %s", dateStr);
            session.getUserActivityLogger().log(String.format("action [removeEvent], param eventID [%s], eventType [%s], error [%s]",eventID, eventType, msg));
            return shellHelper.getErrorMessage(msg);
        }

        char timeSlot = eventID.charAt(3);
        log.debug("checking event Time Slot {}", timeSlot);
        if(EventTimeSlot.get(Character.toString(timeSlot)) == null) {
            String msg = String.format("Invalid Event time slot %s", timeSlot);
            session.getUserActivityLogger().log(String.format("action [removeEvent], param eventID [%s], eventType [%s], error [%s]",eventID, eventType, msg));
            return shellHelper.getErrorMessage(msg);
        }

        String result = null;
        try {
            URL addURL = null;
            if("MTL".equals(session.getLocation())) {
                addURL = new URL(String.format("http://localhost:%s/demsservice?wsdl","9090"));
            } else if("SHE".equals(session.getLocation())) {
                addURL = new URL(String.format("http://localhost:%s/demsservice?wsdl","9091"));
            } else if("QUE".equals(session.getLocation())) {
                addURL = new URL(String.format("http://localhost:%s/demsservice?wsdl","9092"));
            }
            QName addQName = new QName("http://ws.services.comp6231.ginacody.concordia.ca/", "EventManagementServiceImplService");
            Service service = Service.create(addURL, addQName);
            EventManagementService eventManagementService = service.getPort(EventManagementService.class);
            result = shellHelper.getSuccessMessage(eventManagementService.removeEvent(eventID, EventType.get(eventType)));

        } catch (EventManagementServiceException e) {
            session.getUserActivityLogger().log(String.format("action [removeEvent], param eventID [%s], eventType [%s], error [%s]", eventID, eventType, e.getMessage()));
            return shellHelper.getErrorMessage(e.getMessage());
        } catch (BeansException e) {
            session.getUserActivityLogger().log(String.format("action [removeEvent], param eventID [%s], eventType [%s], error [%s]", eventID, eventType, e.getMessage()));
            return shellHelper.getErrorMessage(e.getMessage());
        } catch (Exception e) {
            session.getUserActivityLogger().log(String.format("action [removeEvent], param eventID [%s], eventType [%s], error [%s]", eventID, eventType, e.getMessage()));
            return shellHelper.getErrorMessage(e.getMessage());
        }
        session.getUserActivityLogger().log(String.format("action [removeEvent], eventID [%s], eventType [%s], result [%s]", eventID, eventType, result));
        return result;

    }


    /**
     *
     * @param eventType
     * @return
     */
    @ShellMethod("List Event Availability")
    public String listEventAvailability(@ShellOption(value = { "-type" }) String eventType) {
        log.debug("inside listEventAvailability eventType {}", eventType);
        log.debug("checking if there is a logged in user {}", session.isActive());
        if(!session.isActive()) {
            return shellHelper.getErrorMessage("No Logged in user, Please login");
        }

        log.debug("checking if session user {} is a Manager", session.getUserName());
        if(!UserType.EVENT_MANAGER.equals(session.getUserType())) {
            String msg = String.format("User %s is not authorized to perform this action", session.getUserName());
            session.getUserActivityLogger().log(String.format("action [listEventAvailability], param eventType [%s], error [%s]", eventType, msg));
            return shellHelper.getErrorMessage(msg);
        }

        log.debug("checking event type {}", eventType);
        if(EventType.get(eventType) == null) {
            String msg = String.format("Invalid event type %s", eventType);
            session.getUserActivityLogger().log(String.format("action [listEventAvailability], param eventType [%s], error [%s]", eventType, msg));
            return shellHelper.getErrorMessage(msg);
        }

        String result = null;
        try {
            URL addURL = null;
            if("MTL".equals(session.getLocation())) {
                addURL = new URL(String.format("http://localhost:%s/demsservice?wsdl","9090"));
            } else if("SHE".equals(session.getLocation())) {
                addURL = new URL(String.format("http://localhost:%s/demsservice?wsdl","9091"));
            } else if("QUE".equals(session.getLocation())) {
                addURL = new URL(String.format("http://localhost:%s/demsservice?wsdl","9092"));
            }
            QName addQName = new QName("http://ws.services.comp6231.ginacody.concordia.ca/", "EventManagementServiceImplService");
            Service service = Service.create(addURL, addQName);
            EventManagementService eventManagementService = service.getPort(EventManagementService.class);
            result = shellHelper.getSuccessMessage(eventManagementService.listEventAvailability(EventType.get(eventType)));
        } catch (EventManagementServiceException e) {
            session.getUserActivityLogger().log(String.format("action [listEventAvailability], param eventType [%s], error [%s]", eventType, e.getMessage()));
            return shellHelper.getErrorMessage(e.getMessage());
        } catch (BeansException e) {
            session.getUserActivityLogger().log(String.format("action [listEventAvailability], param eventType [%s], error [%s]", eventType, e.getMessage()));
            return shellHelper.getErrorMessage(e.getMessage());
        } catch (Exception e) {
            session.getUserActivityLogger().log(String.format("action [listEventAvailability], param eventType [%s], error [%s]", eventType, e.getMessage()));
            return shellHelper.getErrorMessage(e.getMessage());
        }
        session.getUserActivityLogger().log(String.format("action [listEventAvailability], eventType [%s], result [%s]", eventType, result));
        return result;
    }
}