package edu.uw.tcss450.team2.chat.createChatRoom;

import android.app.Application;
import android.util.Log;
import android.view.View;

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
import edu.uw.tcss450.team2.databinding.FragmentCreateChatRoomBinding;

import edu.uw.tcss450.team2.io.RequestQueueSingleton;


public class NewChatRoomUserViewModel extends AndroidViewModel {

    private MutableLiveData<List<UserModel>> mContacts;
    private FragmentCreateChatRoomBinding binding;

    public NewChatRoomUserViewModel(@NonNull Application application) {
        super(application);

        mContacts = new MutableLiveData<>();
        mContacts.setValue(new ArrayList<>());
    }

    /*
     * method to send observer to alert the notification
     */

    public void addContactListObserver(@NonNull LifecycleOwner owner,
                                       @NonNull Observer<? super List<UserModel>> observer) {
        mContacts.observe(owner, observer);
    }

    public void setBinding(FragmentCreateChatRoomBinding binding) {
        this.binding = binding;
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
                null, //no body for this get request
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
        //Instantiate the RequestQueue and add the request to the queue
        RequestQueueSingleton.getInstance(getApplication().getApplicationContext())
                .addToRequestQueue(request);
    }


    /*
     * helper method to handle the successful for client requests
     * @param: response is the JSONObject that response to the end point
     */

    private void handelSuccess(final JSONObject response)  {
        List<UserModel> list = new ArrayList<>();
        try {

            JSONArray messages = response.getJSONArray("rows");
            for(int i = 0; i < messages.length(); i++) {
                JSONObject message = messages.getJSONObject(i);

                UserModel friendContacts = new UserModel(
                        message.getInt("memberid"),
                        message.getString("firstname"),
                        message.getString("lastname"),
                        message.getString("username")
                );

                list.add(friendContacts);
            }

            mContacts.setValue(list);
            binding.layoutWait.setVisibility(View.GONE);
        }catch (JSONException e) {
            Log.e("JSON PARSE ERROR", "Found in handle Success FriendContactsViewModel");
            Log.e("JSON PARSE ERROR", "Error: " + e.getMessage());
        }
    }

    /*
    * helper method to handle a error from the request
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
}
