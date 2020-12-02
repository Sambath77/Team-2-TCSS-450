package edu.uw.tcss450.team2.friend;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONObject;

import edu.uw.tcss450.team2.R;
import edu.uw.tcss450.team2.databinding.FragmentFriendCardBinding;
import edu.uw.tcss450.team2.databinding.FragmentFriendListBinding;
import edu.uw.tcss450.team2.model.UserInfoViewModel;


public class FriendListFragment extends Fragment {

    private FriendContactsViewModel mModel;
    private MutableLiveData<JSONObject> mResponse;
    private FriendContactsRecyclerViewAdapter mFriendContactsRecyclerViewAdapter;
    private FriendContacts mContacts;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_friend_list, container, false);
    }

    @Override
    public void onCreate(@NonNull Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserInfoViewModel userInfoViewModel = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);

        mResponse = new MutableLiveData<>();
        mResponse.setValue(new JSONObject());

        mModel = new ViewModelProvider(getActivity()).get(FriendContactsViewModel.class);

        mModel.getContactFriend(userInfoViewModel.getJwt(), userInfoViewModel.getEmail());


    }

    @Override
    public void onViewCreated(@NonNull View view, @NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentFriendListBinding binding = FragmentFriendListBinding.bind(getView());

        mModel.addContactListObserver(getViewLifecycleOwner(), contactList -> {
            mFriendContactsRecyclerViewAdapter = new FriendContactsRecyclerViewAdapter(contactList);

            if (!contactList.isEmpty()) {
                binding.listRoot.setAdapter(mFriendContactsRecyclerViewAdapter);
                binding.layoutWait.setVisibility(View.GONE);

                mFriendContactsRecyclerViewAdapter.setOnItemClickListener(new FriendContactsRecyclerViewAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        String str = contactList.get(position).getmFName() + " " + contactList.get(position).getmLName();
                        contactList.get(position).changedUsername(str);
                        mFriendContactsRecyclerViewAdapter.notifyItemChanged(position);
                    }

                    @Override
                    public void onDeleteClick(int position) {
                        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
                        builder.setTitle("Remove Friend");
                        builder.setMessage("Do you want to remove your friend?");
                        builder.setIcon(R.drawable.ic_baseline_error_24);
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                contactList.remove(position);
                                mFriendContactsRecyclerViewAdapter.notifyItemRemoved(position);
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getContext(), "Cancel", Toast.LENGTH_SHORT).show();
                            }
                        });
                        builder.show();
                    }
                });

            }
        });
    }


}