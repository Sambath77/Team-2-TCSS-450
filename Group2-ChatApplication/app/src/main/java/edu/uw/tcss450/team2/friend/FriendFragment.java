package edu.uw.tcss450.team2.friend;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.uw.tcss450.team2.R;
import edu.uw.tcss450.team2.databinding.FragmentFriendBinding;

import edu.uw.tcss450.team2.R;
import edu.uw.tcss450.team2.databinding.FragmentFriendListBinding;
import edu.uw.tcss450.team2.model.UserInfoViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FriendFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FriendFragment extends Fragment {

    private FriendContactsViewModel mModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_friend, container, false);
    }

    @Override
    public void onCreate(@NonNull Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserInfoViewModel userInfoViewModel = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);
        mModel = new ViewModelProvider(getActivity()).get(FriendContactsViewModel.class);
        mModel.getContactFriend(userInfoViewModel.getJwt(), userInfoViewModel.getEmail());
    }

    @Override
    public void onViewCreated(@NonNull View view, @NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentFriendListBinding binding = FragmentFriendListBinding.bind(getView());
        mModel.addContactListObserver(getViewLifecycleOwner(), contactList -> {
            if (!contactList.isEmpty()) {
                binding.listRoot.setAdapter(
                        new FriendContactsRecyclerViewAdapter(contactList));
               binding.layoutWait.setVisibility(View.GONE);
            }
        });
    }



}