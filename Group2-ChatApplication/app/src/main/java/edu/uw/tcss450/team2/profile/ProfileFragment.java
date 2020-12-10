package edu.uw.tcss450.team2.profile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.tcss450.team2.R;
import edu.uw.tcss450.team2.databinding.FragmentProfileBinding;
import edu.uw.tcss450.team2.model.UserInfoViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;
    private UserInfoViewModel userInfoViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onCreate(@NonNull Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userInfoViewModel = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);

        profileViewModel = new ViewModelProvider(getActivity()).get(ProfileViewModel.class);
        profileViewModel.getProfile(userInfoViewModel.getJwt(), userInfoViewModel.getEmail());



    }

    @Override
    public void onViewCreated(@NonNull View view, @NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentProfileBinding binding = FragmentProfileBinding.bind(view);

        profileViewModel.addContactListObserver(getViewLifecycleOwner(), profile -> {
            if (!profile.isEmpty()) {
                String fullname = profile.get(0).getmFName() + " " + profile.get(0).getmLName();
                binding.acceptTextFullname.setText(fullname);
                binding.acceptTextUsername.setText(profile.get(0).getmUsername());
                binding.acceptTextEmail.setText(profile.get(0).getEmail());

            }
        });
    }
}