package edu.uw.tcss450.team2.search;

import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class SearchContacts implements Serializable {

    private String mSearchUsername;
    private String mSearchEmail;
    private String mFirstname;
    private String mLastname;
    private int memberid;
    public SearchContacts(String mFirstname, String mLastname,String mSearchUsername,
                          String mSearchEmail, int memberid) {
        this.mFirstname = mFirstname;
        this.mLastname = mLastname;
        this.mSearchUsername = mSearchUsername;
        this.mSearchEmail = mSearchEmail;
        this.memberid = memberid;

    }

    /**
     * Static factory method to turn a properly formatted JSON String into a
     * FriendContacts object.
     * @param cmAsJson the String to be parsed into a ChatMessage Object.
     * @return a FriendContacts Object with the details contained in the JSON String.
     * @throws JSONException when cmAsString cannot be parsed into a FriendContacts.
     */
    public static SearchContacts createFromJsonString(final String cmAsJson) throws JSONException {
        final JSONObject searchName = new JSONObject(cmAsJson);
        return new SearchContacts(searchName.getString("firstname"),
                searchName.getString("lastname"),
                searchName.getString("username"),
                searchName.getString("email"),
                searchName.getInt("memberid"));
    }

    public void changedUsername(String text) {
        mSearchUsername = text;
    }
    public String getmSearchUsername() {
        return mSearchUsername;
    }

    public String getmSearchEmail() {
        return mSearchEmail;
    }

    public String getmFirstname() {
        return mFirstname;
    }

    public String getmLastname() {
        return mLastname;
    }

    public int getMemberid() {
        return memberid;
    }
    @Override
    public boolean equals(@Nullable Object other) {
        boolean result = false;
        if (other instanceof SearchContacts) {
            result = mSearchUsername == ((SearchContacts) other).mSearchUsername;
        }
        return result;
    }
}
