package edu.uw.tcss450.team2.chat;

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
import android.widget.PopupWindow;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import edu.uw.tcss450.team2.MainActivity;
import edu.uw.tcss450.team2.R;
import edu.uw.tcss450.team2.databinding.FragmentChatListBinding;
import edu.uw.tcss450.team2.friend.FriendListFragmentDirections;
import edu.uw.tcss450.team2.model.UserInfoViewModel;

/**
 *
 */
public class ChatListFragment extends Fragment {

    private ChatListViewModel mModel;
    private MutableLiveData<JSONObject> mResponse;
    private UserInfoViewModel userInfoViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat_list, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userInfoViewModel = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);

        mResponse = new MutableLiveData<>();
        mResponse.setValue(new JSONObject());


        mModel = new ViewModelProvider(getActivity()).get(ChatListViewModel.class);
        userInfoViewModel.setChatListViewModel(mModel);


        mModel.connectGet(userInfoViewModel.getJwt(), userInfoViewModel.getEmail());
    }




    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentChatListBinding binding = FragmentChatListBinding.bind(getView());

        mModel.setBinding(binding);

        mModel.addUserListObserver(getViewLifecycleOwner(), userList -> {
            if (!userList.isEmpty()) {
                binding.listRoot.setAdapter(
                        new ChatListRecyclerViewAdapter(userList, userInfoViewModel.getChatRoomsIdForNewMessage(), userInfoViewModel)
                );
                binding.layoutWait.setVisibility(View.GONE);
            }
        });

        binding.createChatRoomBtn.setOnClickListener(event -> {
                    Navigation.findNavController(view).navigate(ChatListFragmentDirections
                            .actionNavigationChatToCreateChatRoomFragment());
                }
        );

    }



}