package ca.concordia.ginacody.comp6231;

import ca.concordia.ginacody.comp6231.services.EventManagementServiceRMIImpl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIBasicServer {

    public static void main(String args[]){
        InputStreamReader is = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(is);
        String portNum, registryURL, serverLocation;
        try{
            System.out.println("Enter the Server Location:");
            serverLocation = (br.readLine()).trim();
            System.out.println("Enter the RMI Registry port number:");
            portNum = (br.readLine()).trim();
            int RMIPortNum = Integer.parseInt(portNum);
            startRegistry(RMIPortNum);
            EventManagementServiceRMIImpl exportedObj = new EventManagementServiceRMIImpl();
            registryURL = "rmi://localhost:" + portNum + "/EventManagementService" + serverLocation;
            Naming.rebind(registryURL, exportedObj);
            System.out.println("Server registered.  Registry currently contains:");
            listRegistry(registryURL);
            System.out.println("RMIBasicServer ready.....");
        }
        catch (Exception re) {
            System.out.println("Exception in RMIBasicServer.main: " + re);
        }
    }

    private static void startRegistry(int RMIPortNum)
            throws RemoteException{
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", RMIPortNum);
            registry.list( );
        }
        catch (RemoteException e) {
        System.out.println("RMI registry cannot be located at port " + RMIPortNum);
            Registry registry = LocateRegistry.createRegistry(RMIPortNum);
            System.out.println("RMI registry created at port " + RMIPortNum);
        }
    }

    private static void listRegistry(String registryURL)
            throws RemoteException, MalformedURLException {
        System.out.println("Registry " + registryURL + " contains: ");
        String [ ] names = Naming.list(registryURL);
        for (int i=0; i < names.length; i++)
            System.out.println(names[i]);
    }
}
