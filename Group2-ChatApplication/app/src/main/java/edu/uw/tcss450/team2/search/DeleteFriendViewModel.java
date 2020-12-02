package edu.uw.tcss450.team2.search;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import edu.uw.tcss450.team2.R;

public class DeleteFriendViewModel extends AndroidViewModel {

    private MutableLiveData<JSONObject> mutableLiveData;
    public DeleteFriendViewModel(@NonNull Application application) {
        super(application);
        mutableLiveData = new MutableLiveData<>();
        mutableLiveData.setValue(new JSONObject());
    }

    public void addResponObserver(@NonNull LifecycleOwner owner, @NonNull Observer<? super JSONObject> observer) {
        mutableLiveData.observe(owner, observer);
    }

    public void deleteFriend(String jwt, String email_A, String email_B) {
        String url = getApplication().getResources().getString(R.string.base_url) +
                "edu/uw/tcss450/team2/search/" + email_A + "/" + email_B;

        Request request = new JsonObjectRequest(Request.Method.DELETE, url, null, mutableLiveData::setValue,
                this::handleError) {
            public Map<String, String> getHeader() {
                Map<String, String> header = new HashMap<>();
                header.put("Authorization", jwt);
                return header;
            };
        };
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
