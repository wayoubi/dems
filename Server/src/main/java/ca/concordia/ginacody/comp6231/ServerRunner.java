package ca.concordia.ginacody.comp6231;

import ca.concordia.ginacody.comp6231.config.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

/**
 *
 */
public class ServerRunner {

    /**
     *
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerRunner.class);

    /**
     *
     * @param args
     */
    public static void main(String[] args) {

        LOGGER.debug("checking passed parameters, count {} ", args.length);
        if(args.length != 5 ) {
            LOGGER.error("Please add required arguments, Sever Location, RMI Port, UDP Port, ORB Port, ORB Host");
            return;
        }

        LOGGER.debug("checking passed Server Location to be in valid format {} {}", Configuration.SERVER_LOCATION_PATTERN, args[0]);
        Pattern pattern = Pattern.compile(Configuration.SERVER_LOCATION_PATTERN);
        if(!pattern.matcher(args[0]).matches()) {
            LOGGER.error("Passed Server Location is invalid {}", args[0]);
            return;
        } else {
            Configuration.SERVER_LOCATION = args[0].trim();
        }

        LOGGER.debug("checking passed RMI Server port to be in valid {}", args[1]);
        try {
            Configuration.RMI_PORT = Integer.parseInt(args[1].trim());
        } catch(NumberFormatException nfex) {
            LOGGER.error("Passed RMI Server port is invalid {}, default will be used {}", args[1], Configuration.RMI_PORT);
        }

        LOGGER.debug("checking passed UDP Server port to be in valid {}", args[2]);
        try {
            Configuration.UDP_PORT = Integer.parseInt(args[2]);
        } catch(NumberFormatException nfex) {
            LOGGER.error("Passed RMI Server port is invalid {}, default will be used {}", args[2].trim(), Configuration.UDP_PORT);
        }

        LOGGER.debug("checking passed ORB Server port to be in valid {}", args[3]);
        try {
            Configuration.ORB_PORT = Integer.parseInt(args[3]);
        } catch(NumberFormatException nfex) {
            LOGGER.error("Passed ORB port is invalid {}, default will be used {}", args[2].trim(), Configuration.ORB_PORT);
        }

        LOGGER.debug("checking passed ORB Host to be in valid {}", args[4]);
        Configuration.ORB_HOST = args[4];


        LOGGER.info("Starting RMI Server .....");
        Thread rmiInitializer = new Thread(new RMIInitializer());
        rmiInitializer.setName("RMIInitializer");
        rmiInitializer.start();

        LOGGER.info("Starting UDP Server .....");
        Thread udpServerThread = new Thread(new UDPServer());
        udpServerThread.setName("UDP Server Thread");
        udpServerThread.start();

        LOGGER.info("Starting CORBA Server .....");
        Thread corbaServerThread = new Thread(new CorbaServer());
        corbaServerThread.setName("CORBA Server Thread");
        corbaServerThread.start();
    }

}
