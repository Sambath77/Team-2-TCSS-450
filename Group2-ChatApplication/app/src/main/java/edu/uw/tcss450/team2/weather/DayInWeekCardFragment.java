package edu.uw.tcss450.team2.weather;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.TextViewCompat;
import androidx.fragment.app.Fragment;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.tcss450.team2.R;
import edu.uw.tcss450.team2.databinding.FragmentDayInWeekCardBinding;

/**
 * A fragment to show a single day's forecast in the week forecast fragment.
 *
 * @author Sam Spillers
 * @version 1.0
 */
public class DayInWeekCardFragment extends Fragment {
    FragmentDayInWeekCardBinding binding;

    public DayInWeekCardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_day_in_week_card, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding = FragmentDayInWeekCardBinding.bind(view);

        TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(binding.conditionText, 1, 17, 1,
                TypedValue.COMPLEX_UNIT_DIP);
    }
}