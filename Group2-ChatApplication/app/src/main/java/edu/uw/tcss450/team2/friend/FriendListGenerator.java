package edu.uw.tcss450.team2.friend;

import java.util.Arrays;
import java.util.List;

import edu.uw.tcss450.team2.chat.ChatListUserModel;

public class FriendListGenerator {

    private static final FriendListUserModel[] FRIEND_LIST_USER_MODELS;
    public static final int COUNT = 20;


    static {
        FRIEND_LIST_USER_MODELS = new FriendListUserModel[COUNT];
        for (int i = 0; i < FRIEND_LIST_USER_MODELS.length; i++) {
            FriendListUserModel friendListUserModel = new FriendListUserModel();
            friendListUserModel.setUserName("sambath77");
            friendListUserModel.setFriendFirstName("sambath");
            friendListUserModel.setFriendLastName("pech");
            friendListUserModel.setFriendEmail("bath1205@uw.edu");

            FRIEND_LIST_USER_MODELS[i] = friendListUserModel;
        }
    }

    public static List<FriendListUserModel> getFriendList() {
        return Arrays.asList(FRIEND_LIST_USER_MODELS);
    }

    public static FriendListUserModel[] getFriend() {
        return Arrays.copyOf(FRIEND_LIST_USER_MODELS, FRIEND_LIST_USER_MODELS.length);
    }

    private FriendListGenerator() { }
}
