package ca.concordia.ginacody.comp6231.dao;

import ca.concordia.ginacody.comp6231.enums.EventType;
import ca.concordia.ginacody.comp6231.exception.EventManagementServiceException;
import ca.concordia.ginacody.comp6231.vo.EventVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class EventDAO {

    /**
     *
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(EventDAO.class);

    /**
     *
     */
    public EventDAO() {
    }

    /**
     * @param eventVO
     * @return
     * @throws EventManagementServiceException
     */
    public String addEvent(EventVO eventVO) throws EventManagementServiceException {
        Database.getInstance().getEvents().computeIfPresent(eventVO.getEventType(), (type, map) -> {
            map.computeIfPresent(eventVO.getId(), (id, event) -> {
                if(event.getNumberOfAttendees() > eventVO.getCapacity()) {
                    LOGGER.error("Event {} cannot be updated, new capacity is less than already registered users", event.getId());
                    throw new EventManagementServiceException(String.format("Event %s cannot be updated, new capacity is less than already registered users", eventVO.getId()));
                } else if(event.getNumberOfAttendees() <= eventVO.getCapacity()){
                    event.setCapacity(eventVO.getCapacity());
                }
                return event;
            });
            return map;
        });
        Database.getInstance().getEvents().computeIfPresent(eventVO.getEventType(), (type, map) -> {
            map.putIfAbsent(eventVO.getId(), eventVO);
            return map;
        });
        Database.getInstance().getEvents().computeIfAbsent(eventVO.getEventType(), eventType -> new HashMap<>()).putIfAbsent(eventVO.getId(), eventVO);
        LOGGER.info("Event {} added/updated successfully", eventVO.getId());
        return String.format("Event %s added/updated successfully", eventVO.getId());
    }

     /**
     *
     * @param eventVO
     * @return
     * @throws EventManagementServiceException
     */
    public String removeEvent(EventVO eventVO) throws EventManagementServiceException {
        Database.getInstance().getEvents().computeIfAbsent(eventVO.getEventType(), eventType -> {
            LOGGER.error("No {} Events exist, nothing will be removed", eventType);
            throw new EventManagementServiceException(String.format("No %s Events exist, nothing will be removed", eventType));
        });
        Database.getInstance().getEvents().computeIfPresent(eventVO.getEventType(), (type, map) -> {
            map.computeIfAbsent(eventVO.getId(), eventId -> {
                LOGGER.error("Event {} does not exit, nothing will be removed", eventId);
                throw new EventManagementServiceException(String.format("Event %s does not exit, nothing will be removed", eventId));
            });
            return map;
        });
        Database.getInstance().getEvents().computeIfPresent(eventVO.getEventType(), (type, map) -> {
            map.computeIfPresent(eventVO.getId(), (eventId, eventVO1) -> {
                LOGGER.info("Event {} will be removed", eventId);
                map.remove(eventId);
                return null;
            });
            return map;
        });
        return String.format("Event %s removed successfully", eventVO.getId());
    }

    /**
     *
     * @param eventType
     * @return
     * @throws EventManagementServiceException
     */
    public String selectAllEvents(EventType eventType) throws EventManagementServiceException {
        LOGGER.info("selectAllEvents {}", eventType);
        Database.getInstance().getEvents().computeIfAbsent(eventType, et -> {
            LOGGER.error("No {} Events exist, nothing will be listed", et);
            throw new EventManagementServiceException(String.format("No %s Events exist, nothing will be listed", et));
        });
        StringBuilder stringBuilder = new StringBuilder();
        Database.getInstance().getEvents().computeIfPresent(eventType, (eventType1, stringEventVOMap) -> {
            if(stringEventVOMap.values().isEmpty()){
                LOGGER.error("No {} Events exist, nothing will be listed", eventType1);
                throw new EventManagementServiceException(String.format("No %s Events exist, nothing will be listed", eventType1));
            }
            stringEventVOMap.values().stream().forEach(eventVO -> {
                stringBuilder.append(eventVO.getId());
                stringBuilder.append(String.format(" available places [%s]",(eventVO.getCapacity()-eventVO.getNumberOfAttendees())));
                stringBuilder.append(System.lineSeparator());
            });
            return stringEventVOMap;
        });
        LOGGER.info("{} Events List generated successfully", eventType);
        return stringBuilder.toString();
    }
}