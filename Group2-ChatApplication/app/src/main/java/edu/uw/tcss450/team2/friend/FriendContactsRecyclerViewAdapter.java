package edu.uw.tcss450.team2.friend;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.List;

import edu.uw.tcss450.team2.R;
import edu.uw.tcss450.team2.databinding.FragemntFriendCardBinding;

public class FriendContactsRecyclerViewAdapter extends RecyclerView.Adapter<FriendContactsRecyclerViewAdapter.ContactViewHolder> {

    private final List<FriendContacts> mContacts;
    private final String mFrined;

    public FriendContactsRecyclerViewAdapter(List<FriendContacts> mContacts, String mFrined) {
        this.mContacts = mContacts;
        this.mFrined = mFrined;
    }


    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContactViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragemnt_friend_card, parent, false));
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
        private FragemntFriendCardBinding binding;

        public ContactViewHolder(@NonNull View view) {
            super(view);
            this.mView = view;
            binding = FragemntFriendCardBinding.bind(view);
        }


        void setContact(final FriendContacts contact) {
//            final Resources res = mView.getContext().getResources();
//            final MaterialCardView cardView = binding.cardRoot;
            if (mFrined.equals(contact.getmUsername())) {
                binding.textUsername.setText(contact.getmUsername());
            }
        }
    }
}
