package edu.uw.tcss450.team2.home;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.tasks.OnSuccessListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.Executor;

import edu.uw.tcss450.team2.home.HomeFragmentDirections;
import edu.uw.tcss450.team2.R;

import edu.uw.tcss450.team2.databinding.FragmentHomeBinding;
import edu.uw.tcss450.team2.home.HomeFragment;
import edu.uw.tcss450.team2.model.UserInfoViewModel;
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

/**
 * A fragment that shows unread message notification and current live weather forecast data.
 * Appropriately shows the map in the home page after triggering callback later.
 * @author Hyeong Suk Kim
 * @version 1.0 (Oct 2020)
 */
public class HomeFragment extends Fragment /*implements OnMapReadyCallback*/ {
    //Initialize Map variables
//    private static HomeFragment INSTANCE = null;
//    View view;
//    GoogleMap map;
//    MapView mapView;
    private HomeViewModel mViewModel;
    private FragmentHomeBinding binding;

    private FusedLocationProviderClient fusedLocationClient;
    private double currLatitude;
    private double currLongitude;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = new ViewModelProvider(getActivity()).get(HomeViewModel.class);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_home, container, false);


        binding = FragmentHomeBinding.inflate(inflater);
        return binding.getRoot();
    }


    /**
     * In onViewCreated, first it received user email string and shows it in the application top.
     * Second, it would need to handle (TODO) hit API and retireve data for live weather forecating.
     *
     */
    @SuppressLint("MissingPermission")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Local access to the ViewBinding object. No need to create as Instance Var as it is only
        //used here.
        FragmentHomeBinding binding = FragmentHomeBinding.bind(getView());
        //Note argument sent to the ViewModelProvider constructor. It is the Activity that
        //holds this fragment.
        UserInfoViewModel model = new ViewModelProvider(getActivity())
                .get(UserInfoViewModel.class);



        binding.textUserEmail.setText(model.getEmail());


        //TODO handling notification - need to replace these with actual messages not mock up data
        binding.textUnreadChat.setText("You have 7 Unread Messages");

        binding.layoutHomeUnreadChat.setOnClickListener(layout ->
                Navigation.findNavController(getView()).navigate(
                        HomeFragmentDirections.actionNavigationHomeToChatFragment()));

        binding.layoutHomeWeather.setOnClickListener(layout ->
                Navigation.findNavController(getView()).navigate(
                        HomeFragmentDirections.actionNavigationHomeToNavigationWeather()));

        //setter for lat and long
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            currLatitude = location.getLatitude();
                            currLongitude = location.getLongitude();
                            System.out.println("Current lat: " + currLatitude);
                            System.out.println("Current long: " + currLongitude);
                            mViewModel.connectGet(currLatitude, currLongitude);
                        }
                    }});


        mViewModel.addResponseObserver(getViewLifecycleOwner(),
                result -> {
                    try {
                        binding.textLocation.setText(result.getJSONObject("location").getString("name"));
                        binding.textTemperature.setText("Temperature: " + result.getJSONObject("current").getDouble("temp_f") + " °F");
                        binding.textWeather.setText(result.getJSONObject("current").getJSONObject("condition").getString("text"));
                        binding.textWindSpeed.setText("Wind Speed: " + result.getJSONObject("current").getString("wind_mph") + " mph");
                        binding.textHumidity.setText("Humidity: " + result.getJSONObject("current").getString("humidity") + " %");
                        binding.textPrecipitation.setText("Precipitation: " + result.getJSONObject("current").getString("precip_in") + " inches");

                        Picasso.get().load("https:" + result.getJSONObject("current").getJSONObject("condition").getString("icon")).into(binding.imageWeatherIcon);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });


    }
}