package ca.concordia.ginacody.comp6231.client.cache;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Session {

    /**
     *
     */
    @Value( "${dems.service.url}" )
    private String serviceURL;

    /**
     *
     */
    private String userName;

    /**
     *
     */
    private String location;

    /**
     *
     */
    private UserType userType;

    /**
     *
     */
    private boolean active;

    /**
     *
     */
    public Session(){
        this.setActive(false);
    }

    /**
     *
     * @param userName
     */
    public void init(String userName) {
        this.setUserName(userName);
        this.setLocation(this.userName.substring(0, 3));
        this.setUserType(UserType.get(Character.toString(this.userName.charAt(3))));
        this.setActive(true);
    }

    /**
     *
     * @param userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     *
     * @param userType
     */
    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    /**
     *
     * @param location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     *
     * @param active
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     *
     * @return
     */
    public UserType getUserType() {
        return userType;
    }

    /**
     *
     * @return
     */

    public String getUserName() {
        return userName;
    }

    /**
     *
     * @return
     */
    public String getLocation() {
        return location;
    }

    /**
     *
     * @return
     */
    public String getServiceURL() {
        return serviceURL;
    }

    /**
     *
     * @return
     */
    public boolean isActive() {
        return active;
    }
}
