package edu.uw.tcss450.team2.weather;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import edu.uw.tcss450.team2.R;
import edu.uw.tcss450.team2.databinding.FragmentWeekWeatherBinding;

/**
 * A fragment to display the upcoming 5 to 10 day forecast.
 *
 * @author Sam Spillers
 * @version 1.0
 */
public class WeekWeatherFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
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

        FragmentWeekWeatherBinding binding = FragmentWeekWeatherBinding.bind(getView());
        recyclerView = binding.recyclerView;

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        DailyWeatherForecastRecyclerViewAdapter.DayForecastData[] forecastData = {
                new DailyWeatherForecastRecyclerViewAdapter.DayForecastData(DailyWeatherForecastRecyclerViewAdapter.DayForecastData.DayOfWeek.THURSDAY,
                        R.drawable.ic_baseline_cloud_24, "Cloudy", "57°F", "45°F"),
                new DailyWeatherForecastRecyclerViewAdapter.DayForecastData(DailyWeatherForecastRecyclerViewAdapter.DayForecastData.DayOfWeek.FRIDAY,
                        R.drawable.ic_baseline_wb_sunny_24, "Sunny", "52°F", "38°F"),
                new DailyWeatherForecastRecyclerViewAdapter.DayForecastData(DailyWeatherForecastRecyclerViewAdapter.DayForecastData.DayOfWeek.SATURDAY,
                        R.drawable.ic_baseline_wb_sunny_24, "Sunny", "47°F", "37°F"),
                new DailyWeatherForecastRecyclerViewAdapter.DayForecastData(DailyWeatherForecastRecyclerViewAdapter.DayForecastData.DayOfWeek.SUNDAY,
                        R.drawable.ic_baseline_wb_sunny_24, "Sunny", "48°F", "32°F"),
                new DailyWeatherForecastRecyclerViewAdapter.DayForecastData(DailyWeatherForecastRecyclerViewAdapter.DayForecastData.DayOfWeek.MONDAY,
                        R.drawable.ic_baseline_cloud_24, "Cloudy", "48°F", "40°F")
        };


        mAdapter = new DailyWeatherForecastRecyclerViewAdapter(forecastData);
        recyclerView.setAdapter(mAdapter);


    }

}