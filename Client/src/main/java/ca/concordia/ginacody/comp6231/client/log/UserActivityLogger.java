package ca.concordia.ginacody.comp6231.client.log;

import ca.concordia.ginacody.comp6231.client.controller.EventManagerController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 */
public class UserActivityLogger {

    private static Logger LOGGER = LoggerFactory.getLogger(UserActivityLogger.class);

    /**
     *
     */
    private String username;

    /**
     *
     */
    private PrintWriter printWriter;

    /**
     *
     */
    public UserActivityLogger(String username) {
        this.setUsername(username);
        try{
            FileWriter fileWriter = new FileWriter(String.format("log/%s.log", username), true);
             printWriter = new PrintWriter(new BufferedWriter(fileWriter),true);
        } catch (IOException ex) {
            LOGGER.error(ex.getMessage());
        }
    }

    /**
     *
     * @param message
     */
    public void log(String message) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        this.printWriter.printf(String.format("%s %s %s%s", dateFormat.format(new Date()), Thread.currentThread().getName(), message, System.lineSeparator()));
    }

    /**
     *
     */
    public void release() {
        this.printWriter.close();
    }

    /**
     *
     * @return
     */
    public String getUsername() {
        return username;
    }

    /**
     * s
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }
}
