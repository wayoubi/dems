package ca.concordia.ginacody.comp6231.client.rmi;

import ca.concordia.ginacody.comp6231.client.cache.SessionUser;
import ca.concordia.ginacody.comp6231.services.CabBookingService;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;

public class CabBookingServiceFactoryBean extends RmiProxyFactoryBean {

    public CabBookingServiceFactoryBean(SessionUser sessionUser) {
        this.setServiceUrl("rmi://localhost:1099/CabBookingService"+sessionUser.getLocation());
        this.setServiceInterface(CabBookingService.class);
    }

}
