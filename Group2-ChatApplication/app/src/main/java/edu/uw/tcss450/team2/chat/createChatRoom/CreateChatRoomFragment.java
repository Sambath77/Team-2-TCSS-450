package edu.uw.tcss450.team2.chat.createChatRoom;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import java.util.function.Function;
import java.util.stream.Collectors;

import edu.uw.tcss450.team2.R;
import edu.uw.tcss450.team2.chat.ChatListFragmentDirections;
import edu.uw.tcss450.team2.databinding.FragmentCreateChatRoomBinding;
import edu.uw.tcss450.team2.databinding.FragmentCreateChatRoomCardBinding;
import edu.uw.tcss450.team2.home.HomeFragmentDirections;
import edu.uw.tcss450.team2.io.RequestQueueSingleton;
import edu.uw.tcss450.team2.model.UserInfoViewModel;


public class CreateChatRoomFragment extends Fragment {

    private NewChatRoomUserViewModel mModel;
    private MutableLiveData<JSONObject> mResponse;
    private Map<UserModel, Boolean> mEditUser;
    UserInfoViewModel userInfoViewModel;
    FragmentCreateChatRoomBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mResponse = new MutableLiveData<>();
        mResponse.setValue(new JSONObject());

        userInfoViewModel = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);

        mModel = new ViewModelProvider(getActivity()).get(NewChatRoomUserViewModel.class);

        mModel.getContactFriend(userInfoViewModel.getJwt(), userInfoViewModel.getEmail());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding = FragmentCreateChatRoomBinding.bind(getView());

        mModel.addContactListObserver(getViewLifecycleOwner(), contactList -> {
            if (contactList != null) {
                mEditUser = contactList.stream()
                        .collect(Collectors.toMap(Function.identity(), blog -> false));
                binding.listRoot.setAdapter(
                        new NewChatRoomUserRecyclerViewAdapter(contactList, mEditUser));
                binding.layoutWait.setVisibility(View.GONE);
            }
        });

        binding.createBtn.setOnClickListener(event -> {

            List<Integer> memberIds = new ArrayList<>();
            memberIds.add(userInfoViewModel.getMemberId());
            for(UserModel key: mEditUser.keySet()) {
                if(mEditUser.get(key)) {
                    memberIds.add(key.getMemberId());
                }
            }

            JSONObject jsonBody = new JSONObject();
            try {
                JSONArray memberIdJson = new JSONArray();
                for (Integer memberId: memberIds) {
                    memberIdJson.put(memberId);
                }
                jsonBody.put("roomName", binding.chatRoomNameTxt.getText());
                jsonBody.put("members", memberIdJson);

                binding.layoutWait.setVisibility(View.VISIBLE);
                sendCreateChatRoomRequest(userInfoViewModel.getJwt(), jsonBody);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_chat_room, container, false);
    }

    private void sendCreateChatRoomRequest(String jwt, JSONObject jsonBody) {
        String url = this.getActivity().getApplication().getResources().getString(R.string.base_url) +
                "chatrooms/createChatRoomWithMembers";

        Request request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonBody, //json body
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
        RequestQueueSingleton.getInstance(getActivity().getApplication().getApplicationContext())
                .addToRequestQueue(request);
    }

    private void handelSuccess(final JSONObject response)  {
        binding.layoutWait.setVisibility(View.GONE);
        Navigation.findNavController(this.getView()).navigate(CreateChatRoomFragmentDirections
                .actionCreateChatRoomFragmentToNavigationChat());
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