package edu.uw.tcss450.ui.success;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.tcss450.R;
import edu.uw.tcss450.databinding.FragmentSuccessBinding;
import edu.uw.tcss450.model.UserInfoViewModel;

/**
 * A simple {@link Fragment} subclass.

 * create an instance of this fragment.
 */
public class SuccessFragment extends Fragment {

    //private FragmentSuccessBinding binding;

    public SuccessFragment() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //binding = FragmentSuccessBinding.inflate(inflater, container, false);
        // Inflate the layout for this fragment
        //return binding.getRoot();
        return inflater.inflate(R.layout.fragment_success, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Get a reference to the SafeArgs object
        //assert getArguments() != null;
        //SuccessFragmentArgs args = SuccessFragmentArgs.fromBundle(getArguments());
        //Log.d("ATIVITY", args.getEmial());
        //Set the text color of the label. NOTE no need to cast
        //binding.textView.setText("Hello " + args.getEmail());

        FragmentSuccessBinding binding = FragmentSuccessBinding.bind(getView());
        UserInfoViewModel model = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);
        //Log.d("ACTIVITY", model.getmEmail());
        binding.textEmail.setText("hello " + model.getmEmail() + "Here is my JWR " + model.getJwt());

    }
}