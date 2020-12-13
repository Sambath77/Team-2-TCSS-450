package edu.uw.tcss450.team2.chat.createChatRoom;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONObject;

import edu.uw.tcss450.team2.R;
import edu.uw.tcss450.team2.databinding.FragmentCreateChatRoomBinding;
import edu.uw.tcss450.team2.databinding.FragmentCreateChatRoomCardBinding;
import edu.uw.tcss450.team2.model.UserInfoViewModel;


public class CreateChatRoomFragment extends Fragment {

    private NewChatRoomUserViewModel mModel;
    private MutableLiveData<JSONObject> mResponse;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mResponse = new MutableLiveData<>();
        mResponse.setValue(new JSONObject());

        UserInfoViewModel userInfoViewModel = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);

        mModel = new ViewModelProvider(getActivity()).get(NewChatRoomUserViewModel.class);

        mModel.getContactFriend(userInfoViewModel.getJwt(), userInfoViewModel.getEmail());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentCreateChatRoomBinding binding = FragmentCreateChatRoomBinding.bind(getView());

        mModel.addContactListObserver(getViewLifecycleOwner(), contactList -> {
            if (!contactList.isEmpty()) {
                binding.listRoot.setAdapter(
                        new NewChatRoomUserRecyclerViewAdapter(contactList));
                binding.layoutWait.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_chat_room, container, false);
    }
}