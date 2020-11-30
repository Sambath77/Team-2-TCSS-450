package edu.uw.tcss450.team2.chat.manageMembers;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import edu.uw.tcss450.team2.R;
import edu.uw.tcss450.team2.chat.createChatRoom.*;
import edu.uw.tcss450.team2.databinding.FragmentManageMembersBinding;
import edu.uw.tcss450.team2.model.UserInfoViewModel;


public class ManageMembersFragment extends Fragment {

    private ManageMembersViewModel mModel;
    private AddMembersViewModel mAddMemberModel;
    private Map<UserModel, Boolean> mEditUser;
    UserInfoViewModel userInfoViewModel;
    FragmentManageMembersBinding binding;
    private int chatId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userInfoViewModel = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);

        this.chatId = this.getArguments().getInt("chatId");

        mModel = new ViewModelProvider(getActivity()).get(ManageMembersViewModel.class);
        mModel.setUserInfoViewModel(userInfoViewModel);
        mModel.setChatId(chatId);

        mAddMemberModel = new ViewModelProvider(getActivity()).get(AddMembersViewModel.class);
        mAddMemberModel.setUserInfoViewModel(userInfoViewModel);
        mAddMemberModel.setChatId(chatId);

        mModel.setAddMembersViewModel(mAddMemberModel);
        mAddMemberModel.setManageMembersViewModel(mModel);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding = FragmentManageMembersBinding.bind(getView());

        mModel.setBinding(binding);
        mModel.getMembers(userInfoViewModel.getJwt(), chatId);

        mAddMemberModel.setBinding(binding);

        try {
            mAddMemberModel.getMembers(userInfoViewModel.getJwt(), chatId, userInfoViewModel.getMemberId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mModel.addContactListObserver(getViewLifecycleOwner(), contactList -> {
            if (contactList != null) {
                mEditUser = contactList.stream()
                        .collect(Collectors.toMap(Function.identity(), blog -> false));
                binding.membersList.setAdapter(
                        new ManageMembersRecyclerViewAdapter(contactList, mEditUser,
                                mModel, userInfoViewModel, chatId, mAddMemberModel));
            }
        });

        mAddMemberModel.addContactListObserver(getViewLifecycleOwner(), contactList -> {
            if (contactList != null) {
                mEditUser = contactList.stream()
                        .collect(Collectors.toMap(Function.identity(), blog -> false));
                binding.newMembersList.setAdapter(
                        new AddMembersRecyclerViewAdapter(contactList, mEditUser, mAddMemberModel,
                                userInfoViewModel, chatId, mModel));
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_manage_members, container, false);
    }
}