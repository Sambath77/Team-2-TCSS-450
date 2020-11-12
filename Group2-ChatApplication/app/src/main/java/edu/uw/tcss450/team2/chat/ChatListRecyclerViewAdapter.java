package edu.uw.tcss450.team2.chat;

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

public class ChatListRecyclerViewAdapter extends RecyclerView.Adapter<ChatListRecyclerViewAdapter.ChatViewHolder> {

    private final Map<ChatRoomModel, Boolean> mExpandedFlags;

    //Store all of the blogs to present
    private final List<ChatRoomModel> chatRooms;

    public ChatListRecyclerViewAdapter(List<ChatRoomModel> items) {

        this.chatRooms = items;
        mExpandedFlags = chatRooms.stream()
                .collect(Collectors.toMap(Function.identity(), user -> false));

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
            binding.buittonMore.setOnClickListener(this::handleMoreOrLess);
        }
        /**
         * When the button is clicked in the more state, expand the card to display
         * the blog preview and switch the icon to the less state. When the button
         * is clicked in the less state, shrink the card and switch the icon to the
         * more state.
         * @param button the button that was clicked
         */
        private void handleMoreOrLess(final View button) {
            mExpandedFlags.put(mChatRoom, !mExpandedFlags.get(mChatRoom));
            displayPreview();
        }
        /**
         * Helper used to determine if the preview should be displayed or not.
         */
        private void displayPreview() {
            /*
            if (mExpandedFlags.get(mBlog)) {
                if (binding.textPreview.getVisibility() == View.GONE) {
                    binding.textPreview.setVisibility(View.VISIBLE);
                    binding.buittonMore.setImageIcon(
                            Icon.createWithResource(
                                    mView.getContext(),
                                    R.drawable.ic_less_grey_24dp));
                } else {
                    binding.textPreview.setVisibility(View.GONE);
                    binding.buittonMore.setImageIcon(
                            Icon.createWithResource(
                                    mView.getContext(),
                                    R.drawable.ic_more_grey_24dp));
                }
            }
             */
        }

        void setUser(final ChatRoomModel chatRoomModel) {
            mChatRoom = chatRoomModel;


            binding.buttonFullPost.setOnClickListener(view ->
            {
                Navigation.findNavController(mView).navigate(
                        ChatListFragmentDirections
                                .actionNavigationChatToPersonalChatFragment(chatRoomModel.getChatId()));

            });

            //binding.textPubdate.setText(chatRoomModel.getChatId());
            //binding.textTitle.setText(chatRoomModel.getRoomName());
            //binding.textPubdate.setText(user.getPubDate());
            //Use methods in the HTML class to format the HTML found in the text
            //final String preview = Html.fromHtml(user.getBriefMessage()).toString();
            //binding.textPreview.setText(preview);
            displayPreview();
        }

    }

}
