package edu.uw.tcss450.team2.search;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import edu.uw.tcss450.team2.R;
import edu.uw.tcss450.team2.io.RequestQueueSingleton;

public class CancelFreindRequestViewModel extends AndroidViewModel {

    private MutableLiveData<JSONObject> mutableLiveData;
    public CancelFreindRequestViewModel(@NonNull Application application) {
        super(application);
        mutableLiveData = new MutableLiveData<>();
        mutableLiveData.setValue(new JSONObject());
    }

    public void addCancelObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super JSONObject> observer) {
        mutableLiveData.observe(owner, observer);
    }

    public void getCancelSendRequest(String jwt, String email_A, String email_B) {

        String url = getApplication().getResources().getString(R.string.base_url) +
                "request/" + email_A + "/" + email_B;



        Request request = new JsonObjectRequest(Request.Method.DELETE,
                url, null, mutableLiveData::setValue, this::handleError) {
            public Map<String, String> getHeaders() {
                Map<String, String> header = new HashMap<>();
                header.put("Authorization", jwt);
                return header;
            };
        };

        request.setRetryPolicy(new DefaultRetryPolicy(10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueueSingleton.getInstance(getApplication().getApplicationContext()).addToRequestQueue(request);
    }

    private void handleError(final VolleyError error) {
        if (Objects.isNull(error.networkResponse)) {
            Log.e("NETWORK ERROR", error.getMessage());
        } else {
            String data = new String(error.networkResponse.data, Charset.defaultCharset());
            Log.e("CLIENT ERROR", error.networkResponse + " " + data);
        }
    }
}
