package ca.concordia.ginacody.comp6231.services;

import ca.concordia.ginacody.comp6231.exception.EventManagementServiceException;

/**
 * Hello world!
 */
public interface EventManagementService {
    public String login(String userName) throws EventManagementServiceException;
}