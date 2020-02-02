package ca.concordia.ginacody.comp6231.client.controller;

import ca.concordia.ginacody.comp6231.client.cache.Session;
import ca.concordia.ginacody.comp6231.client.cache.UserType;
import ca.concordia.ginacody.comp6231.client.rmi.EventManagementServiceFactoryBean;
import ca.concordia.ginacody.comp6231.client.shell.ShellHelper;
import ca.concordia.ginacody.comp6231.enums.EventTimeSlot;
import ca.concordia.ginacody.comp6231.enums.EventType;
import ca.concordia.ginacody.comp6231.services.EventManagementService;
import ca.concordia.ginacody.comp6231.exception.EventManagementServiceException;
import ca.concordia.ginacody.comp6231.vo.EventVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.rmi.RemoteException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

@ShellComponent
public class EventManagerController {

    private static Logger log = LoggerFactory.getLogger(EventManagerController.class);

    private ObjectProvider<EventManagementServiceFactoryBean> eventManagementServiceFactoryBeanProvider;

    @Autowired
    private ShellHelper shellHelper;

    @Autowired
    private Session session;

    @Autowired
    private BeanFactory beanFactory;

    @Autowired
    public void UsingMyPrototype(ObjectProvider<EventManagementServiceFactoryBean> eventManagementServiceFactoryBeanProvider) {
        this.eventManagementServiceFactoryBeanProvider = eventManagementServiceFactoryBeanProvider;
    }

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
            return shellHelper.getErrorMessage("Invalid Username");
        }
        String result = null;
        try {
            session.init(userName);
            EventManagementServiceFactoryBean eventManagementServiceFactoryBean = this.eventManagementServiceFactoryBeanProvider.getObject(session);
            EventManagementService eventManagementService = beanFactory.getBean(EventManagementService.class);
            result = shellHelper.getSuccessMessage(eventManagementService.login(userName));
        } catch (EventManagementServiceException e) {
            return shellHelper.getErrorMessage(e.getMessage());
        } catch (RemoteException e) {
            return shellHelper.getErrorMessage(e.getMessage());
        }
        return result;
    }


    /**
     *
     * @param evertID
     * @param eventType
     * @param capacity
     * @return
     */
    @ShellMethod("Create Event")
    public String createEvent(@ShellOption(value = { "-id" }) String evertID,
                              @ShellOption(value = { "-type" }) String eventType,
                              @ShellOption(value = { "-capacity" }) String capacity
                              ) {
        log.debug("inside createEvent , evertID {}, eventType {}, capacity {}", evertID, eventType, capacity);


        log.debug("checking if there is a logged in user {}", session.isActive());
        if(!session.isActive()) {
            return shellHelper.getErrorMessage("No Logged in user, Please login");
        }

        log.debug("checking if session user {} is a Manager", session.getUserName());
        if(!UserType.EVENT_MANAGER.equals(session.getUserType())) {
            return shellHelper.getErrorMessage(String.format("User %s is not authorized to perform this action", session.getUserName()));
        }

        log.debug("checking session user is authorized to create passed event. username {}, eventid {}", session.getUserName(), evertID);
        if(!session.getLocation().equals(evertID.substring(0,3))) {
            return shellHelper.getErrorMessage(String.format("Invalid evertID, User %s is not allowed to create event %s eventid, location mismatch", session.getUserName(), evertID));
        }

        log.debug("checking evertID to be in valid format {} {}", usernamePattern, evertID);
        Pattern pattern = Pattern.compile(eventIDPattern);
        if(!pattern.matcher(evertID).matches()) {
            return shellHelper.getErrorMessage("Invalid evertID");
        }

        Integer capacityValue = Integer.parseInt(capacity);
        log.debug("checking capacity value {}", capacity);
        if(capacityValue <= 0) {
            return shellHelper.getErrorMessage(String.format("Invalid capacity value %s", capacity));
        }

        log.debug("checking event type {}", eventType);
        if(EventType.get(eventType) == null) {
            return shellHelper.getErrorMessage(String.format("Invalid event type %s", eventType));
        }

        String dateStr = evertID.substring(4);
        log.debug("checking event Date {}", dateStr);
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(eventDatePattern);
            simpleDateFormat.parse(dateStr);
        } catch (ParseException e) {
            return shellHelper.getErrorMessage(String.format("Invalid event Date %s", dateStr));
        }

        char timeSlot = evertID.charAt(3);
        log.debug("checking event Time Slot {}", timeSlot);
        if(EventTimeSlot.get(Character.toString(timeSlot)) == null) {
            return shellHelper.getErrorMessage(String.format("Invalid Event time slot %s", timeSlot));
        }

        String result = null;
        try {
            EventManagementServiceFactoryBean eventManagementServiceFactoryBean = this.eventManagementServiceFactoryBeanProvider.getObject(session);
            EventManagementService eventManagementService = beanFactory.getBean(EventManagementService.class);
            EventVO eventVO = new EventVO(evertID, EventType.get(eventType), capacityValue);
            result = shellHelper.getSuccessMessage(eventManagementService.addEvent(evertID, EventType.get(eventType), capacityValue));
        } catch (EventManagementServiceException e) {
            return shellHelper.getErrorMessage(e.getMessage());
        } catch (RemoteException e) {
            return shellHelper.getErrorMessage(e.getMessage());
        }
        return result;
    }

    @ShellMethod("Remove Event")
    public String removeEvent(@ShellOption(value = { "-id" }) String evertID,
                              @ShellOption(value = { "-type" }) String eventType) {

        log.debug("inside removeEvent , evertID {}, eventType {}", evertID, eventType);

        log.debug("checking if there is a logged in user {}", session.isActive());
        if(!session.isActive()) {
            return shellHelper.getErrorMessage("No Logged in user, Please login");
        }

        log.debug("checking if session user {} is a Manager", session.getUserName());
        if(!UserType.EVENT_MANAGER.equals(session.getUserType())) {
            return shellHelper.getErrorMessage(String.format("User %s is not authorized to perform this action", session.getUserName()));
        }

        log.debug("checking evertID to be in valid format {} {}", usernamePattern, evertID);
        Pattern pattern = Pattern.compile(eventIDPattern);
        if(!pattern.matcher(evertID).matches()) {
            return shellHelper.getErrorMessage("Invalid evertID");
        }

        log.debug("checking event type {}", eventType);
        if(EventType.get(eventType) == null) {
            return shellHelper.getErrorMessage(String.format("Invalid event type %s", eventType));
        }

        String dateStr = evertID.substring(4);
        log.debug("checking event Date {}", dateStr);
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(eventDatePattern);
            simpleDateFormat.parse(dateStr);
        } catch (ParseException e) {
            return shellHelper.getErrorMessage(String.format("Invalid event Date %s", dateStr));
        }

        char timeSlot = evertID.charAt(3);
        log.debug("checking event Time Slot {}", timeSlot);
        if(EventTimeSlot.get(Character.toString(timeSlot)) == null) {
            return shellHelper.getErrorMessage(String.format("Invalid Event time slot %s", timeSlot));
        }

        String result = null;
        try {
            EventManagementServiceFactoryBean eventManagementServiceFactoryBean = this.eventManagementServiceFactoryBeanProvider.getObject(session);
            EventManagementService eventManagementService = beanFactory.getBean(EventManagementService.class);
            result = shellHelper.getSuccessMessage(eventManagementService.removeEvent(evertID, EventType.get(eventType)));
        } catch (EventManagementServiceException e) {
            return shellHelper.getErrorMessage(e.getMessage());
        } catch (RemoteException e) {
            return shellHelper.getErrorMessage(e.getMessage());
        }
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
            return shellHelper.getErrorMessage(String.format("User %s is not authorized to perform this action", session.getUserName()));
        }

        String result = null;
        try {
            EventManagementServiceFactoryBean eventManagementServiceFactoryBean = this.eventManagementServiceFactoryBeanProvider.getObject(session);
            EventManagementService eventManagementService = beanFactory.getBean(EventManagementService.class);
            result = shellHelper.getSuccessMessage(eventManagementService.listEventAvailability(EventType.get(eventType)));
        } catch (EventManagementServiceException e) {
            return shellHelper.getErrorMessage(e.getMessage());
        } catch (RemoteException e) {
            return shellHelper.getErrorMessage(e.getMessage());
        }
        return result;
    }
}