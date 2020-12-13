package edu.uw.tcss450.team2.chat;

import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import edu.uw.tcss450.team2.R;
import edu.uw.tcss450.team2.databinding.FragmentChatCardBinding;

/**
 *
 */
public class ChatListRecyclerViewAdapter extends RecyclerView.Adapter<ChatListRecyclerViewAdapter.ChatViewHolder> {

    private final Map<ChatRoomModel, Boolean> mExpandedFlags;

    //Store all of the chat rooms to present
    private List<ChatRoomModel> chatRooms;

    private List<Integer> chatRoomsIdForNewMessage;

    public ChatListRecyclerViewAdapter(List<ChatRoomModel> items, List<Integer> chatRoomsId) {

        this.chatRooms = items;

        mExpandedFlags = chatRooms.stream()
                .collect(Collectors.toMap(Function.identity(), user -> false));

        this.chatRoomsIdForNewMessage = chatRoomsId;
        sortChatRoomsBasedOnTime();
    }

    private void sortChatRoomsBasedOnTime() {
        for(int i = 0; i < chatRooms.size(); i ++) {
            if(chatRoomsIdForNewMessage.contains(new Integer(chatRooms.get(i).getChatId()))) {
                ChatRoomModel temp = chatRooms.get(i);
                chatRooms.remove(i);
                chatRooms.add(0, temp);
            }
        }


    }


    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_chat_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        holder.setUser(chatRooms.get(position));
    }


    @Override
    public int getItemCount() {
        return chatRooms.size();
    }

    /**
     * Objects from this class represent an Individual row View from the List
     * of rows in the Blog Recycler View.
     */
    public class ChatViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public FragmentChatCardBinding binding;
        private ChatRoomModel mChatRoom;
        public ChatViewHolder(View view) {
            super(view);
            mView = view;

            binding = FragmentChatCardBinding.bind(view);

        }


        void setUser(final ChatRoomModel chatRoomModel) {
            mChatRoom = chatRoomModel;


            binding.buttonFullPost.setOnClickListener(view ->
            {
                Navigation.findNavController(mView).navigate(
                        ChatListFragmentDirections
                                .actionNavigationChatToPersonalChatFragment(chatRoomModel.getChatId()));
                chatRoomsIdForNewMessage.remove(new Integer(mChatRoom.getChatId()));
            });

            binding.textPubdate.setText("Room Id: " + chatRoomModel.getChatId() + "");
            if(chatRoomsIdForNewMessage.contains(mChatRoom.getChatId())) {
                binding.textPubdate.setBackgroundColor(Color.BLACK);
            }

            binding.textTitle.setText(chatRoomModel.getRoomName());

            binding.buittonMore.setImageIcon(
                    Icon.createWithResource(
                            mView.getContext(),
                            R.drawable.ic_member));



            binding.buittonMore.setOnClickListener(view ->
            {
                /*
                Navigation.findNavController(mView).navigate(
                        ChatListFragmentDirections
                                .actionNavigationChatToChatRoomMemberFragment(chatRoomModel.getChatId()));
                 */
                Navigation.findNavController(mView).navigate(
                        ChatListFragmentDirections
                                .actionNavigationChatToManageMembersFragment(chatRoomModel.getChatId()));

            });
        }

    }

}
