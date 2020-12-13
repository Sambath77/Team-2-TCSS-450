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

import edu.uw.tcss450.team2.R;
import edu.uw.tcss450.team2.chat.createChatRoom.UserModel;
import edu.uw.tcss450.team2.databinding.FragmentChatListBinding;
import edu.uw.tcss450.team2.io.RequestQueueSingleton;
import edu.uw.tcss450.team2.model.UserInfoViewModel;

/**
 *
 */
public class ChatListViewModel extends AndroidViewModel {

    private MutableLiveData<List<ChatRoomModel>> mRooms;
    private String jwt;
    private ChatRoomModel currentChatRoomModel;
    private FragmentChatListBinding binding;
    private UserInfoViewModel userInfoViewModel;

    public ChatListViewModel(@NonNull Application application) {
        super(application);

        mRooms = new MutableLiveData<>();
        mRooms.setValue(new ArrayList<>());

    }

    public void setUserInfoViewModel(UserInfoViewModel userInfoViewModel) {
        this.userInfoViewModel = userInfoViewModel;
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


    private List<Integer> memberIds;
    private int chatId;
    public void deleteChatRoom(String jwt, int chatId) {
        this.chatId = chatId;
        getMembers(userInfoViewModel.getJwt(), chatId);
    }

    private void getMembers(String jwt, int chatId) {
        binding.layoutWait.setVisibility(View.VISIBLE);
        String url =
                "https://team-2-tcss-450-webservices.herokuapp.com/chatrooms/getMembersByChatId/" + chatId;

        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null, //no body for this get edu.uw.tcss450.team2.request
                this::handelSuccessForGetMembers,
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


    private void handelSuccessForGetMembers(final JSONObject response)  {
        memberIds = new ArrayList<>();
        try {

            JSONArray messages = response.getJSONArray("rows");
            for(int i = 0; i < messages.length(); i++) {
                JSONObject message = messages.getJSONObject(i);
                memberIds.add(message.getInt("memberid"));
            }
            binding.layoutWait.setVisibility(View.GONE);
            sendDeleteChatRoomRequest();
        }catch (JSONException e) {
            Log.e("JSON PARSE ERROR", "Found in handle Success FriendContactsViewModel");
            Log.e("JSON PARSE ERROR", "Error: " + e.getMessage());
        }
    }

    private void sendDeleteChatRoomRequest() {
        JSONObject jsonBody = new JSONObject();
        try {
            JSONArray memberIdJson = new JSONArray();
            for (Integer memberId: memberIds) {
                memberIdJson.put(memberId);
            }
            jsonBody.put("chatId",chatId);
            jsonBody.put("members", memberIdJson);

            binding.layoutWait.setVisibility(View.VISIBLE);
            sendDeleteChatRoomRequest(userInfoViewModel.getJwt(), jsonBody);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void sendDeleteChatRoomRequest(String jwt, JSONObject jsonBody) {
        String url = this.getApplication().getResources().getString(R.string.base_url) +
                "chatrooms/deleteChatRoom";

        Request request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonBody, //json body
                this::handelSuccessForDeleteChatRoom,
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

    private void handelSuccessForDeleteChatRoom(final JSONObject response)  {
        connectGet(userInfoViewModel.getJwt(), userInfoViewModel.getEmail());
    }

}
