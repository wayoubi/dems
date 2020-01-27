package ca.concordia.ginacody.comp6231.client.controller;

import ca.concordia.ginacody.comp6231.client.cache.SessionUser;
import ca.concordia.ginacody.comp6231.client.rmi.EventManagementServiceFactoryBean;
import ca.concordia.ginacody.comp6231.client.shell.ShellHelper;
import ca.concordia.ginacody.comp6231.services.EventManagementService;
import ca.concordia.ginacody.comp6231.exception.EventManagementServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.regex.Pattern;

@ShellComponent
public class ClientController {

    private static Logger log = LoggerFactory.getLogger(ClientController.class);

    private ObjectProvider<EventManagementServiceFactoryBean> eventManagementServiceFactoryBeanProvider;

    @Autowired
    private ShellHelper shellHelper;

    @Autowired
    private SessionUser sessionUser;

    @Autowired
    private BeanFactory beanFactory;

    @Autowired
    public void UsingMyPrototype(ObjectProvider<EventManagementServiceFactoryBean> eventManagementServiceFactoryBeanProvider) {
        this.eventManagementServiceFactoryBeanProvider = eventManagementServiceFactoryBeanProvider;
    }

    @Value( "${dems.pattern.username}" )
    private String usernamePattern;

    @ShellMethod("Login to the system")
    public String login(@ShellOption(value = { "-user" }) String userName) {

        if (log.isDebugEnabled()) {
            log.debug("inside login , username {}", userName);
        }

        log.debug("checking username to be in valid format {} {}", usernamePattern, userName);
        Pattern pattern = Pattern.compile(usernamePattern);
        if(!pattern.matcher(userName).matches()) {
            return shellHelper.getErrorMessage("Invalid Username");
        }

        try {
            sessionUser.setUserName(userName);
            EventManagementServiceFactoryBean cabBookingServiceBean = this.eventManagementServiceFactoryBeanProvider.getObject(sessionUser);
            EventManagementService eventManagementService = beanFactory.getBean(EventManagementService.class);
            shellHelper.printSuccess(eventManagementService.login(userName));
        } catch (EventManagementServiceException e) {
            return shellHelper.getErrorMessage(e.getMessage());
        }

        return shellHelper.getSuccessMessage("User logged in successfully");
    }

}