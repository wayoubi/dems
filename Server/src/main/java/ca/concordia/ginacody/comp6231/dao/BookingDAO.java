package ca.concordia.ginacody.comp6231.dao;

import ca.concordia.ginacody.comp6231.enums.EventType;
import ca.concordia.ginacody.comp6231.exception.EventManagementServiceException;
import ca.concordia.ginacody.comp6231.vo.EventVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class BookingDAO {

    /**
     *
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(BookingDAO.class);

    /**
     *
     */
    public BookingDAO() {
    }


    /**
     *
     * @param customerID
     * @param eventVO
     * @return
     * @throws EventManagementServiceException
     */
    public String addBooking(String customerID, EventVO eventVO) throws EventManagementServiceException {
        Database.getInstance().getEvents().computeIfAbsent(eventVO.getEventType(), eventType -> {
            LOGGER.error("No {} Events exist, nothing will be booked", eventType);
            throw new EventManagementServiceException(String.format("No %s Events exist, nothing will be booked", eventType));
        });
        Database.getInstance().getEvents().computeIfPresent(eventVO.getEventType(), (type, map) -> {
            map.computeIfAbsent(eventVO.getId(), eventId -> {
                LOGGER.error("Event {} does not exit, nothing will be booked", eventId);
                throw new EventManagementServiceException(String.format("Event %s does not exit, nothing will be booked", eventId));
            });
            return map;
        });
        synchronized(this) {
            Database.getInstance().getEvents().computeIfPresent(eventVO.getEventType(), (type, map) -> {
                map.computeIfPresent(eventVO.getId(), (eventId, eventVO1) -> {

                    LOGGER.info("Check if customer already made a booking in this event {} type {}", eventId, eventVO1.getEventType());
                    Database.getInstance().getEventRecords().computeIfPresent(eventVO1, (eventVO2, list) -> {
                        if(list.contains(customerID)) {
                            throw new EventManagementServiceException(String.format("Customer %s is already booked for %s", customerID, eventId));
                        }
                        return list;
                    });

                    LOGGER.info("Check if there is capacity to book event {}", eventId);
                    int spaces = eventVO1.getCapacity() - eventVO1.getNumberOfAttendees();
                    if(spaces > 0) {
                        LOGGER.info("{} spaces are available Booking in progress", spaces);
                        eventVO1.setNumberOfAttendees(eventVO1.getNumberOfAttendees()+1);

                        Database.getInstance().getUserRecords().putIfAbsent(customerID, new ArrayList<>());
                        Database.getInstance().getUserRecords().computeIfPresent(customerID, (customerID0, list) -> {
                            list.add(eventVO1);
                            return list;
                        });

                        Database.getInstance().getEventRecords().putIfAbsent(eventVO1, new ArrayList<>());
                        Database.getInstance().getEventRecords().computeIfPresent(eventVO1, (eventVO2, list) -> {
                            list.add(customerID);
                            return list;
                        });

                    } else {
                        throw new EventManagementServiceException(String.format("Event %s is full, nothing will be booked", eventId));
                    }
                    return eventVO1;
                });
                return map;
            });
        }
        return String.format("Event %s booked for Customer %s successfully", eventVO.getId(), customerID);
    }

    /**
     *
     * @param customerID
     * @return
     * @throws EventManagementServiceException
     */
    public String selectAllBookings(String customerID) throws EventManagementServiceException {
        return null;
    }

    /**
     *
     * @param customerID
     * @param eventVO
     * @return
     * @throws EventManagementServiceException
     */
    public String removeBooking(String customerID, EventVO eventVO) throws EventManagementServiceException {
        return null;
    }
}