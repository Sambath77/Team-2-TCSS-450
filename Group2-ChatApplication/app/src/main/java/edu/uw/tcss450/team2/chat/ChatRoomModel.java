package edu.uw.tcss450.team2.chat;

import java.util.List;

/**
 *
 */
public class ChatRoomModel {

    private int chatId;
    private String roomName;

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

    private List<String> members;

    public int getChatId() {
        return chatId;
    }

    public void setChatId(int chatId) {
        this.chatId = chatId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

}
