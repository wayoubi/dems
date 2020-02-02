package ca.concordia.ginacody.comp6231.client.rmi;

import ca.concordia.ginacody.comp6231.client.cache.Session;
import ca.concordia.ginacody.comp6231.services.EventManagementService;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;

public class EventManagementServiceFactoryBean extends RmiProxyFactoryBean {

    public EventManagementServiceFactoryBean(Session session) {
        //this.setServiceUrl("rmi://localhost:1099/EventManagementService"+sessionUser.getLocation());
        this.setServiceUrl(session.getServiceURL()+session.getLocation());
        this.setServiceInterface(EventManagementService.class);
    }
}
