package ca.concordia.ginacody.comp6231.dao;

import ca.concordia.ginacody.comp6231.enums.EventType;
import ca.concordia.ginacody.comp6231.vo.EventVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Database {

    /**
     *
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(Database.class);

    /**
     *
     */
    private static Database instance;

    /**
     *
     */
    private Map<EventType, Map<String, EventVO>> events;

    /**
     *
     */
    private Map<String, List<String>> userRecords;

    /**
     *
     */
    private Database() {
        this.setEvents(new ConcurrentHashMap<>());
        this.setUserRecords(new ConcurrentHashMap<>());
    }

    /**
     *
     * @return
     */
    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    /**
     *
     * @return
     */
    Map<EventType, Map<String, EventVO>> getEvents() {
        return events;
    }

    /**
     *
     * @return
     */
    Map<String, List<String>> getUserRecords() {
        return userRecords;
    }

    /**
     * 1
     * @param events
     */
    private void setEvents(Map<EventType, Map<String, EventVO>> events) {
        this.events = events;
    }

    /**
     *
     * @param userRecords
     */
    private void setUserRecords(Map<String, List<String>> userRecords) {
        this.userRecords = userRecords;
    }
}
