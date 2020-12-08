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

public class SearchContactsViewModel extends AndroidViewModel {

    private MutableLiveData<List<SearchContacts>> mSearchContacts;
    private SearchContactsRecyclerViewAdapter mSearchContactsRecyclerViewAdapter;
    private List<SearchContacts> searchList;

    public SearchContactsViewModel(@NonNull Application application) {
        super(application);

        mSearchContacts = new MutableLiveData<>();
        searchList = new ArrayList<>();
        //searchList.add(new SearchContacts("", ""));
        //mSearchContacts.setValue(searchList);

    }

    /*
     * method to send observer to alert the notification
     */
    public void addSearchContactsObserver(@NonNull LifecycleOwner owner,
                                          @NonNull Observer<? super List<SearchContacts>> observer) {
        mSearchContacts.observe(owner, observer);
    }

    /*
     * method to edu.uw.tcss450.team2.search for contacts from the web server
     *
     */
    public void getSearchContacts(String jwt, String email) {
        String url = getApplication().getResources().getString(R.string.base_url) +
                "search/" + email;

        Request reqest = new JsonObjectRequest(Request.Method.GET,
                url, null, this::handleSuccess, this::handleError) {
                public Map<String, String> getHeaders() {
                    Map<String, String> header = new HashMap<>();
                    header.put("Authorization", jwt);
                    return header;
                };
        };

        reqest.setRetryPolicy(new DefaultRetryPolicy(10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueueSingleton.getInstance(getApplication().getApplicationContext()).addToRequestQueue(reqest);

    }

    private void handleSuccess(final JSONObject response) {
//        List<SearchContacts> list = new ArrayList<>();
        searchList = new ArrayList<>();
        try {

            JSONArray searchs = response.getJSONArray("rows");
            for (int i = 0; i < searchs.length(); i++) {

                JSONObject search = searchs.getJSONObject(i);
                String uFirstname = search.getString("firstname");
                String uLastname = search.getString("lastname");
                String uName = search.getString("username");
                String uEmail = search.getString("email");
                int uMemberid = search.getInt("memberid");
                searchList.add(new SearchContacts(uFirstname, uLastname,
                        uName, uEmail, uMemberid));

//                mSearchContactsRecyclerViewAdapter = new SearchContactsRecyclerViewAdapter(searchList);
//                mSearchContactsRecyclerViewAdapter.setOnItemClickListener(SearchContactsViewModel.this);
                //                SearchContacts sContact = new SearchContacts(
//                        edu.uw.tcss450.team2.search.getString("username"),
//                        edu.uw.tcss450.team2.search.getString("email")
//                );
//                list.add(sContact);
            }


            mSearchContacts.setValue(searchList);
        } catch (JSONException e) {
            Log.e("JSON PARSE ERROR", "Found in handle Success SearchContactsViewModel");
            Log.e("JSON PARSE ERROR", "Error: " + e.getMessage());
        }

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
