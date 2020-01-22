package ca.concordia.ginacody.comp6231.client.controller;

import ca.concordia.ginacody.comp6231.client.shell.ShellHelper;
import ca.concordia.ginacody.comp6231.services.BookingException;
import ca.concordia.ginacody.comp6231.services.CabBookingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class ClientController {

    /**
     * Class Logger
     */
    private static Logger log = LoggerFactory.getLogger(ClientController.class);

    /**
     * shellHelper bean
     */
    @Autowired
    ShellHelper shellHelper;

    @Autowired
    CabBookingService cabBookingService;

    @ShellMethod("Login to the system")
    public String login() {

        if (log.isDebugEnabled()) {
            log.debug("inside login");
        }
        try {
            shellHelper.printSuccess(cabBookingService.bookRide("13 Seagate Blvd, Key Largo, FL 33037").toString());
        } catch (BookingException e) {
            e.printStackTrace();
        }
        return shellHelper.getSuccessMessage("User logged in successfully");
    }

}