package edu.uw.tcss450.team2.weather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import edu.uw.tcss450.team2.R;
import edu.uw.tcss450.team2.databinding.FragmentDayHourForecastBarBinding;
import edu.uw.tcss450.team2.databinding.FragmentDayInWeekCardBinding;

public class HourlyWeatherForecastRecyclerViewAdapter extends RecyclerView.Adapter<HourlyWeatherForecastRecyclerViewAdapter.MyViewHolder> {

    private final List<HourData> mData;


    public HourlyWeatherForecastRecyclerViewAdapter(List<HourData> inputList) {
        mData = new ArrayList<HourData>(inputList);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_day_hour_forecast_bar, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        HourData hourData = mData.get(position);
        holder.setData(hourData);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void updateData(List<HourData> interpretDayForecast) {
        mData.clear();
        mData.addAll(interpretDayForecast);
        notifyDataSetChanged();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public static final double MINIMUM_BAR_HEIGHT_PROPORTION = 0.6;
        public static final int BIG_FONT_SIZE = 14;
        public static final int SMALL_FONT_SIZE = 10;

        private final View mView;
        FragmentDayHourForecastBarBinding mBinding;
        private final int startingHeight;

        public MyViewHolder(View v) {
            super(v);
            mView = v;
            mBinding = FragmentDayHourForecastBarBinding.bind(v);
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) mBinding.tempBar.getLayoutParams();
            startingHeight = params.height;
        }

        public void setData(HourlyWeatherForecastRecyclerViewAdapter.HourData data) {
            mBinding.hourTemperatureText.setText(data.getTemperature());
            mBinding.hourTimeText.setText(data.getTime());
            if (data.getTime().length() >= 4) {
                mBinding.hourTimeText.setTextSize(SMALL_FONT_SIZE);
            } else {
                mBinding.hourTimeText.setTextSize(BIG_FONT_SIZE);
            }
            if (data.getTemperature().length() >= 5) {
                mBinding.hourTemperatureText.setTextSize(SMALL_FONT_SIZE);
            } else {
                mBinding.hourTemperatureText.setTextSize(BIG_FONT_SIZE);
            }
            boolean bolded = data.getBolded();
            if (bolded) {
                mBinding.tempBar.setBackgroundColor(ContextCompat.getColor(mView.getContext(), R.color.colorWeatherDark));
            } else {
                mBinding.tempBar.setBackgroundColor(ContextCompat.getColor(mView.getContext(), R.color.colorWeatherLight));
            }
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) mBinding.tempBar.getLayoutParams();
            int minimumBarHeight = (int) Math.round(startingHeight * MINIMUM_BAR_HEIGHT_PROPORTION);
            params.height = (int) Math.round((startingHeight - minimumBarHeight) * data.getSize() + minimumBarHeight);
            mBinding.tempBar.setLayoutParams(params);
        }
    }

    public static class HourData {
        private final String mTemperature;
        private final String mTime;
        private final boolean mBolded;
        private final double mSizeProportion;

        public HourData(String myTemperature, String myTime, double mySize, boolean myBolded) {
            mTemperature = myTemperature;
            mTime = myTime;
            mSizeProportion = mySize;
            mBolded = myBolded;
        }

        public String getTime() {
            return mTime;
        }

        public boolean getBolded() {
            return mBolded;
        }

        public double getSize() {
            return mSizeProportion;
        }

        public String getTemperature() {
            return mTemperature;
        }

        public String toString() {
            return "HourData[" + mTemperature + ", " + mTime + ", " + mSizeProportion + ", " + mBolded + "]";
        }
    }
}