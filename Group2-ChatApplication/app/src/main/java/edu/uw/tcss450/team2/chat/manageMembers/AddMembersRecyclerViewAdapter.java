package edu.uw.tcss450.team2.chat.manageMembers;

import android.graphics.drawable.Icon;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;

import java.util.List;
import java.util.Map;

import edu.uw.tcss450.team2.R;
import edu.uw.tcss450.team2.chat.createChatRoom.UserModel;
import edu.uw.tcss450.team2.databinding.FragmentAddMemberCardBinding;
import edu.uw.tcss450.team2.model.UserInfoViewModel;

public class AddMembersRecyclerViewAdapter extends RecyclerView.Adapter<AddMembersRecyclerViewAdapter.ContactViewHolder> {

    private final List<UserModel> mContacts;
    private UserModel friendContacts;
    private AddMembersViewModel mModel;
    private Map<UserModel, Boolean> mEditUser;
    private UserInfoViewModel userInfoViewModel;
    private int chatId;
    private ManageMembersViewModel manageMembersViewModel;

    public AddMembersRecyclerViewAdapter(List<UserModel> mContacts, Map<UserModel, Boolean> mEditUser,
                                         AddMembersViewModel mModel, UserInfoViewModel userInfoViewModel,
                                         int chatId, ManageMembersViewModel manageMembersViewModel) {
        this.mContacts = mContacts;
        this.mEditUser = mEditUser;
        this.mModel = mModel;
        this.userInfoViewModel = userInfoViewModel;
        this.chatId = chatId;
        this.manageMembersViewModel = manageMembersViewModel;
    }


    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContactViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_add_member_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        holder.setContact(mContacts.get(position));
    }

    @Override
    public int getItemCount() {
        return mContacts.size();
    }


    class ContactViewHolder extends RecyclerView.ViewHolder {

        private final View mView;
        private @NonNull
        FragmentAddMemberCardBinding binding;
        UserModel mUser;

        public ContactViewHolder(@NonNull View view) {
            super(view);
            this.mView = view;
            binding = FragmentAddMemberCardBinding.bind(view);
        }


        /*
         * method to set up the contact and display the username
         * @param: contact is the contact list
         */
        void setContact(final UserModel contact) {
            mUser = contact;
            friendContacts = contact;

            binding.editUserBtn.setImageIcon(
                    Icon.createWithResource(
                            mView.getContext(),
                            R.drawable.ic_action_add_user));

            binding.editUserBtn.setOnClickListener(view -> {
                try {
                    mModel.addMember(userInfoViewModel.getJwt(), chatId, contact.getMemberId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            binding.textUsername.setText(contact.getmUsername());

        }
    }
}
