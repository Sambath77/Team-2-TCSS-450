package edu.uw.tcss450.team2.chat.createChatRoom;

import android.app.Application;
import android.graphics.drawable.Icon;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import edu.uw.tcss450.team2.R;
import edu.uw.tcss450.team2.databinding.FragmentCreateChatRoomCardBinding;
import edu.uw.tcss450.team2.databinding.FragmentFriendCardBinding;
import edu.uw.tcss450.team2.friend.FriendContacts;
import edu.uw.tcss450.team2.friend.FriendListFragmentDirections;

public class NewChatRoomUserRecyclerViewAdapter extends RecyclerView.Adapter<NewChatRoomUserRecyclerViewAdapter.ContactViewHolder> {

    private final List<UserModel> mContacts;
    private UserModel friendContacts;

    private Map<UserModel, Boolean> mEditUser;

    public NewChatRoomUserRecyclerViewAdapter(List<UserModel> mContacts, Map<UserModel, Boolean> mEditUser) {
        this.mContacts = mContacts;

        this.mEditUser = mEditUser;
    }


    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContactViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_create_chat_room_card, parent, false));
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
        FragmentCreateChatRoomCardBinding binding;
        UserModel mUser;

        public ContactViewHolder(@NonNull View view) {
            super(view);
            this.mView = view;
            binding = FragmentCreateChatRoomCardBinding.bind(view);
        }


        /*
         * method to set up the contact and display the username
         * @param: contact is the contact list
         */
        void setContact(final UserModel contact) {
            mUser = contact;
            friendContacts = contact;

            binding.editUserBtn.setOnClickListener(view -> {

                if (!mEditUser.get(mUser)) {
                    binding.editUserBtn.setImageIcon(
                            Icon.createWithResource(
                                    mView.getContext(),
                                    R.drawable.ic_action_remove_user));

                    mEditUser.put(mUser, true);
                }
                else {
                    binding.editUserBtn.setImageIcon(
                            Icon.createWithResource(
                                    mView.getContext(),
                                    R.drawable.ic_action_add_user));

                    mEditUser.put(mUser, false);
                }
                //binding.editUserBtn.setBackgroundDrawable(ContextCompat.getDrawable(mView.getContext(), R.drawable.add));
                //binding.editUserBtn.getBackgroundDrawable
                //binding.editUserBtn.getBackground().get

            });

            binding.textUsername.setText(contact.getmUsername());
            binding.editUserBtn.setImageIcon(
                    Icon.createWithResource(
                            mView.getContext(),
                            R.drawable.ic_action_add_user));
        }
    }
}
