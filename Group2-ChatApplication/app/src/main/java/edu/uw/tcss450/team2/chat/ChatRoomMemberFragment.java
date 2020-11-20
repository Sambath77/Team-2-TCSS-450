package edu.uw.tcss450.team2.chat;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import edu.uw.tcss450.team2.R;
import edu.uw.tcss450.team2.databinding.FragmentChatRoomMemberBinding;
import edu.uw.tcss450.team2.model.UserInfoViewModel;

/**
 *
 */
public class ChatRoomMemberFragment extends Fragment {

    UserInfoViewModel userInfoViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        userInfoViewModel = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);
        connectGet(userInfoViewModel.getJwt(), this.getArguments().getInt("chatId"));

        return inflater.inflate(R.layout.fragment_chat_room_member, container, false);
    }

    public void connectGet(String jwt, int chatId) {

        String url =
                "https://team-2-tcss-450-webservices.herokuapp.com/chatrooms/getMembersByChatId/" + chatId;

        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null, //no body for this get request
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
        //Instantiate the RequestQueue and add the request to the queue
        Volley.newRequestQueue(getActivity().getApplication().getApplicationContext())
                .add(request);
    }


    private void handelSuccess(final JSONObject response) {
        String members = "Members: ";
        try {
            JSONArray messages = response.getJSONArray("rows");
            for(int i = 0; i < messages.length(); i++) {
                JSONObject message = messages.getJSONObject(i);
                members += message.getString("username") + ", ";
            }
            members = members.substring(0, members.length() - 2);
            FragmentChatRoomMemberBinding binding = FragmentChatRoomMemberBinding.bind(getView());
            binding.membersTxt.setText(members);
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


}