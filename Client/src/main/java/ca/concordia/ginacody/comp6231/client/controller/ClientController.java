package ca.concordia.ginacody.comp6231.client.controller;

import ca.concordia.ginacody.comp6231.client.cache.SessionUser;
import ca.concordia.ginacody.comp6231.client.rmi.CabBookingServiceFactoryBean;
import ca.concordia.ginacody.comp6231.client.shell.ShellHelper;
import ca.concordia.ginacody.comp6231.services.BookingException;
import ca.concordia.ginacody.comp6231.services.CabBookingService;
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

    private ObjectProvider<CabBookingServiceFactoryBean> cabBookingServiceFactoryBeanProvider;

    @Autowired
    private ShellHelper shellHelper;

    @Autowired
    private SessionUser sessionUser;

    @Autowired
    private BeanFactory beanFactory;

    @Autowired
    public void UsingMyPrototype(ObjectProvider<CabBookingServiceFactoryBean> cabBookingServiceFactoryBeanProvider) {
        this.cabBookingServiceFactoryBeanProvider = cabBookingServiceFactoryBeanProvider;
    }

    @ShellMethod("Login to the system")
    public String login(@ShellOption(value = { "-user" }) String userName) {
        if (log.isDebugEnabled()) {
            log.debug("inside login , username {}", userName);
        }
        try {
            sessionUser.setUserName(userName);
            sessionUser.setLocation(userName);
            CabBookingServiceFactoryBean cabBookingServiceBean = this.cabBookingServiceFactoryBeanProvider.getObject(sessionUser);
            CabBookingService cabBookingService = beanFactory.getBean(CabBookingService.class);
            shellHelper.printSuccess(cabBookingService.bookRide("13 Seagate Blvd, Key Largo, FL 33037").toString());
        } catch (BookingException e) {//
            e.printStackTrace();
        }
        return shellHelper.getSuccessMessage("User logged in successfully");
    }

}