package edu.uw.tcss450.team2.chat;

import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import edu.uw.tcss450.team2.R;
import edu.uw.tcss450.team2.chat.createChatRoom.UserModel;
import edu.uw.tcss450.team2.databinding.FragmentChatCardBinding;
import edu.uw.tcss450.team2.databinding.FragmentChatListBinding;
import edu.uw.tcss450.team2.io.RequestQueueSingleton;
import edu.uw.tcss450.team2.model.UserInfoViewModel;

/**
 *
 */
public class ChatListRecyclerViewAdapter extends RecyclerView.Adapter<ChatListRecyclerViewAdapter.ChatViewHolder> {

    private final Map<ChatRoomModel, Boolean> mExpandedFlags;

    //Store all of the chat rooms to present
    private List<ChatRoomModel> chatRooms;

    private Map<Integer, ChatMessage> chatRoomsIdForNewMessage;

    private UserInfoViewModel userInfoViewModel;

    private FragmentChatListBinding binding;

    private ChatListViewModel chatListViewModel;

    public ChatListRecyclerViewAdapter(List<ChatRoomModel> items, Map<Integer,
            ChatMessage> chatRoomsId, UserInfoViewModel userInfoViewModel, FragmentChatListBinding binding,
                                       ChatListViewModel chatListViewModel) {

        this.binding = binding;
        this.chatListViewModel = chatListViewModel;

        this.userInfoViewModel = userInfoViewModel;
        this.chatRooms = items;

        mExpandedFlags = chatRooms.stream()
                .collect(Collectors.toMap(Function.identity(), user -> false));

        this.chatRoomsIdForNewMessage = chatRoomsId;
        sortChatRoomsBasedOnTime();
    }

    private void sortChatRoomsBasedOnTime() {
        for(int i = 0; i < chatRooms.size(); i ++) {
            if(chatRoomsIdForNewMessage.containsKey(new Integer(chatRooms.get(i).getChatId()))) {
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
                userInfoViewModel.setCurrentChatRoom(mChatRoom.getChatId());
                chatRoomsIdForNewMessage.remove(new Integer(mChatRoom.getChatId()));
            });

            //binding.textPubdate.setText("Room Id: " + chatRoomModel.getChatId() + "");
            binding.textPubdate.setText(chatRoomModel.getRoomName());

            if(chatRoomsIdForNewMessage.containsKey(mChatRoom.getChatId())) {
                ChatMessage chatMessage = chatRoomsIdForNewMessage.get(new Integer(mChatRoom.getChatId()));

                if(chatMessage.getMessage().length() <= 36) {
                    binding.textTitle.setText(chatMessage.getSender() + ": " + chatMessage.getMessage());
                }
                else {
                    binding.textTitle.setText(chatMessage.getSender() + ": " + chatMessage.getMessage().substring(0, 42) + "...");
                }

                binding.dateReceived.setText(getTimeReceived(chatMessage.getDateReceived(), new Date()));
            }

            binding.deleteRoom.setImageIcon(
                    Icon.createWithResource(
                            mView.getContext(),
                            R.drawable.ic_baseline_close_24));

            binding.deleteRoom.setOnClickListener(view -> {
                chatListViewModel.deleteChatRoom(userInfoViewModel.getJwt(), chatRoomModel.getChatId());
            });

            binding.buittonMore.setImageIcon(
                    Icon.createWithResource(
                            mView.getContext(),
                            R.drawable.ic_member));


            binding.buittonMore.setOnClickListener(view ->
            {

                Navigation.findNavController(mView).navigate(
                        ChatListFragmentDirections
                                .actionNavigationChatToManageMembersFragment(chatRoomModel.getChatId()));

            });
        }

    }

    private String getTimeReceived(Date date1, Date date2) {
        long duration  = date2.getTime() - date1.getTime();
        long diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(duration);
        long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(duration);
        long diffInHours = TimeUnit.MILLISECONDS.toHours(duration);
        long diffInDays = TimeUnit.MILLISECONDS.toDays(duration);

        if(diffInDays > 0)
            return diffInDays + " day(s) ago";
        if(diffInHours > 0)
            return diffInHours + " hr(s) ago";
        if(diffInMinutes > 0)
            return diffInMinutes + " minute(s) ago";

        return diffInSeconds + " second(s) ago";

    }


}
