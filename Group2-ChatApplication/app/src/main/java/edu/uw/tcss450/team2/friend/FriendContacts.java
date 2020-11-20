package edu.uw.tcss450.team2.friend;

import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class FriendContacts implements Serializable {
    private final String mUsername;
    private final String nfName;
    private final String nlName;

    public FriendContacts(String fName, String lName, String mUsername) {

        this.nfName = fName;
        this.nlName = lName;
        this.mUsername = mUsername;
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
                username.getString("lastname"),username.getString("username"));
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


    @Override
    public boolean equals(@Nullable Object other) {
        boolean result = false;
        if (other instanceof FriendContacts) {
            result = mUsername == ((FriendContacts) other).mUsername;
        }
        return result;
    }
}
