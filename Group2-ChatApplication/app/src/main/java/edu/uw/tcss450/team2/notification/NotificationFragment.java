package edu.uw.tcss450.team2.notification;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONObject;

import edu.uw.tcss450.team2.R;
import edu.uw.tcss450.team2.chat.ChatListFragmentDirections;
import edu.uw.tcss450.team2.chat.ChatListRecyclerViewAdapter;
import edu.uw.tcss450.team2.chat.ChatListViewModel;
import edu.uw.tcss450.team2.databinding.FragmentChatListBinding;
import edu.uw.tcss450.team2.databinding.FragmentNotificationBinding;
import edu.uw.tcss450.team2.model.UserInfoViewModel;


public class NotificationFragment extends Fragment {

    private NotificationViewModel mModel;
    private UserInfoViewModel userInfoViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notification, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userInfoViewModel = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);


        mModel = new ViewModelProvider(getActivity()).get(NotificationViewModel.class);
        //userInfoViewModel.setChatListViewModel(mModel);


        mModel.connectGet(userInfoViewModel.getJwt(), userInfoViewModel.getMemberId());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentNotificationBinding binding = FragmentNotificationBinding.bind(getView());

        mModel.setBinding(binding);

        mModel.addNotificationListObserver(getViewLifecycleOwner(), userList -> {
            if (!userList.isEmpty()) {
                binding.listRoot.setAdapter(
                        new NotificationRecyclerViewAdapter(userList)
                );
                binding.layoutWait.setVisibility(View.GONE);
            }
        });

    }

}