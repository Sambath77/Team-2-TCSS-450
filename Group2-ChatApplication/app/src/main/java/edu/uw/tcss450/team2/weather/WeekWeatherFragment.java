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

import edu.uw.tcss450.team2.R;
import edu.uw.tcss450.team2.databinding.FragmentWeekWeatherBinding;

/**
 * A fragment to display the upcoming 5 to 10 day forecast.
 *
 * @author Sam Spillers
 * @version 1.0
 */
public class WeekWeatherFragment extends Fragment {

    private WeatherViewModel mWeatherModel;

    private RecyclerView recyclerView;
    private DailyWeatherForecastRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    /**
     * Empty constructor.
     *
     * @author Sam Spillers
     * @version 1.0
     */
    public WeekWeatherFragment() {
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
        return inflater.inflate(R.layout.fragment_week_weather, container, false);
    }

    /**
     * Populates the week forecast's recycler view with individual day forecast cards.
     *
     * @param view See inherited class.
     * @param savedInstanceState See inherited class.
     *
     * @author Sam Spillers
     * @version 1.0
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mWeatherModel = new ViewModelProvider(getActivity())
                .get(WeatherViewModel.class);
        mWeatherModel.addResponseObserver(
                getViewLifecycleOwner(),
                this::weatherUpdate);

        FragmentWeekWeatherBinding binding = FragmentWeekWeatherBinding.bind(getView());
        recyclerView = binding.recyclerView;

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        // Fills with dummy data while loading. Theoretically this is really bad. Not sure of the workaround though. Leaving it in.
        DailyWeatherForecastRecyclerViewAdapter.DayForecastData[] forecastData = {
                new DailyWeatherForecastRecyclerViewAdapter.DayForecastData(DailyWeatherForecastRecyclerViewAdapter.DayForecastData.DayOfWeek.THURSDAY,
                        null, "Cloudy", "", "57°F", "45°F", null),
                new DailyWeatherForecastRecyclerViewAdapter.DayForecastData(DailyWeatherForecastRecyclerViewAdapter.DayForecastData.DayOfWeek.FRIDAY,
                        null, "Sunny", "", "52°F", "38°F", null),
                new DailyWeatherForecastRecyclerViewAdapter.DayForecastData(DailyWeatherForecastRecyclerViewAdapter.DayForecastData.DayOfWeek.SATURDAY,
                        null, "Sunny", "", "47°F", "37°F", null),
                new DailyWeatherForecastRecyclerViewAdapter.DayForecastData(DailyWeatherForecastRecyclerViewAdapter.DayForecastData.DayOfWeek.SUNDAY,
                        null, "Sunny", "", "48°F", "32°F", null),
                new DailyWeatherForecastRecyclerViewAdapter.DayForecastData(DailyWeatherForecastRecyclerViewAdapter.DayForecastData.DayOfWeek.MONDAY,
                        null, "Cloudy", "", "48°F", "40°F", null)
        };


        mAdapter = new DailyWeatherForecastRecyclerViewAdapter(forecastData);
        recyclerView.setAdapter(mAdapter);
    }

    private void weatherUpdate(final JSONObject response) {
        if (response != null && response.names() != null && response.names().length() != 0) {
            mAdapter.updateData(WeatherViewModel.interpretForecast(response, mAdapter));
        }
    }


}