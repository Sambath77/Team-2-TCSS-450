package edu.uw.tcss450.team2.friend;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class FriendViewModel extends AndroidViewModel {

    private MutableLiveData<List<FriendListUserModel>> mFriend;


    public FriendViewModel(@NonNull Application application) {
        super(application);
        mFriend = new MutableLiveData<>();
        mFriend.setValue(FriendListGenerator.getFriendList());
    }

    public void addFriendListObserver(@NonNull LifecycleOwner owner, @NonNull Observer<? super List<FriendListUserModel>> observer) {
        mFriend.observe(owner, observer);
    }
}
