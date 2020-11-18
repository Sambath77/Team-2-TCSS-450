package edu.uw.tcss450.team2.friend;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.uw.tcss450.team2.R;
import edu.uw.tcss450.team2.databinding.FragmentFriendBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FriendFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FriendFragment extends Fragment {

    private FragmentFriendBinding binding;
    private FriendViewModel friendUserView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        friendUserView = new ViewModelProvider(this).get(FriendViewModel.class);
//        View root = inflater.inflate(R.layout.fragment_friend, container, false);
//        final TextView textView = root.findViewById(R.id.friend_view);
//        friendUserView.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        }) ;
        //return root;
        binding = FragmentFriendBinding.inflate(inflater, container, false);
//
//        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onCreate(@NonNull Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }



}