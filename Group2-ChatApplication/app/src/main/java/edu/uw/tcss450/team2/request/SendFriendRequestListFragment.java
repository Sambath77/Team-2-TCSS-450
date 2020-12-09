package edu.uw.tcss450.team2.request;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import android.view.View;

import org.json.JSONObject;

import java.util.List;

import edu.uw.tcss450.team2.databinding.FragmentSendFriendRequestListBinding;
import edu.uw.tcss450.team2.model.UserInfoViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SendFriendRequestListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SendFriendRequestListFragment extends Fragment {

    private SendFriendRequestViewModel mModel;
    private MutableLiveData<JSONObject> listMutableLiveData;
    private List<SendFriendRequest> sendFriendRequests;
    private SendFriendRequestRecyclerViewAdapter sendFriendRequestRecyclerViewAdapter;
    private UserInfoViewModel userInfoViewModel;
    private AddFriendViewModel addFriendViewModel;

    @Override
    public void onCreate(@NonNull Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listMutableLiveData = new MutableLiveData<>();
        listMutableLiveData.setValue(new JSONObject());

        userInfoViewModel = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);
        mModel = new ViewModelProvider(getActivity()).get(SendFriendRequestViewModel.class);
        mModel.getSendRequest(userInfoViewModel.getJwt(), userInfoViewModel.getEmail());

        addFriendViewModel = new ViewModelProvider(getActivity()).get(AddFriendViewModel.class);


    }

    @Override
    public void onViewCreated(@NonNull View view, @NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentSendFriendRequestListBinding binding = FragmentSendFriendRequestListBinding.bind(getView());

        mModel.sendRequestObserver(getViewLifecycleOwner(), requestFriend -> {
            sendFriendRequestRecyclerViewAdapter = new SendFriendRequestRecyclerViewAdapter(requestFriend);
            if (!requestFriend.isEmpty()) {
                binding.acceptRecyclerView.setAdapter(sendFriendRequestRecyclerViewAdapter);
                binding.acceptRecyclerViewWait.setVisibility(View.GONE);

                sendFriendRequestRecyclerViewAdapter.setOnItemClickListener(new SendFriendRequestRecyclerViewAdapter.OnItemClickListener() {
                    @Override
                    public void onAcceptClick(int position) {
                        addFriendViewModel.getAddFriend(userInfoViewModel.getJwt(), userInfoViewModel.getEmail(), requestFriend.get(position).getmMemberId());
                    }

                    @Override
                    public void onCancelClick(int position) {

                    }
                });
            }
        });
    }
}