package ca.concordia.ginacody.comp6231.processors;

import ca.concordia.ginacody.comp6231.services.EventManagementServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class RequestProcessor extends Thread {

    /**
     *
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestProcessor.class);

    /**
     *
     */
    private String requestMessage;

    /**
     *
     */
    private String replyMessage;

    /**
     *
     * @param requestMessage
     */
    public RequestProcessor(String requestMessage){
        this.requestMessage = requestMessage;
    }

    @Override
    public void run() {
        DatagramSocket aSocket = null;
        try {
            aSocket = new DatagramSocket();
            byte[] m = this.requestMessage.getBytes();
            InetAddress aHost = InetAddress.getByName("127.0.0.1");
            int serverPort = 8080;
            DatagramPacket request = new DatagramPacket(m, m.length, aHost, serverPort);
            aSocket.send(request);
            byte[] buffer = new byte[1000];
            DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
            aSocket.receive(reply);
            replyMessage = new String(reply.getData()).substring(0, reply.getData().length);

        } catch (SocketException e) {
            LOGGER.error(e.getMessage());
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        } finally {
            if (aSocket != null)
                aSocket.close();
        }
    }

    public String getReplyMessage() {
        return replyMessage;
    }
}