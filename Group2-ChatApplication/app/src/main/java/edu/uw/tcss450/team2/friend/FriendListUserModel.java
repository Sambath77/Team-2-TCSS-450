package edu.uw.tcss450.team2.friend;

import java.io.Serializable;

public class FriendListUserModel implements Serializable {

    private String friendFirstName;
    private String friendLastName;
    private String friendEmail;
    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getFriendFirstName() {
        return friendFirstName;
    }

    public void setFriendFirstName(String friendFirstName) {
        this.friendFirstName = friendFirstName;
    }

    public String getFriendLastName() {
        return friendLastName;
    }

    public void setFriendLastName(String friendLastName) {
        this.friendLastName = friendLastName;
    }

    public String getFriendEmail() {
        return friendEmail;
    }
    public void setFriendEmail(String friendEmail) {
        this.friendEmail = friendEmail;
    }

}
