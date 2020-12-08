package edu.uw.tcss450.team2.weather;

import android.app.Application;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import edu.uw.tcss450.team2.io.RequestQueueSingleton;

public class WeatherViewModel extends AndroidViewModel {

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

    public static void interpretWeather(JSONObject response) {
        Log.e("WEATHERVIEWMODEL", "response: " + response);
        try {
            JSONObject body = new JSONObject(response.getString("body"));
            Log.e("WEATHERVIEWMODEL", "body: " + body);
            Log.e("WEATHERVIEWMODEL", "body.names(): " + body.names());
            JSONObject current = new JSONObject(body.getString("current"));
            JSONObject forecast = new JSONObject(body.getString("forecast"));
            Log.e("WEATHERVIEWMODEL", "current: " + current);
            Log.e("WEATHERVIEWMODEL", "forecast: " + forecast);
            Log.e("WEATHERVIEWMODEL", "current condition: " + current.getJSONObject("condition").getString("text"));
            String weatherDiscriptor = current.getJSONObject("condition").getString("text");
            double currentTemp = current.getDouble("temp_f");
            Log.e("WEATHERVIEWMODEL", "weatherDiscriptor: " + weatherDiscriptor);
            Log.e("WEATHERVIEWMODEL", "currentTemp: " + currentTemp);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static double currentTemperature(JSONObject response) throws JSONException {
        return new JSONObject(new JSONObject(response.getString("body")).getString("current")).getDouble("temp_f");
    }

    public static String currentDescription(JSONObject response) throws JSONException {
        return new JSONObject(new JSONObject(response.getString("body")).getString("current")).getJSONObject("condition").getString("text");
    }
}