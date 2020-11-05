package edu.uw.tcss450.team2.weather;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import edu.uw.tcss450.team2.R;
import edu.uw.tcss450.team2.databinding.FragmentDayInWeekCardBinding;

/**
 * An adapter to facilitate the interaction between WeeklyWeather's recyclerView and its forecast card elements.
 *
 * @author Sam Spillers
 * @version 1.0
 */
public class DailyWeatherForecastRecyclerViewAdapter extends RecyclerView.Adapter<DailyWeatherForecastRecyclerViewAdapter.MyViewHolder> {
    private final DayForecastData[] mDataSet;

    /**
     * A view holder for a single forecast card element.
     *
     * @author Sam Spillers
     * @version 1.0
     */
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        public FragmentDayInWeekCardBinding binding;

        /**
         * Generates a viewHolder object.
         *
         * @param v The associated view.
         *
         * @author Sam Spillers
         * @version 1.0
         */
        public MyViewHolder(View v) {
            super(v);
            mView = v;
            binding = FragmentDayInWeekCardBinding.bind(v);
        }

        /**
         * Sets the appropriate fields on the corresponding forecast card view according to the given DayForecastData.
         *
         * @param data The forecast data to update the associated card view with.
         *
         * @author Sam Spillers
         * @version 1.0
         */
        public void setData(DayForecastData data) {
            binding.dayOfWeekText.setText(data.getDayOfWeek().getAbbreviation());
            binding.weatherPreviewImageView.setImageResource(data.getResID());
            binding.temperatureHighText.setText(data.getHighTemperatureForecast());
            binding.temperatureLowText.setText(data.getLowTemperatureForecast());
            binding.conditionText.setText(data.getWeatherDiscriptor());
        }
    }

    /**
     * Generates a new DailyWeatherForecastRecyclerViewAdapter and populates it using the given data set.
     *
     * @param myDataSet
     *
     * @author Sam Spillers
     * @version 1.0
     */
    public DailyWeatherForecastRecyclerViewAdapter(DayForecastData[] myDataSet) {
        mDataSet = myDataSet;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView v = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_day_in_week_card, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    /**
     * Populates the given view holder according to the data in the given position
     *
     * @param holder The vew holder to populate
     * @param position The position of the view holder in the recycler view
     *
     * @author Sam Spillers
     * @version 1.0
     */
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DayForecastData currentDayData = mDataSet[position];
        holder.setData(currentDayData);
    }

    @Override
    public int getItemCount() {
        return mDataSet.length;
    }

    /**
     * A class to contain the various data a single forecast card would want to display
     *
     * @author Sam Spillers
     * @version 1.0
     */
    public static class DayForecastData {
        // DO NOT make final. May change when new data comes in. Code to do so is not currently implemented.
        private DayOfWeek mDayOfWeek;
        private int mResID;
        private String mWeatherDiscriptor;
        private String mHighTemperatureForecast;
        private String mLowTemperatureForecast;

        /**
         * Creates a new DayForecastData object with the given parameters.
         *
         * @param myDayOfWeek The day of the week whose weather is being displayed.
         * @param myResID The ID of the icon to display for this weather forecast.
         * @param myWeatherDiscriptor The discriptor of the weater of this weather forecast card.
         * @param myHighTemperatureForecast This high temperature of the forecast card.
         * @param myLowTemperatureForecast The low temperature of the forecast card.
         *
         * @author Sam Spillers
         * @version 1.0
         */
        public DayForecastData(DayOfWeek myDayOfWeek, int myResID, String myWeatherDiscriptor,
                               String myHighTemperatureForecast, String myLowTemperatureForecast) {
            mDayOfWeek = myDayOfWeek;
            mResID = myResID;
            mWeatherDiscriptor = myWeatherDiscriptor;
            mHighTemperatureForecast = myHighTemperatureForecast;
            mLowTemperatureForecast = myLowTemperatureForecast;
        }

        /**
         * Gets the day of the week of this forecast data.
         *
         * @return The day of the week.
         *
         * @author Sam Spillers
         * @version 1.0
         */
        public DayOfWeek getDayOfWeek() {
            return mDayOfWeek;
        }

        /**
         * Gets the ID of the icon of this forecast data.
         *
         * @return The ID.
         *
         * @author Sam Spillers
         * @version 1.0
         */
        public int getResID() {
            return mResID;
        }

        /**
         * Gets the weather discriptor of this forecast data.
         *
         * @return The weather discriptor.
         *
         * @author Sam Spillers
         * @version 1.0
         */
        public String getWeatherDiscriptor() {
            return mWeatherDiscriptor;
        }

        /**
         * Gets the high temperature of this forecast data.
         *
         * @return The high temperature.
         *
         * @author Sam Spillers
         * @version 1.0
         */
        public String getHighTemperatureForecast() {
            return mHighTemperatureForecast;
        }

        /**
         * Gets the low temperature of this forecast data.
         *
         * @return The low temperature.
         *
         * @author Sam Spillers
         * @version 1.0
         */
        public String getLowTemperatureForecast() {
            return mLowTemperatureForecast;
        }

        /**
         * A helper enum to represent the day of the week.
         *
         * @author Sam Spillers
         * @version 1.0
         */
        public enum DayOfWeek {

            // TODO: Move to string resource file
            MONDAY ("MON"),
            TUESDAY ("TUE"),
            WEDNESDAY ("WED"),
            THURSDAY ("THU"),
            FRIDAY ("FRI"),
            SATURDAY ("SAT"),
            SUNDAY ("SUN");

            private final String mAbbreviation;
            DayOfWeek(String myAbbreviation) {
                mAbbreviation = myAbbreviation;
            }

            /**
             * Gets the three letter abbreviation of the day of the week.
             *
             * @return The three letter abbreviation as a String.
             *
             * @author Sam Spillers
             * @version 1.0
             */
            public String getAbbreviation() {
                return mAbbreviation;
            }
        }
    }
}
