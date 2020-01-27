package ca.concordia.ginacody.comp6231.client.cache;

import org.springframework.stereotype.Component;

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
        if(this.userName.charAt(3)=='C') {
            this.setUserType(UserType.CUSTOMER);
        } else if(this.userName.charAt(3)=='M') {
            this.setUserType(UserType.EVENT_MANAGER);
        } else {
            throw new RuntimeException("Invalid Username, user type cannot be determined");
        }
    }

    public String getUserName() {
        return userName;
    }
}
