package edu.uw.tcss450.team2.request;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONObject;

import java.util.List;

import edu.uw.tcss450.team2.R;
import edu.uw.tcss450.team2.databinding.FragmentSendFriendRequestListBinding;
import edu.uw.tcss450.team2.model.UserInfoViewModel;
import edu.uw.tcss450.team2.search.CancelFreindRequestViewModel;

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
    private CancelFreindRequestViewModel cancelFreindRequestViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_send_friend_request_list, container, false);
    }

    @Override
    public void onCreate(@NonNull Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listMutableLiveData = new MutableLiveData<>();
        listMutableLiveData.setValue(new JSONObject());

        userInfoViewModel = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);
        mModel = new ViewModelProvider(getActivity()).get(SendFriendRequestViewModel.class);
        mModel.getSendRequest(userInfoViewModel.getJwt(), userInfoViewModel.getEmail());

        addFriendViewModel = new ViewModelProvider(getActivity()).get(AddFriendViewModel.class);
        cancelFreindRequestViewModel = new ViewModelProvider(getActivity()).get(CancelFreindRequestViewModel.class);


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

                        try {
                            Thread.sleep(10);
                            cancelFreindRequestViewModel.getCancelSendRequest(userInfoViewModel.getJwt(), requestFriend.get(position).getmEmail(), userInfoViewModel.getEmail());
                            requestFriend.remove(position);
                            sendFriendRequestRecyclerViewAdapter.notifyItemRemoved(position);
                        } catch (InterruptedException e) {

                        }
//                        cancelFreindRequestViewModel.getCancelSendRequest(userInfoViewModel.getJwt(), requestFriend.get(position).getmEmail(), userInfoViewModel.getEmail());
//                        requestFriend.remove(position);
//                        sendFriendRequestRecyclerViewAdapter.notifyItemRemoved(position);
                    }

                    @Override
                    public void onCancelClick(int position) {
                        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
                        builder.setTitle("Delete Request");
                        builder.setMessage("Do you want to remove your friend request?");
                        builder.setIcon(R.drawable.ic_baseline_error_24);
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                cancelFreindRequestViewModel.getCancelSendRequest(userInfoViewModel.getJwt(), requestFriend.get(position).getmEmail(), userInfoViewModel.getEmail());
                                requestFriend.remove(position);
                                sendFriendRequestRecyclerViewAdapter.notifyItemRemoved(position);
                                binding.acceptRecyclerView.setVisibility(View.GONE);
                            }
                        });
                        builder.setNegativeButton("Cancel", (dialog, which) -> Toast.makeText(getContext(), "Cancel", Toast.LENGTH_SHORT).show());
                        builder.show();
                    }
                });
            }
        });
    }
}