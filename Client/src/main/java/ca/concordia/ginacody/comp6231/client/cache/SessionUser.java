package ca.concordia.ginacody.comp6231.client.cache;

import ca.concordia.ginacody.comp6231.UserType;
import org.springframework.stereotype.Component;
import java.util.Objects;

@Component
public class SessionUser {

    private String userName;

    private String location;

    private UserType userType;

    public SessionUser(){}

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setUserName(String userName) {
        this.userName = userName;
        this.location = this.userName.substring(0, 3);
    }

    public String getUserName() {
        return userName;
    }
}
