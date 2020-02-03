package ca.concordia.ginacody.comp6231.client.controller;

import ca.concordia.ginacody.comp6231.client.cache.Session;
import ca.concordia.ginacody.comp6231.client.cache.UserType;
import ca.concordia.ginacody.comp6231.client.rmi.EventManagementServiceFactoryBean;
import ca.concordia.ginacody.comp6231.client.shell.ShellHelper;
import ca.concordia.ginacody.comp6231.enums.EventTimeSlot;
import ca.concordia.ginacody.comp6231.enums.EventType;
import ca.concordia.ginacody.comp6231.exception.EventManagementServiceException;
import ca.concordia.ginacody.comp6231.services.EventManagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
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
public class BookingController {

    /**
     *
     */
    private static Logger log = LoggerFactory.getLogger(BookingController.class);

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

    @Value("${dems.pattern.username}")
    private String usernamePattern;

    @Value("${dems.pattern.eventid}")
    private String eventIDPattern;

    @Value("${dems.pattern.eventdate}")
    private String eventDatePattern;


    @ShellMethod("Book Event")
    public String bookEvent(@ShellOption(value = {"-customerid"}) String customerID,
                            @ShellOption(value = {"-eventid"}) String eventID,
                            @ShellOption(value = {"-type"}) String eventType
    ) {

        log.debug("inside bookEvent, customerID {}, eventID {}, eventType {}", customerID, eventID, eventType);
        log.debug("checking if there is a logged in user {}", session.isActive());
        if (!session.isActive()) {
            return shellHelper.getErrorMessage("No Logged in user, Please login");
        }

        log.debug("checking if session user {} is a Manager or a Customer", session.getUserName());
        if (!UserType.EVENT_MANAGER.equals(session.getUserType()) && !UserType.CUSTOMER.equals(session.getUserType())) {
            String msg = String.format("User %s is not authorized to perform this action", session.getUserName());
            session.getUserActivityLogger().log(String.format("action [bookEvent], param customerID [%s] eventID [%s], eventType [%s], error [%s]", customerID, eventID, eventType, msg));
            return shellHelper.getErrorMessage(msg);
        }

        log.debug("checking customerID to be in valid format {} {}", usernamePattern, customerID);
        Pattern pattern = Pattern.compile(usernamePattern);
        if (!pattern.matcher(customerID).matches()) {
            String msg = "Invalid customerID";
            session.getUserActivityLogger().log(String.format("action [bookEvent], param customerID [%s], eventID [%s], eventType [%s], error [%s]", customerID, eventID, eventType, msg));
            return shellHelper.getErrorMessage(msg);
        }

        log.debug("checking customerID is not a manager {}", customerID);
        if (UserType.EVENT_MANAGER.equals(UserType.get(Character.toString(customerID.charAt(3))))) {
            String msg = "Invalid customerID, Manager cannot book an event";
            session.getUserActivityLogger().log(String.format("action [bookEvent], param customerID [%s], eventID [%s], eventType [%s], error [%s]", customerID, eventID, eventType, msg));
            return shellHelper.getErrorMessage(msg);
        }

        log.debug("checking eventID to be in valid format {} {}", eventIDPattern, eventID);
        pattern = Pattern.compile(eventIDPattern);
        if (!pattern.matcher(eventID).matches()) {
            String msg = "Invalid eventID";
            session.getUserActivityLogger().log(String.format("action [bookEvent], param customerID [%s], eventID [%s], eventType [%s],  error [%s]", customerID, eventID, eventType, msg));
            return shellHelper.getErrorMessage(msg);
        }

        log.debug("checking event type {}", eventType);
        if (EventType.get(eventType) == null) {
            String msg = String.format("Invalid event type %s", eventType);
            session.getUserActivityLogger().log(String.format("action [bookEvent], param customerID [%s], eventID [%s], eventType [%s],  error [%s]", customerID, eventID, eventType, msg));
            return shellHelper.getErrorMessage(msg);
        }

        String dateStr = eventID.substring(4);
        log.debug("checking event Date {}", dateStr);
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(eventDatePattern);
            simpleDateFormat.parse(dateStr);
        } catch (ParseException e) {
            String msg = String.format("Invalid event Date %s", dateStr);
            session.getUserActivityLogger().log(String.format("action [bookEvent], param customerID [%s], eventID [%s], eventType [%s], error [%s]", customerID, eventID, eventType, msg));
            return shellHelper.getErrorMessage(msg);
        }

        char timeSlot = eventID.charAt(3);
        log.debug("checking event Time Slot {}", timeSlot);
        if (EventTimeSlot.get(Character.toString(timeSlot)) == null) {
            String msg = String.format("Invalid Event time slot %s", timeSlot);
            session.getUserActivityLogger().log(String.format("action [bookEvent], param customerID [%s], eventID [%s], eventType [%s], error [%s]", customerID, eventID, eventType, msg));
            return shellHelper.getErrorMessage(msg);
        }

        String result = null;
        try {
            EventManagementServiceFactoryBean eventManagementServiceFactoryBean = this.eventManagementServiceFactoryBeanProvider.getObject(session);
            EventManagementService eventManagementService = beanFactory.getBean(EventManagementService.class);
            result = shellHelper.getSuccessMessage(eventManagementService.bookEvent(customerID, eventID, EventType.get(eventType)));
        } catch (EventManagementServiceException e) {
            session.getUserActivityLogger().log(String.format("action [bookEvent], param customerID [%s], eventID [%s], eventType [%s], error [%s]", customerID, eventID, eventType, e.getMessage()));
            return shellHelper.getErrorMessage(e.getMessage());
        } catch (RemoteException e) {
            session.getUserActivityLogger().log(String.format("action [bookEvent], param customerID [%s], eventID [%s], eventType [%s], error [%s]", customerID, eventID, eventType, e.getMessage()));
            return shellHelper.getErrorMessage(e.getMessage());
        } catch (BeansException e) {
            session.getUserActivityLogger().log(String.format("action [bookEvent], param customerID [%s], eventID [%s], eventType [%s], error [%s]", customerID, eventID, eventType, e.getMessage()));
            return shellHelper.getErrorMessage(e.getMessage());
        }
        session.getUserActivityLogger().log(String.format("action [bookEvent], param customerID [%s], eventID [%s], eventType [%s], result [%s]", customerID, eventID, eventType, result));
        return result;
    }
}
