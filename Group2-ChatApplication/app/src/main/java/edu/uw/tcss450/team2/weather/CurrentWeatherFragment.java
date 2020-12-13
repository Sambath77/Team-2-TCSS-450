package edu.uw.tcss450.team2.weather;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import edu.uw.tcss450.team2.R;
import edu.uw.tcss450.team2.databinding.FragmentCurrentWeatherBinding;
import edu.uw.tcss450.team2.databinding.FragmentSignInBinding;
import edu.uw.tcss450.team2.databinding.FragmentWeekWeatherBinding;

/**
 * A fragment to show the current weather.
 *
 * @author Sam Spillers
 * @version 1.0
 */
public class CurrentWeatherFragment extends Fragment {

    private WeatherViewModel mWeatherModel;
    private FragmentCurrentWeatherBinding binding;

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
        binding = FragmentCurrentWeatherBinding.inflate(inflater);

        mWeatherModel = new ViewModelProvider(getActivity())
                .get(WeatherViewModel.class);
        mWeatherModel.addResponseObserver(
                getViewLifecycleOwner(),
                this::weatherUpdate);


        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    private void weatherUpdate(final JSONObject response) {
        try {
            binding.currentDescripter.setText(WeatherViewModel.currentDescription(response));
            binding.currentTemperature.setText(Math.round(WeatherViewModel.currentTemperature(response)) + "°F");
        } catch (JSONException e) {
//            e.printStackTrace();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }
}