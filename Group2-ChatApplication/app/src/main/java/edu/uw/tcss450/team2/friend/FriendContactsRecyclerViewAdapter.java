package edu.uw.tcss450.team2.friend;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import edu.uw.tcss450.team2.R;
import edu.uw.tcss450.team2.databinding.FragmentFriendCardBinding;
import edu.uw.tcss450.team2.model.UserInfoViewModel;

public class FriendContactsRecyclerViewAdapter extends RecyclerView.Adapter<FriendContactsRecyclerViewAdapter.ContactViewHolder> {

    private List<FriendContacts> mContacts;
    private FriendContacts friendContacts;
    private DeleteFriendViewModel mDeleteModel;



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
        //holder.getMemberID(mContacts.get(position));
    }

    @Override
    public int getItemCount() {
        return mContacts.size();
    }

    public void filterList(List<FriendContacts> filteredList) {
        mContacts = filteredList;
        notifyDataSetChanged();
    }


    class ContactViewHolder extends RecyclerView.ViewHolder {

        private final View mView;
        private @NonNull
        FragmentFriendCardBinding binding;
        private UserInfoViewModel model;

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

//            binding.deleteButton.setOnClickListener(v -> {
//                if (mListener != null) {
//                    int position = getAdapterPosition();
//                    if (position != RecyclerView.NO_POSITION) {
//                        mListener.onDeleteClick(position);
//                    }
//                }
//            });
        }



        /*
         * method to set up the contact and display the username
         * @param: contact is the contact list
         */
        void setContact(final FriendContacts contact) {


            binding.deleteButton.setOnClickListener(v -> {
                if (mListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        mListener.onDeleteClick(position);
                    }
                }
            });

            binding.textUsername.setText(contact.getmUsername());
            binding.textEmail.setText(contact.getEmail());
        }


    }
}
