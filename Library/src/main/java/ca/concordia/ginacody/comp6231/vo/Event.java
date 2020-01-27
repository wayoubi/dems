package ca.concordia.ginacody.comp6231.vo;

import ca.concordia.ginacody.comp6231.enums.EventSlot;
import ca.concordia.ginacody.comp6231.enums.EventType;

import java.io.Serializable;
import java.util.Date;

public class Event implements Serializable {

    private String id;
    private EventType eventType;
    private EventSlot eventSlot;
    private Date date;
    private int capacity;

    public Event() {
    }

    public Event(String id, EventType eventType, int capacity) {
        this.setId(id);
        this.setEventType(eventType);
        this.setCapacity(capacity);
    }

    public String getId() {
        return id;
    }

    public EventType getEventType() {
        return eventType;
    }

    public EventSlot getEventSlot() {
        return eventSlot;
    }

    public Date getDate() {
        return date;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public void setEventSlot(EventSlot eventSlot) {
        this.eventSlot = eventSlot;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}
