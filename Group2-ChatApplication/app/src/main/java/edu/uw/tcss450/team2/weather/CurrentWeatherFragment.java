package edu.uw.tcss450.team2.weather;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import edu.uw.tcss450.team2.R;
import edu.uw.tcss450.team2.databinding.FragmentCurrentWeatherBinding;
import edu.uw.tcss450.team2.databinding.FragmentWeekWeatherBinding;

/**
 * A fragment to show the current weather.
 *
 * @author Sam Spillers
 * @version 1.0
 */
public class CurrentWeatherFragment extends Fragment {

    /**
     * Empty constructor
     *
     * @author Sam Spillers
     * @version 1.0
     */
    public CurrentWeatherFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_current_weather, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }
}