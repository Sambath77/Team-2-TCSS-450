package edu.uw.tcss450.team2.friend;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.tcss450.team2.R;
import edu.uw.tcss450.team2.databinding.FragmentFriendPostBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FriendPostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FriendPostFragment extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_friend_post, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FriendPostFragmentArgs args = FriendPostFragmentArgs.fromBundle(getArguments());
        FragmentFriendPostBinding binding = FragmentFriendPostBinding.bind(getView());
        String s = args.getContactList().getmFName() + " " + args.getContactList().getmLName();
        binding.profileTextUsername.setText(s);

        binding.profileTextEmail.setText(args.getContactList().getEmail());

//        binding.sendButton.setOnClickListener(button -> {
//
//                Navigation.findNavController(getView()).navigate(FriendPostFragmentDirections
//                        .actionFragmentFriendPostToFriendChatFragment());
//
//        });

    }
}