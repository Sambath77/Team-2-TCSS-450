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

public class DailyWeatherForecastRecyclerViewAdapter extends RecyclerView.Adapter<DailyWeatherForecastRecyclerViewAdapter.MyViewHolder> {
    private final DayForecastData[] mDataSet;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        public FragmentDayInWeekCardBinding binding;
        public MyViewHolder(View v) {
            super(v);
            mView = v;
            binding = FragmentDayInWeekCardBinding.bind(v);
        }

        public void setData(DayForecastData data) {
            binding.dayOfWeekText.setText(data.getDayOfWeek().getAbbreviation());
            binding.weatherPreviewImageView.setImageResource(data.getResID());
            binding.temperatureHighText.setText(data.getHighTemperatureForecast());
            binding.temperatureLowText.setText(data.getLowTemperatureForecast());
            binding.conditionText.setText(data.getWeatherDiscriptor());
        }
    }

    public DailyWeatherForecastRecyclerViewAdapter(DayForecastData[] myDataSet) {
        mDataSet = myDataSet;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView v = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_day_in_week_card, parent, false);
        /// ... TODO(?)
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DayForecastData currentDayData = mDataSet[position];
        holder.setData(currentDayData);
    }

    @Override
    public int getItemCount() {
        return mDataSet.length;
    }

    public static class DayForecastData {
        // DO NOT make final. May change when new data comes in. Code to do so is not currently implemented.
        private DayOfWeek mDayOfWeek;
        private int mResID;
        private String mWeatherDiscriptor;
        private String mHighTemperatureForecast;
        private String mLowTemperatureForecast;

        public DayForecastData(DayOfWeek myDayOfWeek, int myResID, String myWeatherDiscriptor,
                               String myHighTemperatureForecast, String myLowTemperatureForecast) {
            mDayOfWeek = myDayOfWeek;
            mResID = myResID;
            mWeatherDiscriptor = myWeatherDiscriptor;
            mHighTemperatureForecast = myHighTemperatureForecast;
            mLowTemperatureForecast = myLowTemperatureForecast;
        }

        public DayOfWeek getDayOfWeek() {
            return mDayOfWeek;
        }

        public int getResID() {
            return mResID;
        }

        public String getWeatherDiscriptor() {
            return mWeatherDiscriptor;
        }

        public String getHighTemperatureForecast() {
            return mHighTemperatureForecast;
        }

        public String getLowTemperatureForecast() {
            return mLowTemperatureForecast;
        }

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
            public String getAbbreviation() {
                return mAbbreviation;
            }
        }
    }
}
