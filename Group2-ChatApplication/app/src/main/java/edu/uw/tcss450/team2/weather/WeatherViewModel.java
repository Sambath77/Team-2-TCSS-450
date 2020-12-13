package edu.uw.tcss450.team2.weather;

import android.annotation.SuppressLint;
import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import edu.uw.tcss450.team2.io.RequestQueueSingleton;

public class WeatherViewModel extends AndroidViewModel {

    public static final int HOURS_IN_DAY = 24;

    private MutableLiveData<JSONObject> mResponse;

    public WeatherViewModel(@NonNull Application application) {
        super(application);
        mResponse = new MutableLiveData<>();
        mResponse.setValue(new JSONObject());
    }

    public void addResponseObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super JSONObject> observer) {
        mResponse.observe(owner, observer);
    }

    private void handleError(final VolleyError error) {
        Log.e("TEST", "error: " + error);
        if (Objects.isNull(error.networkResponse)) {
            try {
                mResponse.setValue(new JSONObject("{" +
                        "error:\"" + error.getMessage() +
                        "\"}"));
            } catch (JSONException e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleError");
            }
        }
        else {
            String data = new String(error.networkResponse.data, Charset.defaultCharset())
                    .replace('\"', '\'');
            try {
                JSONObject response = new JSONObject();
                response.put("code", error.networkResponse.statusCode);
                response.put("data", new JSONObject(data));
                mResponse.setValue(response);
            } catch (JSONException e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleError");
            }
        }
    }

    public void connect(final String location) {
        Log.e("WEATHER", "Weather of: " + location);

        String url = "https://team-2-tcss-450-webservices.herokuapp.com/weather";

        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                mResponse::setValue,
                this::handleError){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("location", location);
                return headers;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the edu.uw.tcss450.team2.request to the queue
        RequestQueueSingleton.getInstance(getApplication().getApplicationContext())
                .addToRequestQueue(request);
    }

    public static double currentTemperature(JSONObject response) throws JSONException {
        return currentTemperatureFromDay(new JSONObject(new JSONObject(response.getString("body")).getString("current")));
    }

    public static double todayMaxTemperature(JSONObject response) throws JSONException {
        return maxTemperatureFromDay(new JSONObject(new JSONObject(response.getString("body")).getString("current")));
    }

    public static double todayMinTemperature(JSONObject response) throws JSONException {
        return minTemperatureFromDay(new JSONObject(new JSONObject(response.getString("body")).getString("current")));
    }

    public static String currentDescription(JSONObject response) throws JSONException {
        return currentDescriptionFromDay(new JSONObject(new JSONObject(response.getString("body")).getString("current")));
    }

    public static List<HourlyWeatherForecastRecyclerViewAdapter.HourData> interpretDayForecast(JSONObject response) {
        try {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            @SuppressLint("SimpleDateFormat") SimpleDateFormat format2 = new SimpleDateFormat("ha");
            JSONObject body = new JSONObject(response.getString("body"));
            JSONObject forecast = new JSONObject(body.getString("forecast"));
            JSONArray forecastday = forecast.getJSONArray("forecastday");

            // The following gets today's and tomorrow's forecast data and puts it in a single list
            JSONArray hourArrayDay1 = ((JSONObject) forecastday.get(0)).getJSONArray("hour");
            JSONArray hourArrayDay2 = ((JSONObject) forecastday.get(1)).getJSONArray("hour");
            List<JSONObject> dayList = new ArrayList<>();
            for (int index = 0; index < hourArrayDay1.length(); index++) {
                dayList.add((JSONObject) hourArrayDay1.get(index));
            }
            for (int index = 0; index < hourArrayDay2.length(); index++) {
                dayList.add((JSONObject) hourArrayDay2.get(index));
            }
            // The following finds the index of the first hour object after the current hour
            Date now = new Date();
            int startIndex = -1;
            for (int index = 0; index < dayList.size(); index++) {
                Date dt1 = format1.parse(dayList.get(index).get("time").toString());
                if (dt1.after(now)) {
                    startIndex = index - 1;
                    break;
                }
            }
            if (startIndex == -1) {
                throw new IllegalStateException();
            }

            List<String> times = new ArrayList<>();
            List<Double> temperatures = new ArrayList<>();
            List<Boolean> boldeds = new ArrayList<>();
            // Iterates over the next 24 hours objects
            for (JSONObject currentHour : dayList.subList(startIndex, startIndex + HOURS_IN_DAY)) {
                Date time = format1.parse(currentHour.get("time").toString());
                boldeds.add(time.getHours() % 3 == 0);
                if (time.getHours() % 3 == 0) {
                    times.add(format2.format(time).toLowerCase());
                } else {
                    times.add("");
                }
                temperatures.add(currentHour.getDouble("temp_f"));
            }
            double maxTemp = Collections.max(temperatures);
            double minTemp = Collections.min(temperatures);
            List<Double> lengthProportions = new ArrayList<>();
            for (double temperature : temperatures) {
                lengthProportions.add((temperature - minTemp) / (maxTemp - minTemp));
            }
            List<String> temperatureStrings = new ArrayList<>();
            for (int i = 0; i < HOURS_IN_DAY; i++) {
                temperatureStrings.add(convertTempToString(temperatures.get(i)));
            }

            List<HourlyWeatherForecastRecyclerViewAdapter.HourData> output = new ArrayList<>();
            for (int i = 0; i < HOURS_IN_DAY; i++) {
                output.add(new HourlyWeatherForecastRecyclerViewAdapter.HourData(temperatureStrings.get(i), times.get(i), lengthProportions.get(i), boldeds.get(i)));
            }
            return output;
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static DailyWeatherForecastRecyclerViewAdapter.DayForecastData interpretCurrentWeather(JSONObject response, DataUpdatable card) {
        try {
            JSONObject body = new JSONObject(response.getString("body"));
            JSONObject forecast = new JSONObject(body.getString("forecast"));
            JSONArray forecastday = forecast.getJSONArray("forecastday");
            JSONObject current = new JSONObject(body.getString("current"));

            String imageURL = "https:" + iconFromDay(current);
            double currentTemperature = currentTemperatureFromDay(current);
            String condition = currentDescriptionFromDay(current);
            String precipitation = "Humidity: " + precipitationFromDay(current) + "%";

            JSONObject currentDayTa = ((JSONObject) forecastday.get(0)).getJSONObject("day");  // First item is today
            double maxTemperature = maxTemperatureFromDay(currentDayTa);
            double minTemperature = minTemperatureFromDay(currentDayTa);

            DailyWeatherForecastRecyclerViewAdapter.DayForecastData output = new DailyWeatherForecastRecyclerViewAdapter.DayForecastData(null, null, condition, convertTempToString(currentTemperature), convertTempToString(maxTemperature), convertTempToString(minTemperature), precipitation);
            new DownloadBitmapTask(card, output).execute(imageURL);
            return output;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static DailyWeatherForecastRecyclerViewAdapter.DayForecastData[] interpretForecast(JSONObject response, DataUpdatable adapter) {
        try {
            JSONObject body = new JSONObject(response.getString("body"));
            JSONObject forecast = new JSONObject(body.getString("forecast"));
            JSONArray forecastday = forecast.getJSONArray("forecastday");

            @SuppressLint("SimpleDateFormat") SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
            @SuppressLint("SimpleDateFormat") DateFormat format2 = new SimpleDateFormat("EEEE");

            DailyWeatherForecastRecyclerViewAdapter.DayForecastData[] output = new DailyWeatherForecastRecyclerViewAdapter.DayForecastData[forecastday.length()];

            for (int index = 0; index < forecastday.length(); index++) {
                JSONObject currentDayTa = ((JSONObject) forecastday.get(index));

                // Get day of week
                String date = currentDayTa.getString("date");
                Date dt1= null;
                try {
                    dt1 = format1.parse(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String finalDay=format2.format(dt1);
                DailyWeatherForecastRecyclerViewAdapter.DayForecastData.DayOfWeek day = DailyWeatherForecastRecyclerViewAdapter.DayForecastData.DayOfWeek.getByString(finalDay);

                JSONObject currentDay = currentDayTa.getJSONObject("day");
                String imageURL = "https:" + iconFromDay(currentDay);
                double maxTemperature = maxTemperatureFromDay(currentDay);
                double minTemperature = minTemperatureFromDay(currentDay);
                String condition = currentDescriptionFromDay(currentDay);

                output[index] = new DailyWeatherForecastRecyclerViewAdapter.DayForecastData(day, null, condition, "", convertTempToString(maxTemperature), convertTempToString(minTemperature), null);
                new DownloadBitmapTask(adapter, output[index]).execute(imageURL);
            }
            return output;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static double currentTemperatureFromDay(JSONObject day) throws JSONException {
        return day.getDouble("temp_f");
    }

    private static double maxTemperatureFromDay(JSONObject day) throws JSONException {
        return day.getDouble("maxtemp_f");
    }

    private static double minTemperatureFromDay(JSONObject day) throws JSONException {
        return day.getDouble("mintemp_f");
    }

    private static String currentDescriptionFromDay(JSONObject day) throws JSONException {
        // The following capitalizes the first letter of each word after the first word
        char[] output = day.getJSONObject("condition").getString("text").toCharArray();
        for (int i = 1; i < output.length; i++) {
            if (output[i - 1] == ' ') {
                output[i] = Character.toUpperCase(output[i]);
            }
        }
        return String.valueOf(output);
    }

    private static String iconFromDay(JSONObject day) throws JSONException {
        return day.getJSONObject("condition").getString("icon");
    }

    private static String precipitationFromDay(JSONObject day) throws JSONException {
        return day.getString("humidity");
    }

    private static String convertTempToString(double temperature) {
        return Math.round(temperature) + "°F";
    }

    private static class DownloadBitmapTask extends AsyncTask<String, Void, Bitmap> {
        DataUpdatable mDataHolder;
        DailyWeatherForecastRecyclerViewAdapter.DayForecastData mData;

        public DownloadBitmapTask(DataUpdatable myDataHolder, DailyWeatherForecastRecyclerViewAdapter.DayForecastData data) {
            mDataHolder = myDataHolder;
            mData = data;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            mData.setBitMap(bitmap);
            mDataHolder.updateData();
        }
    }
}