package edu.uw.tcss450.team2.friend;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import edu.uw.tcss450.team2.R;
import edu.uw.tcss450.team2.databinding.FragmentFriendListBinding;
import edu.uw.tcss450.team2.model.UserInfoViewModel;


public class FriendListFragment extends Fragment {

    private FriendContactsViewModel mModel;
    private MutableLiveData<JSONObject> mResponse;
    private FriendContactsRecyclerViewAdapter mFriendContactsRecyclerViewAdapter;
    private FriendContacts mContacts;
    private DeleteFriendViewModel mDelete;
    private UserInfoViewModel userInfoViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_friend_list, container, false);
    }

    @Override
    public void onCreate(@NonNull Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userInfoViewModel = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);

        mResponse = new MutableLiveData<>();
        mResponse.setValue(new JSONObject());

        mModel = new ViewModelProvider(getActivity()).get(FriendContactsViewModel.class);

        mModel.getContactFriend(userInfoViewModel.getJwt(), userInfoViewModel.getEmail());

        mDelete = new ViewModelProvider(getActivity()).get(DeleteFriendViewModel.class);



    }

    @Override
    public void onViewCreated(@NonNull View view, @NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentFriendListBinding binding = FragmentFriendListBinding.bind(getView());

        mModel.addContactListObserver(getViewLifecycleOwner(), contactList -> {
            mFriendContactsRecyclerViewAdapter = new FriendContactsRecyclerViewAdapter(contactList);

            if (!contactList.isEmpty()) {
                if (TextUtils.isEmpty(binding.seachContacts.getText())) {
                    binding.layoutWait.setVisibility(View.GONE);
                }
//                binding.listRoot.setAdapter(mFriendContactsRecyclerViewAdapter);
//                binding.layoutWait.setVisibility(View.GONE);

                binding.seachContacts.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (!TextUtils.isEmpty(binding.seachContacts.getText())) {
                            binding.listRoot.setVisibility(View.VISIBLE);
                            binding.listRoot.setAdapter(mFriendContactsRecyclerViewAdapter);
                            binding.layoutWait.setVisibility(View.GONE);
                        }
                        List<FriendContacts> filteredList = new ArrayList<>();
                        for (FriendContacts item : contactList) {
                            if (item.getmUsername().toLowerCase().contains(s.toString().toLowerCase())) {
                                filteredList.add(item);
                            }
                        }
                        mFriendContactsRecyclerViewAdapter.filterList(filteredList);
                    }
                });

                binding.btnCancelBtn.setOnClickListener(button -> {
                    binding.seachContacts.setText("");
                    binding.listRoot.setVisibility(View.GONE);
                });

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
                                mDelete.deleteFriend(userInfoViewModel.getJwt(), userInfoViewModel.getEmail(), contactList.get(position).getEmail());
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

            } else {
               // binding.listRoot.setAdapter();
            }
        });
    }


}