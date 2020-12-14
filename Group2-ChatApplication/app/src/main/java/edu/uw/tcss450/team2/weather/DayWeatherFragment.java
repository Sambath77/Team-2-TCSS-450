package edu.uw.tcss450.team2.weather;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import edu.uw.tcss450.team2.R;
import edu.uw.tcss450.team2.databinding.FragmentDayWeatherBinding;

/**
 * A fragment to show the forecast for the next 24 hours.
 *
 * @author Sam Spillers
 * @version 1.0
 */
public class DayWeatherFragment extends Fragment {
    FragmentDayWeatherBinding binding;

    private WeatherViewModel mWeatherModel;
    private RecyclerView recyclerView;
    private HourlyWeatherForecastRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    public DayWeatherFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_day_weather, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mWeatherModel = new ViewModelProvider(getActivity())
                .get(WeatherViewModel.class);
        mWeatherModel.addResponseObserver(
                getViewLifecycleOwner(),
                this::weatherUpdate);

        binding = FragmentDayWeatherBinding.bind(getView());
        recyclerView = binding.hourlyForecastRv;
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        List<HourlyWeatherForecastRecyclerViewAdapter.HourData> hourDataList = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            hourDataList.add(new HourlyWeatherForecastRecyclerViewAdapter.HourData("33°F", "3am", 1, false));
        }

        mAdapter = new HourlyWeatherForecastRecyclerViewAdapter(hourDataList);
        recyclerView.setAdapter(mAdapter);
    }

    /**
     * Handles a response from the webserver with updated weather information.
     *
     * @param response The response from the web server.
     * @author Sam Spillers
     * @version 1.0
     */
    private void weatherUpdate(JSONObject response) {
        if (response != null && response.names() != null && response.names().length() != 0) {
            List<HourlyWeatherForecastRecyclerViewAdapter.HourData> out = WeatherViewModel.interpretDayForecast(response);
            if (out != null) {
                mAdapter.updateData(out);
            }
        }
    }
}