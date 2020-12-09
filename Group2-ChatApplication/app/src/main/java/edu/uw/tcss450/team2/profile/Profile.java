package edu.uw.tcss450.team2.profile;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import edu.uw.tcss450.team2.friend.FriendContacts;

public class Profile {

    private String mUsername;
    private String nfName;
    private String nlName;
    private String email;
    private int memberId;
    private List<Integer> list;

    public Profile(String fName, String lName, String mUsername, String email, int memberId) {

        this.nfName = fName;
        this.nlName = lName;
        this.mUsername = mUsername;
        this.email = email;
        this.memberId = memberId;
        list = new ArrayList<>();
    }



    /**
     * Static factory method to turn a properly formatted JSON String into a
     * FriendContacts object.
     * @param cmAsJson the String to be parsed into a ChatMessage Object.
     * @return a FriendContacts Object with the details contained in the JSON String.
     * @throws JSONException when cmAsString cannot be parsed into a FriendContacts.
     */
    public static FriendContacts createFromJsonString(final String cmAsJson) throws JSONException {
        final JSONObject username = new JSONObject(cmAsJson);
        return new FriendContacts(username.getString("firstname"),
                username.getString("lastname"),
                username.getString("username"),
                username.getString("email"),
                username.getInt("memberId"));
    }

    /*
     * method to return the username
     */
    public String getmUsername() {
        return mUsername;
    }

    /*
     * method to return the first name
     */
    public String getmFName() {
        return nfName;
    }

    /*
     * method to return the last name
     */
    public String getmLName() {
        return nlName;
    }

    /*
     * method to return the email
     */
    public String getEmail() {
        return email;
    }

    /*
     * method to return the memberId
     */
    public int getMemberId() {
        return memberId;
    }
}
