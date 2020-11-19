package edu.uw.tcss450.team2.chat;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.tcss450.team2.R;
import edu.uw.tcss450.team2.databinding.FragmentChatListBinding;

public class ChatListFragment extends Fragment {

    private ChatViewModel mModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_chat_list, container, false);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //UserInfoViewModel model = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);

        mModel = new ViewModelProvider(getActivity()).get(ChatViewModel.class);
        //mModel.connectGet(model.getJwt());
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentChatListBinding binding = FragmentChatListBinding.bind(getView());


        mModel.addUserListObserver(getViewLifecycleOwner(), userList -> {
            if (!userList.isEmpty()) {
                binding.listRoot.setAdapter(
                        new ChatRecyclerViewAdapter(userList)
                );
                binding.layoutWait.setVisibility(View.GONE);
            }
        });


    }



}