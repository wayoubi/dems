package ca.concordia.ginacody.comp6231.client.cache;

import org.springframework.stereotype.Component;
import java.util.Objects;

@Component
public class SessionUser {

    private String userName;

    private String location;

    public SessionUser(){}

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }
}
