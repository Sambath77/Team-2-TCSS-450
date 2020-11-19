package edu.uw.tcss450.team2.chat;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.List;

public class ChatViewModel extends AndroidViewModel {

    private MutableLiveData<List<ChatListUserModel>> mUsers;

    public ChatViewModel(@NonNull Application application) {
        super(application);
        mUsers = new MutableLiveData<>();
        //mBlogList.setValue(new ArrayList<>());
        mUsers.setValue(ChatListUserGenerator.getUserList());

    }
    public void addUserListObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super List<ChatListUserModel>> observer) {
        mUsers.observe(owner, observer);
    }

}
