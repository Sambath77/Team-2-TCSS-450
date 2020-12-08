package edu.uw.tcss450.team2.notification;

import android.graphics.drawable.Icon;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.tcss450.team2.R;
import edu.uw.tcss450.team2.chat.createChatRoom.UserModel;
import edu.uw.tcss450.team2.databinding.FragmentCreateChatRoomCardBinding;
import edu.uw.tcss450.team2.databinding.FragmentNotificationCardBinding;

public class NotificationRecyclerViewAdapter extends RecyclerView.Adapter<NotificationRecyclerViewAdapter.NotificationViewHolder> {

    private final List<String> mMessages;
    //private UserModel friendContacts;

    //private Map<UserModel, Boolean> mEditUser;

    public NotificationRecyclerViewAdapter(List<String> mMessages) {
        this.mMessages = mMessages;
    }


    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotificationViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_notification_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        holder.setContact(mMessages.get(position));
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }


    class NotificationViewHolder extends RecyclerView.ViewHolder {

        private final View mView;
        private @NonNull
        FragmentNotificationCardBinding binding;
        //UserModel mUser;

        public NotificationViewHolder(@NonNull View view) {
            super(view);
            this.mView = view;
            binding = FragmentNotificationCardBinding.bind(view);
        }


        /*
         * method to set up the contact and display the username
         * @param: contact is the contact list
         */
        void setContact(final String message) {
            //mUser = contact;
            //friendContacts = contact;
            binding.txtMessage.setText(message);
        }
    }
}
