package edu.uw.tcss450.team2.request;

import android.widget.ImageButton;

import org.json.JSONException;
import org.json.JSONObject;

public class SendFriendRequest {


    private String mUsername;
    private String mEmail;
    private int mMemberId;

    public SendFriendRequest(String mUsername, String mEmail, int mMemberId) {
        this.mUsername = mUsername;
        this.mEmail = mEmail;
        this.mMemberId = mMemberId;
    }

    public String getmUsername() {
        return mUsername;
    }

    public void setmUsername(String mUsername) {
        this.mUsername = mUsername;
    }

    public String getmEmail() {
        return mEmail;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public int getmMemberId() {
        return mMemberId;
    }

    /**
     * Static factory method to turn a properly formatted JSON String into a
     * FriendContacts object.
     * @param cmAsJson the String to be parsed into a ChatMessage Object.
     * @return a FriendContacts Object with the details contained in the JSON String.
     * @throws JSONException when cmAsString cannot be parsed into a FriendContacts.
     */
    public static SendFriendRequest createFromJsonString(final String cmAsJson) throws JSONException {
        final JSONObject username = new JSONObject(cmAsJson);
        return new SendFriendRequest(username.getString("username"),
                username.getString("email"), username.getInt("mMemberId"));

    }

}
