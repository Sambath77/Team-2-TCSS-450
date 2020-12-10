package edu.uw.tcss450.team2.search;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import edu.uw.tcss450.team2.R;
import edu.uw.tcss450.team2.databinding.FragmentSearchContactsListBinding;
import edu.uw.tcss450.team2.io.RequestQueueSingleton;

public class AddSendRequestViewModel extends AndroidViewModel {

    private MutableLiveData<JSONObject> mutableLiveData;
    private FragmentSearchContactsListBinding binding;

    public AddSendRequestViewModel(@NonNull Application application) {
        super(application);
        mutableLiveData = new MutableLiveData<>();
        mutableLiveData.setValue(new JSONObject());
    }

    public void setBinding(FragmentSearchContactsListBinding binding) {
        this.binding = binding;
    }

    public void addSendResponseObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super JSONObject> observer) {
        mutableLiveData.observe(owner, observer);
    }

    public void getAddSendRequest(String jwt, String email_A, String email_B, int memberId_B) {
        binding.recyclerViewWait.setVisibility(View.VISIBLE);

        String url = getApplication().getResources().getString(R.string.base_url) +
                "request";

        JSONObject body = new JSONObject();

        try {
            body.put("email_A", email_A);
            body.put("email_B", email_B);
            body.put("memberId_B", memberId_B);
        } catch (JSONException e){
            e.printStackTrace();
        }

        Request request = new JsonObjectRequest(Request.Method.POST,
                url, body, this::handelSuccess, this::handleError) {
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

    private void handelSuccess(final JSONObject response)  {
        binding.recyclerViewWait.setVisibility(View.GONE);
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
