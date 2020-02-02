package ca.concordia.ginacody.comp6231.processors;

import ca.concordia.ginacody.comp6231.UDPServer;
import ca.concordia.ginacody.comp6231.config.Configuration;
import ca.concordia.ginacody.comp6231.enums.EventType;
import ca.concordia.ginacody.comp6231.exception.EventManagementServiceException;
import ca.concordia.ginacody.comp6231.facade.EventManagementBusinessFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Optional;
import java.util.StringTokenizer;

/**
 *
 */
public class ResponseProcessor extends Thread {

    /**
     *
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ResponseProcessor.class);

    /**
     *
     */
    private DatagramPacket request;

    /**
     *
     */
    private DatagramSocket socket;



    /**
     *
     * @param request
     */
    public ResponseProcessor(DatagramSocket aSocket, DatagramPacket request) {
        this.setRequest(request);
        this.setSocket(aSocket);
    }

    @Override
    public void run()  {
        String  commandString = new String(this.getRequest().getData()).substring(0, this.getRequest().getData().length);
        StringTokenizer stringTokenizer = new StringTokenizer(commandString, ":");
        String remoteServer = stringTokenizer.nextToken();
        String command = stringTokenizer.nextToken();
        String subCommand = stringTokenizer.nextToken();
        LOGGER.info("Processing request sent from {} server  to {} for {}", remoteServer, command, subCommand);

        StringBuilder stringBuilder = new StringBuilder();
        //stringBuilder.append(String.format("Response from Server %s", Configuration.SERVER_LOCATION));
        stringBuilder.append(System.lineSeparator());

        if("listEventAvailability".equals(command)) {
            Optional<EventType> optional = Optional.ofNullable(EventType.get(subCommand));
            if(optional.isPresent()) {
                EventManagementBusinessFacade eventManagementBusinessFacade = new EventManagementBusinessFacade();
                try {
                    stringBuilder.append(eventManagementBusinessFacade.listEventAvailability(optional.get()));
                } catch(EventManagementServiceException e) {
                    stringBuilder.append(String.format("%s from remote server %s%s", e.getMessage(), Configuration.SERVER_LOCATION, System.lineSeparator()));
                }
            }
        } else {
            LOGGER.warn("Unsupported request {} {}", this.getRequest().getAddress(), this.getRequest().getPort(), command, subCommand);
            stringBuilder.append(String.format("Unsupported Operation %s", command)) ;
        }
        try {
            DatagramPacket reply = new DatagramPacket(stringBuilder.toString().getBytes(), stringBuilder.toString().getBytes().length, request.getAddress(), request.getPort());
            LOGGER.info("sending response back to remote server {} to {} {} ", remoteServer, command, subCommand);
            this.getSocket().send(reply);
        } catch (IOException ioex) {
            LOGGER.error("{} caused by {}", ioex.getMessage(), ioex.getCause().getMessage());
        }
    }


    /**
     *
     * @return
     */
    public DatagramPacket getRequest() {
        return request;
    }

    /**
     *
     * @param request
     */
    public void setRequest(DatagramPacket request) {
        this.request = request;
    }

    /**
     *
     * @return
     */
    public DatagramSocket getSocket() {
        return socket;
    }

    /**
     *
     * @param socket
     */
    public void setSocket(DatagramSocket socket) {
        this.socket = socket;
    }
}
