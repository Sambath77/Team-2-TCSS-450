package edu.uw.tcss450.team2.model;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.uw.tcss450.team2.chat.ChatListViewModel;
import edu.uw.tcss450.team2.chat.ChatMessage;

public class UserInfoViewModel extends ViewModel {



    private final String mEmail;
    private final String jwt;
    private final int memberId;
    private int unreadMessageCount;
    private Map<Integer, ChatMessage> chatRoomsIdForNewMessage;
    private ChatListViewModel chatListViewModel;
    private int currentChatRoom;

    public UserInfoViewModel(String mEmail, String jwt, int memberId) {
        this.mEmail = mEmail;
        this.jwt = jwt;
        this.memberId = memberId;
        chatRoomsIdForNewMessage = new HashMap<>();
    }

    public void setCurrentChatRoom(int currentChatRoom) {
        this.currentChatRoom = currentChatRoom;
    }

    public int getCurrentChatRoom() {
        return currentChatRoom;
    }

    public ChatListViewModel getChatListViewModel() {
        return chatListViewModel;
    }

    public void setChatListViewModel(ChatListViewModel chatListViewModel) {
        this.chatListViewModel = chatListViewModel;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getJwt() {
        return jwt;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setUnreadMessageCount(int myCount) {
        this.unreadMessageCount = myCount;
    }

    public int getUnreadMessageCount() {
        return this.unreadMessageCount;
    }


    public Map<Integer, ChatMessage> getChatRoomsIdForNewMessage() {
        return chatRoomsIdForNewMessage;
    }

    public void setChatRoomsIdForNewMessage(Map<Integer, ChatMessage> chatRoomsIdForNewMessage) {
        this.chatRoomsIdForNewMessage = chatRoomsIdForNewMessage;
    }

    public static class UserInfoViewModelFactory implements ViewModelProvider.Factory {
        private final String email;
        private final String jwt;
        private final int memberId;




        public UserInfoViewModelFactory(String email, String jwt, int memberId) {
            this.email = email;
            this.jwt = jwt;
            this.memberId = memberId;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass == UserInfoViewModel.class) {
                return (T) new UserInfoViewModel(email, jwt, memberId);
            }
            throw new IllegalArgumentException(
                    "Argument must be: " + edu.uw.tcss450.team2.model.UserInfoViewModel.class);
        }
    }

}
