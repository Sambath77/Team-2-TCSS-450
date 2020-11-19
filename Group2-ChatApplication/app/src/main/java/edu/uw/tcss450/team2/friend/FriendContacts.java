package edu.uw.tcss450.team2.friend;

import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class FriendContacts implements Serializable {
    private final String mUsername;
    private final int mContactId;
    private final int mMemberId;

    public FriendContacts(int mContactId, int mMemberId, String mUsername) {
        this.mUsername = mUsername;
        this.mContactId = mContactId;
        this.mMemberId = mMemberId;
    }



    public static FriendContacts createFromJsonString(final String cmAsJson) throws JSONException {
        final JSONObject username = new JSONObject(cmAsJson);
        return new FriendContacts(username.getInt("ContactId"), username.getInt("MemberId"),username.getString("Username"));
    }

    public String getmUsername() {
        return mUsername;
    }

    public int getmContactId() {
        return mContactId;
    }

    public int getmMemberId() {
        return mMemberId;
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
