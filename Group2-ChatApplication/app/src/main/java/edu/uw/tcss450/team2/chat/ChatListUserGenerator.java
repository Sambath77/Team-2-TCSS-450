package edu.uw.tcss450.team2.chat;

import java.util.Arrays;
import java.util.List;

public class ChatListUserGenerator {

    private static final ChatListUserModel[] CHAT_LIST_USER_MODELS;
    public static final int COUNT = 20;


    static {
        CHAT_LIST_USER_MODELS = new ChatListUserModel[COUNT];
        for (int i = 0; i < CHAT_LIST_USER_MODELS.length; i++) {
            ChatListUserModel chatListUserModel = new ChatListUserModel();
            chatListUserModel.setUserName("sambath777");
            chatListUserModel.setBriefMessage("Hey, pls dont forget to ...");

            CHAT_LIST_USER_MODELS[i] = chatListUserModel;
        }
    }

    public static List<ChatListUserModel> getUserList() {
        return Arrays.asList(CHAT_LIST_USER_MODELS);
    }

    public static ChatListUserModel[] getUSERS() {
        return Arrays.copyOf(CHAT_LIST_USER_MODELS, CHAT_LIST_USER_MODELS.length);
    }

    private ChatListUserGenerator() { }

}
