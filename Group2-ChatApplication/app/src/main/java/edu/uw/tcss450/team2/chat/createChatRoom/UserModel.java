package edu.uw.tcss450.team2.chat.createChatRoom;

import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class UserModel implements Serializable {

    private final int memberId;
    private final String mUsername;
    private final String nfName;
    private final String nlName;

    public UserModel(int memberId, String fName, String lName, String mUsername) {

        this.memberId = memberId;
        this.nfName = fName;
        this.nlName = lName;
        this.mUsername = mUsername;
    }

    public int getMemberId() {
        return memberId;
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
        if (other instanceof UserModel) {
            result = mUsername == ((UserModel) other).mUsername;
        }
        return result;
    }
}
