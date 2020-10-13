package edu.uw.tcss450.sprint0.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.tcss450.sprint0.databinding.FragmentColorBinding;

/**
 */
public class ColorFragment extends Fragment {

    private FragmentColorBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        binding = FragmentColorBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        assert getArguments() != null;
        ColorFragmentArgs args = ColorFragmentArgs.fromBundle(getArguments());

        updateContent(args.getColor());
    }

    public void updateContent(int color) {
        binding.textLabel.setTextColor(color);
    }
}