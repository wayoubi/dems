package ca.concordia.ginacody.comp6231.client.controller;

import ca.concordia.ginacody.comp6231.client.cache.SessionUser;
import ca.concordia.ginacody.comp6231.client.rmi.EventManagementServiceFactoryBean;
import ca.concordia.ginacody.comp6231.client.shell.ShellHelper;
import ca.concordia.ginacody.comp6231.services.EventManagementService;
import ca.concordia.ginacody.comp6231.services.EventManagementServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

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

    @ShellMethod("Login to the system")
    public String login(@ShellOption(value = { "-user" }) String userName) {
        if (log.isDebugEnabled()) {
            log.debug("inside login , username {}", userName);
        }
        try {
            sessionUser.setUserName(userName);
            sessionUser.setLocation(userName);
            EventManagementServiceFactoryBean cabBookingServiceBean = this.eventManagementServiceFactoryBeanProvider.getObject(sessionUser);
            EventManagementService eventManagementService = beanFactory.getBean(EventManagementService.class);
            shellHelper.printSuccess(eventManagementService.login(userName));
        } catch (EventManagementServiceException e) {//
            return shellHelper.getErrorMessage(e.getMessage());
        }
        return shellHelper.getSuccessMessage("User logged in successfully");
    }

}