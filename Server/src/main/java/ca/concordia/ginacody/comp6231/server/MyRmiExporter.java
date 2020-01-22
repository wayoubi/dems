package ca.concordia.ginacody.comp6231.server;


import org.springframework.remoting.rmi.RmiServiceExporter;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;

public class MyRmiExporter extends RmiServiceExporter {

    protected Registry getRegistry(String registryHost, int registryPort,
                                   RMIClientSocketFactory clientSocketFactory, RMIServerSocketFactory serverSocketFactory)
            throws RemoteException {

        if (registryHost != null) {
            // Host explictly specified: only lookup possible.
            if (logger.isInfoEnabled()) {
                logger.info("Looking for RMI registry at port '" + registryPort + "' of host [" + registryHost + "]");
            }
            try {
                Registry reg = LocateRegistry.getRegistry(registryHost, registryPort, clientSocketFactory);
                testRegistry(reg);
                return reg;
            } catch (RemoteException ex) {
                logger.debug("RMI registry access threw exception", ex);
                logger.warn("Could not detect RMI registry - creating new one");
                // Assume no registry found -> create new one.
                LocateRegistry.createRegistry(registryPort);
                Registry reg = LocateRegistry.getRegistry(registryHost, registryPort, clientSocketFactory);
                testRegistry(reg);
                return reg;
            }
        } else {
            return getRegistry(registryPort, clientSocketFactory, serverSocketFactory);
        }
    }
}