package edu.uw.tcss450.team2.friend;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.tcss450.team2.R;
import edu.uw.tcss450.team2.databinding.FragmentFriendCardBinding;

public class FriendContactsRecyclerViewAdapter extends RecyclerView.Adapter<FriendContactsRecyclerViewAdapter.ContactViewHolder> {

    private final List<FriendContacts> mContacts;
    private FriendContacts friendContacts;

    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public FriendContactsRecyclerViewAdapter(List<FriendContacts> mContacts) {
        this.mContacts = mContacts;


    }


    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContactViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_friend_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        holder.setContact(mContacts.get(position));
        holder.getMemberID(mContacts.get(position));
    }

    @Override
    public int getItemCount() {
        return mContacts.size();
    }


    class ContactViewHolder extends RecyclerView.ViewHolder {

        private final View mView;
        private @NonNull
        FragmentFriendCardBinding binding;

        public ContactViewHolder(@NonNull View view) {
            super(view);
            this.mView = view;
            binding = FragmentFriendCardBinding.bind(view);

//            view.setOnClickListener(v -> {
//                Navigation.findNavController(view).navigate(FriendListFragmentDirections
//                .actionNavigationContactToFriend(contact));
//            });

            view.setOnClickListener(v ->  {
                if (mListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        mListener.onItemClick(position);
                    }
                }
            });

            binding.deleteButton.setOnClickListener(v -> {
                if (mListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        mListener.onDeleteClick(position);
                    }
                }
            });
        }

//        private DeleteModel;

        /*
         * method to set up the contact and display the username
         * @param: contact is the contact list
         */
        void setContact(final FriendContacts contact) {
//            contact.getMemberId();
//            DeleteModel.delete(getMemberID())
//            deletebuton.setOnLick
//
//
//            {
//                this.model.delete(contact.getMemberId(), )
//            }
            //friendContacts = contact;

//            binding.buttonFullView.setOnClickListener(view -> {
//                        Navigation.findNavController(mView).navigate(FriendListFragmentDirections
//                                .actionNavigationContactToFriend(contact));
//                    }
//            );
//            mView.setOnClickListener(v -> {
//                Navigation.findNavController(mView).navigate(FriendListFragmentDirections
//                        .actionFriendListFragmentToFriendPostFragment(contact));
//            });
//            mView.setOnClickListener(v -> {
//                if (mListener != null) {
//                    int position = getAdapterPosition();
//                    if (position != RecyclerView.NO_POSITION) {
//                        mListener.onItemClick(position);
//                    }
//                }
//            });
            binding.textUsername.setText(contact.getmUsername());
            binding.textEmail.setText(contact.getEmail());
        }

        int getMemberID(final FriendContacts contacts) {
            return contacts.getMemberId();
        }
    }
}
