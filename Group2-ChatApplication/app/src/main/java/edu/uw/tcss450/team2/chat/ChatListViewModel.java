package edu.uw.tcss450.team2.chat;

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
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import edu.uw.tcss450.team2.databinding.FragmentChatListBinding;

/**
 *
 */
public class ChatListViewModel extends AndroidViewModel {

    private MutableLiveData<List<ChatRoomModel>> mRooms;
    private String jwt;
    private ChatRoomModel currentChatRoomModel;
    private FragmentChatListBinding binding;

    public ChatListViewModel(@NonNull Application application) {
        super(application);

        mRooms = new MutableLiveData<>();
        mRooms.setValue(new ArrayList<>());

    }

    public void addUserListObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super List<ChatRoomModel>> observer) {
        mRooms.observe(owner, observer);
    }

    public void connectGet(String jwt, String userEmail) {

        this.mRooms.setValue(new ArrayList<>());
        this.jwt = jwt;

        String url =
                "https://team-2-tcss-450-webservices.herokuapp.com/chatrooms/" + userEmail;
        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null, //no body for this get edu.uw.tcss450.team2.request
                this::handelSuccess,
                this::handleError) {

            @Override
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
        Volley.newRequestQueue(getApplication().getApplicationContext())
                .add(request);




    }


    private void handelSuccess(final JSONObject response) {
        List<ChatMessage> list;
        try {
            //list = getMessageListByChatId(response.getInt("chatId"));
            mRooms.setValue(new ArrayList<>());
            JSONArray messages = response.getJSONArray("rows");
            for(int i = 0; i < messages.length(); i++) {
                JSONObject message = messages.getJSONObject(i);
                ChatRoomModel chatRoom = new ChatRoomModel();
                chatRoom.setChatId(message.getInt("chatid"));
                chatRoom.setRoomName(message.getString("name"));
                mRooms.getValue().add(chatRoom);
            }

            mRooms.setValue(mRooms.getValue());
            binding.layoutWait.setVisibility(View.GONE);

        }catch (JSONException e) {
            Log.e("JSON PARSE ERROR", "Found in handle Success ChatViewModel");
            Log.e("JSON PARSE ERROR", "Error: " + e.getMessage());
        }
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

    public void setBinding(FragmentChatListBinding binding) {
        this.binding = binding;
    }

}
