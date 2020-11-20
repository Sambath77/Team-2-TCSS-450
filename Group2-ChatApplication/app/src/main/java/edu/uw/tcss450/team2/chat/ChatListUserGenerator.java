package edu.uw.tcss450.team2.chat;

import java.util.Arrays;
import java.util.List;

/**
 *
 */
public class ChatListUserGenerator {

    private static final ChatRoomModel[] CHAT_LIST_USER_MODELS;
    public static final int COUNT = 20;


    static {
        CHAT_LIST_USER_MODELS = new ChatRoomModel[COUNT];
        for (int i = 0; i < CHAT_LIST_USER_MODELS.length; i++) {
            ChatRoomModel chatRoomModel = new ChatRoomModel();
            chatRoomModel.setChatId(1);
            chatRoomModel.setRoomName("Chat room 1");

            CHAT_LIST_USER_MODELS[i] = chatRoomModel;
        }
    }

    public static List<ChatRoomModel> getUserList() {
        return Arrays.asList(CHAT_LIST_USER_MODELS);
    }

    public static ChatRoomModel[] getUSERS() {
        return Arrays.copyOf(CHAT_LIST_USER_MODELS, CHAT_LIST_USER_MODELS.length);
    }

    private ChatListUserGenerator() { }

}
