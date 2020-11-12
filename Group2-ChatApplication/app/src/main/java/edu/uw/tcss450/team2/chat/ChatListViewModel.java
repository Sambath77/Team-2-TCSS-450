package edu.uw.tcss450.team2.chat;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.List;

public class ChatListViewModel extends AndroidViewModel {

    private MutableLiveData<List<ChatRoomModel>> mUsers;

    public ChatListViewModel(@NonNull Application application) {
        super(application);
        mUsers = new MutableLiveData<>();
        mUsers.setValue(ChatListUserGenerator.getUserList());

    }
    public void addUserListObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super List<ChatRoomModel>> observer) {
        mUsers.observe(owner, observer);
    }

}
