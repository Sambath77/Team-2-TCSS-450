package edu.uw.tcss450.team2.friend;

import android.app.Application;
import android.app.admin.FactoryResetProtectionPolicy;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import edu.uw.tcss450.team2.R;
import edu.uw.tcss450.team2.io.RequestQueueSingleton;
import edu.uw.tcss450.team2.search.SearchContacts;


public class FriendContactsViewModel extends AndroidViewModel {

    private MutableLiveData<List<FriendContacts>> mContacts;
    private List<FriendContacts> list;
    private FriendContacts searchContacts;
    private List<FriendContacts> mList;



    public FriendContactsViewModel(@NonNull Application application) {
        super(application);
        mContacts = new MutableLiveData<>();
        list = new ArrayList<>();
        searchContacts = new FriendContacts("", "", "", "", 0 );
        mList = new ArrayList<>();
    }

    /*
     * method to send observer to alert the notification
     */

    public void addContactListObserver(@NonNull LifecycleOwner owner,
                                       @NonNull Observer<? super List<FriendContacts>> observer) {
        mContacts.observe(owner, observer);
    }

    /*
     * method to setup a contact edu.uw.tcss450.team2.friend from the web server
     *
     */

    public void getContactFriend(String jwt, String email) {
        String url = getApplication().getResources().getString(R.string.base_url) +
                "contact/" + email;

        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null, //no body for this get edu.uw.tcss450.team2.request
                this::handelSuccess,
                this::handleError) {
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<>();
                    // add headers <key,value>
                    headers.put("Authorization", jwt);
                    return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the edu.uw.tcss450.team2.request to the queue
        RequestQueueSingleton.getInstance(getApplication().getApplicationContext())
                .addToRequestQueue(request);
    }


    /*
     * helper method to handle the successful for client requests
     * @param: response is the JSONObject that response to the end point
     */
    private void handelSuccess(final JSONObject response)  {

        try {

            JSONArray contacts = response.getJSONArray("rows");

            for(int i = 0; i < contacts.length(); i++) {
                JSONObject message = contacts.getJSONObject(i);

                FriendContacts friendContacts = new FriendContacts(
                        message.getString("firstname"),
                        message.getString("lastname"),
                        message.getString("username"),
                        message.getString("email"),
                        message.getInt("memberid"));

                    list.add(friendContacts);

            }

            if (list.isEmpty()) {
                mList.add(searchContacts);
                mContacts.setValue(mList);
            } else {
                mContacts.setValue(list);
            }


        }catch (JSONException e) {
            Log.e("JSON PARSE ERROR", "Found in handle Success FriendContactsViewModel");
            Log.e("JSON PARSE ERROR", "Error: " + e.getMessage());
        }
    }

    /*
    * helper method to handle a error from the edu.uw.tcss450.team2.request
    * @param: error
    */
    private void handleError(final VolleyError error) {
        if (Objects.isNull(error.networkResponse)) {
            Log.e("NETWORK ERROR", error.getMessage());
        }
        else {
            String data = new String(error.networkResponse.data, Charset.defaultCharset());
            Log.e("CLIENT ERROR",
                    error.networkResponse.statusCode +
                            " " +
                            data);
        }
    }

    public void DeleteContactFriend(String jwt, String email, String email_B) {
        String url = getApplication().getResources().getString(R.string.base_url) +
                "contact/" + email + "/" + email_B;
//
//        JSONObject body = new JSONObject();
//        try {
//            body.put("email", email);
//            body.put("email_B", email_B);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        Request request = new JsonObjectRequest(
                Request.Method.DELETE,
                url,
                null, //no body for this get edu.uw.tcss450.team2.request
                this::handelSuccess,
                this::handleError) {
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // add headers <key,value>
                headers.put("Authorization", jwt);
                return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the edu.uw.tcss450.team2.request to the queue
        RequestQueueSingleton.getInstance(getApplication().getApplicationContext())
                .addToRequestQueue(request);
    }
}
