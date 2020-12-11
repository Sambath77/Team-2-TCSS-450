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
import edu.uw.tcss450.team2.model.UserInfoViewModel;
import edu.uw.tcss450.team2.search.AddSendRequestViewModel;
import edu.uw.tcss450.team2.search.CancelFreindRequestViewModel;


public class SendFriendRequestRecyclerViewAdapter extends RecyclerView.Adapter<SendFriendRequestRecyclerViewAdapter.SendRequestViewHolder> {

    private List<SendFriendRequest> mSendFriendRequest;
    private UserInfoViewModel userInfoViewModel;
    private AddFriendViewModel addFriendViewModel;
    private CancelFreindRequestViewModel cancelFreindRequestViewModel;


    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onCancelClick(int position);

    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
    public SendFriendRequestRecyclerViewAdapter(List<SendFriendRequest> sendFriendRequests, AddFriendViewModel addFriendViewModel,
                                                CancelFreindRequestViewModel cancelFreindRequestViewModel, UserInfoViewModel userInfoViewModel) {
        mSendFriendRequest = sendFriendRequests;
        this.addFriendViewModel = addFriendViewModel;
        this.cancelFreindRequestViewModel = cancelFreindRequestViewModel;
        this.userInfoViewModel = userInfoViewModel;
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
                if (contact.getmMemberId() > 0) {
                    addFriendViewModel.getAddFriend(userInfoViewModel.getJwt(), userInfoViewModel.getEmail(), contact.getmMemberId());
                }
            });

            binding.cancelBtnRequest.setOnClickListener(view -> {
                if (mListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        mListener.onCancelClick(position);
                    }
                }
                if (contact.getmMemberId() > 0) {
                    cancelFreindRequestViewModel.getCancelSendRequest(userInfoViewModel.getJwt(), contact.getmEmail(), userInfoViewModel.getEmail());
                }

            });
        }
    }
}


