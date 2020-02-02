package ca.concordia.ginacody.comp6231;

import ca.concordia.ginacody.comp6231.config.Configuration;
import ca.concordia.ginacody.comp6231.services.EventManagementServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 *
 */
public class RMIInitializer implements Runnable {

    /**
     *
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(RMIInitializer.class);

    /**
     *
     */
    @Override
    public void run() {
        try{
            startRegistry();
            EventManagementServiceImpl exportedObj = new EventManagementServiceImpl();
            String registryURL = "rmi://"+ Configuration.RMI_REGISTRY_HOST+":" + Configuration.RMI_PORT + "/EventManagementService" + Configuration.SERVER_LOCATION;
            Naming.rebind(registryURL, exportedObj);
            LOGGER.info("RMI Server started on host {} port {}", Configuration.RMI_REGISTRY_HOST, Configuration.RMI_PORT);
        }
        catch (Exception re) {
            LOGGER.error("Exception in RMIBasicServer.main: " + re.getMessage());
        }
    }

    /**
     *
     * @throws RemoteException
     */
    private static void startRegistry() throws RemoteException{
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", Configuration.RMI_PORT);
            registry.list();
        }
        catch (RemoteException e) {
            LOGGER.error("RMI registry cannot be located at port {}", Configuration.RMI_PORT);
            Registry registry = LocateRegistry.createRegistry(Configuration.RMI_PORT);
            LOGGER.info("RMI registry created at port {}", Configuration.RMI_PORT);
        }
    }
}
