package edu.uw.tcss450.team2.weather;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import org.json.JSONObject;

import edu.uw.tcss450.team2.databinding.FragmentCurrentWeatherBinding;

/**
 * A fragment to show the current weather.
 *
 * @author Sam Spillers
 * @version 1.0
 */
public class CurrentWeatherFragment extends Fragment implements DataUpdatable {

    private WeatherViewModel mWeatherModel;
    private FragmentCurrentWeatherBinding binding;
    private DailyWeatherForecastRecyclerViewAdapter.DayForecastData mData;

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

    /**
     * The method that is called when our weather view model receives an updated response. Handles its incoming weather data.
     *
     * @param response The JSON response from the weather update web call.
     * @author Sam Spillers
     * @version 1.0
     */
    private void weatherUpdate(final JSONObject response) {
        if (response != null && response.names() != null && response.names().length() != 0) {
            DailyWeatherForecastRecyclerViewAdapter.DayForecastData dataResponse = WeatherViewModel.interpretCurrentWeather(response, this);
            if (dataResponse != null) {
                mData = dataResponse;
                updateData();
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mData = null;
    }

    /**
     * Updates the current weather's data.
     *
     * @author Sam Spillers
     * @version 1.0
     */
    @Override
    public void updateData() {
        if (mData != null) {
            binding.currentDescripter.setText(mData.getWeatherDiscriptor());
            binding.currentTemperature.setText(mData.getCurrentWeather());
            String temperatureText = mData.getHighTemperatureForecast() + "/" + mData.getLowTemperatureForecast();
            binding.currentHighLow.setText(temperatureText);
            binding.currentPrecipitation.setText(mData.getPrecipitation());
            binding.conditionIcon.setImageBitmap(mData.getBitMap());
        }
    }
}