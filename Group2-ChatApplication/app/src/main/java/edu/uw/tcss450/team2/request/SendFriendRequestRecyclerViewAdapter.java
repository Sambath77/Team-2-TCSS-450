package edu.uw.tcss450.team2.request;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.tcss450.team2.R;
import edu.uw.tcss450.team2.databinding.FragmentSendFriendRequestBinding;
import edu.uw.tcss450.team2.friend.FriendContacts;
import edu.uw.tcss450.team2.friend.FriendContactsRecyclerViewAdapter;


public class SendFriendRequestRecyclerViewAdapter extends RecyclerView.Adapter<SendFriendRequestRecyclerViewAdapter.SendRequestViewHolder> {

    private List<SendFriendRequest> mSendFriendRequest;


    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onAcceptClick(int position);
        void onCancelClick(int position);

    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
    public SendFriendRequestRecyclerViewAdapter(List<SendFriendRequest> sendFriendRequests) {
        mSendFriendRequest = sendFriendRequests;
    }

    @NonNull
    @Override
    public SendRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SendFriendRequestRecyclerViewAdapter.SendRequestViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_send_friend_request, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SendRequestViewHolder holder, int position) {
        holder.setSendRequest(mSendFriendRequest.get(position));
    }

    @Override
    public int getItemCount() {
        return mSendFriendRequest.size();
    }


    public class SendRequestViewHolder extends RecyclerView.ViewHolder{

        private final View mView;
        private FragmentSendFriendRequestBinding binding;
        private SendFriendRequest mSend;
        public SendRequestViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            binding = FragmentSendFriendRequestBinding.bind(itemView);
        }

        void setSendRequest(final SendFriendRequest contact) {
            mSend = contact;
            binding.acceptTextUsername.setText(contact.getmUsername());
            binding.acceptTextEmail.setText(contact.getmEmail());
            binding.acceptBtn.setOnClickListener(view -> {
                binding.acceptTextView.setVisibility(View.VISIBLE);
                binding.cancelBtnRequest.setVisibility(View.GONE);
                binding.acceptBtn.setVisibility(View.GONE);
                if (mListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        mListener.onAcceptClick(position);
                    }
                }
//                binding.acceptTextView.setVisibility(View.VISIBLE);
//                binding.cancelBtnRequest.setVisibility(View.GONE);
//                binding.acceptBtn.setVisibility(View.GONE);
            });

            binding.cancelBtnRequest.setOnClickListener(view -> {
                if (mListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        mListener.onCancelClick(position);
                    }
                }
            });
        }
    }
}


