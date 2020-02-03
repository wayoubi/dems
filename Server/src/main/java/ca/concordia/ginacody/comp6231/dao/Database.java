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
    private Map<String, List<EventVO>> userRecords;

    /**
     *
     */
    private Map<EventVO, List<String>> eventRecords;

    /**
     *
     */
    private Database() {
        this.setEvents(new ConcurrentHashMap<>());
        this.setUserRecords(new ConcurrentHashMap<>());
        this.setEventRecords(new ConcurrentHashMap<>());
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
    Map<String, List<EventVO>> getUserRecords() {
        return userRecords;
    }

    /**
     *
     * @return
     */
    public Map<EventVO, List<String>> getEventRecords() {
        return eventRecords;
    }

    /**
     *
     * @param eventRecords
     */
    public void setEventRecords(Map<EventVO, List<String>> eventRecords) {
        this.eventRecords = eventRecords;
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
    private void setUserRecords(Map<String, List<EventVO>> userRecords) {
        this.userRecords = userRecords;
    }
}
