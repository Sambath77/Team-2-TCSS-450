package edu.uw.tcss450.team2.home;

import android.app.Application;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.Objects;

public class HomeViewModel extends AndroidViewModel {

    private MutableLiveData<JSONObject> mResponse;
    private String name;
    private double tempF;
    private String conditionText;

    private double currLatitude;
    private double currLongitude;



    public HomeViewModel(@NonNull Application application) {
        super(application);

        mResponse = new MutableLiveData<>();
        mResponse.setValue(new JSONObject());
    }

    public void addResponseObserver(@NonNull LifecycleOwner owner, @NonNull Observer<? super JSONObject> observer) {
        mResponse.observe(owner, observer);
    }

    private void handleResult(final JSONObject result) {
//        JSONObject businessObject = offerObject.getJSONObject("business");
//        String nameValue = businessObject.getString("name");
//        System.out.println(nameValue);



        try {
            JSONObject locationObject = result.getJSONObject("location");
            JSONObject currentObject = result.getJSONObject("current");
            JSONObject conditionObject = currentObject.getJSONObject("condition");

            name = locationObject.getString("name"); // Stores correct name
            tempF = currentObject.getDouble("temp_f");
            conditionText = conditionObject.getString("text");

            System.out.println("Name: " + name);
            System.out.println("tempF: " + tempF);
            System.out.println("conditionText: " + conditionText);
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        mResponse.setValue(result);
        mResponse.setValue(result);
    }

    private void handleError(final VolleyError error) {
        if (Objects.isNull(error.networkResponse)) {
            try {
                mResponse.setValue(new JSONObject("{error:\"" + error.getMessage() + "\"}"));
            } catch (JSONException e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleError");
            }
        } else {
            String data = new String(error.networkResponse.data, Charset.defaultCharset()).replace('\"', '\'');
            try {
                mResponse.setValue(new JSONObject("{code:" + error.networkResponse.statusCode + ", data:\"" + data + "\"}"));
            } catch (JSONException e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleError");
            }
        }
    }

    public void connectGet(final double theLatitude, final double theLongitude) {
//        String url = "https://hsk26-lab4-backend.herokuapp.com/hello";
//        Request request = new JsonObjectRequest(
//                Request.Method.GET,
//                url,
//                null,  // no body for this get request
//                this::handleResult,
//                this::handleError
//        );

//        request.setRetryPolicy(new DefaultRetryPolicy(
//                10_000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Instantiate the RequestQueue and add the request to the queue
//        Volley.newRequestQueue(getApplication().getApplicationContext()).add(request);









        String key = "66154e12d5ec4716b1c55141201211";

//        String lat = "47.606209";
//        String longi = "-122.332069";



        String url = "https://api.weatherapi.com/v1/current.json?key=" + key + "&q=" + theLatitude + "," + theLongitude;
        System.out.println("Open Weather URL:   "+ url);
        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                this::handleResult,
                this::handleError
        );
        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

         //Instantiate the RequestQueue and add the request to the queue
        Volley.newRequestQueue(getApplication().getApplicationContext()).add(request);




    }

    public void connectPost() {
        String url = "https://hsk26-lab4-backend.herokuapp.com/hello";

        Request request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                null,
                mResponse::setValue,
                this::handleError);

        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(getApplication().getApplicationContext()).add(request);
    }

//    public void setLocationViewModel(final double inputLatitude, final double inputLongitude) {
//        this.currLatitude = inputLatitude;
//        this.currLongitude = inputLongitude;
//    }
}
