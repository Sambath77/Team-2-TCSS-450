package edu.uw.tcss450.team2.weather;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import edu.uw.tcss450.team2.R;
import edu.uw.tcss450.team2.databinding.FragmentCurrentWeatherBinding;
import edu.uw.tcss450.team2.databinding.FragmentWeatherBinding;
import edu.uw.tcss450.team2.signin.SignInFragmentDirections;
import edu.uw.tcss450.team2.signin.SignInViewModel;

/**
 * The main fragment of the weather tab. Displays the current, daily, and 'weekly' forecast, as well as the location whose weather is being displayed.
 *
 * @author Sam Spillers
 * @version 1.0
 */
public class WeatherFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private WeatherViewModel mWeatherModel;
    private FragmentWeatherBinding binding;
    // TODO: fill with real locations
    List<String> dummyLocationData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWeatherBinding.inflate(inflater);


        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    /**
     * Populates the Spinner with data, asks for location permission if not already enabled.
     * @param view See inherited class
     * @param savedInstanceState See inherited class
     *
     * @author Sam Spillers
     * @version 1.0
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Gets permission for location data
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }


        mWeatherModel = new ViewModelProvider(getActivity())
                .get(WeatherViewModel.class);
        mWeatherModel.addResponseObserver(
                getViewLifecycleOwner(),
                this::weatherUpdate);

        // TODO: Populate with real data
        dummyLocationData = new ArrayList<String>();
        dummyLocationData.add("Tacoma, WA (98402)");
        dummyLocationData.add("Your Location");
        dummyLocationData.add("Seattle, WA (98195)");
        dummyLocationData.add("New York, NY (40.7128° N, 74.0060° W)");
        dummyLocationData.add("Add new location...");

        FragmentWeatherBinding binding = FragmentWeatherBinding.bind(getView());

        Spinner locationSpinner = binding.LocationSpinner;
        locationSpinner.setOnItemSelectedListener(this);

        ArrayAdapter<String> locationAdapter = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, dummyLocationData);
        locationAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        locationSpinner.setAdapter(locationAdapter);

        mWeatherModel.connect("98402");

    }

    private void weatherUpdate(final JSONObject response) {
        Toast.makeText(getActivity(),
                "Weather data received: " + response,
                Toast.LENGTH_LONG).show();

//        WeatherViewModel.interpretWeather(response);
//            Log.e("WEATHERVIEWMODEL", "desc: " + WeatherViewModel.currentDescription(response));
//            Log.e("WEATHERVIEWMODEL", "temp: " + WeatherViewModel.currentTemperature(response));
    }

    /**
     * Handles Spinner items being selected.
     *
     * @author Sam Spillers
     * @version 1.0
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.d("WeatherFragment.onItemSelected()", "parent: " + parent.toString() + ", view: " + view.toString() + ", position: " + position + ", id: " + id);
        if (id == dummyLocationData.size() - 1) {  // If the last item (the choose new location item) was selected
            Navigation.findNavController(getView()).navigate(WeatherFragmentDirections.actionNavigationWeatherToChooseLocationContainerFragment());
        }
    }

    // TODO: ?
    @Override
    public void onNothingSelected(AdapterView<?> parent) {}
}