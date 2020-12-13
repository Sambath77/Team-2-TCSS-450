package edu.uw.tcss450.team2.search;

import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

public class SearchContacts {

    private String mSearchUsername;
    private String mSearchEmail;
    private String mFirstname;
    private String mLastname;

    public SearchContacts(String mFirstname, String mLastname,String mSearchUsername,
                          String mSearchEmail) {
        this.mFirstname = mFirstname;
        this.mLastname = mLastname;
        this.mSearchUsername = mSearchUsername;
        this.mSearchEmail = mSearchEmail;

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
                searchName.getString("email"));
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

    @Override
    public boolean equals(@Nullable Object other) {
        boolean result = false;
        if (other instanceof SearchContacts) {
            result = mSearchUsername == ((SearchContacts) other).mSearchUsername;
        }
        return result;
    }
}
