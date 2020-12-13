package edu.uw.tcss450.team2.search;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.tcss450.team2.R;
import edu.uw.tcss450.team2.databinding.FragmentSendFriendRequestBinding;
import edu.uw.tcss450.team2.model.UserInfoViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SendFriendRequestListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SendFriendRequestListFragment extends Fragment {
    private AddFriendViewModel addFriendViewModel;
    private UserInfoViewModel userInfoViewModel;
    private SearchContactsViewModel searchContactsViewModel;
    private SearchContactsRecyclerViewAdapter searchContactsRecyclerViewAdapter;

    @Override
    public void onCreate(@NonNull Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        userInfoViewModel = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);
        searchContactsViewModel = new ViewModelProvider(getActivity()).get(SearchContactsViewModel.class);
        addFriendViewModel = new ViewModelProvider(getActivity()).get(AddFriendViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentSendFriendRequestBinding binding = FragmentSendFriendRequestBinding.bind(getView());
        searchContactsViewModel.addSearchContactsObserver(getViewLifecycleOwner(), searchContacts -> {
            searchContactsRecyclerViewAdapter = new SearchContactsRecyclerViewAdapter(searchContacts);
            if (!searchContacts.isEmpty()) {

            }
        });
    }
}