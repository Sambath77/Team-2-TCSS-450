package edu.uw.tcss450.team2.friend;

import android.app.Application;
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

import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;

import edu.uw.tcss450.team2.R;
import edu.uw.tcss450.team2.io.RequestQueueSingleton;


public class FriendContactsViewModel extends AndroidViewModel {

    private MutableLiveData<List<FriendContacts>> mContacts;

    public FriendContactsViewModel(@NonNull Application application) {
        super(application);
    }

    public void addFriendListObserver(@NonNull LifecycleOwner owner, @NonNull Observer<? super List<FriendContacts>> observer) {
        mContacts.observe(owner, observer);
    }

    public void getContactFriend() {
        String url = getApplication().getResources().getString(R.string.base_url) +
                "contact";
        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null, //no body for this get request
                this::handelSuccess,
                this::handleError);
        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        RequestQueueSingleton.getInstance(getApplication().getApplicationContext())
                .addToRequestQueue(request);
    }

//    public void addContacts(final FriendContacts contacts) {
//        List<FriendContacts> list = getMessageListByChatId(chatId);
//        list.add(contacts);
//
//    }

    private void handelSuccess(final JSONObject response)  {

    }

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
