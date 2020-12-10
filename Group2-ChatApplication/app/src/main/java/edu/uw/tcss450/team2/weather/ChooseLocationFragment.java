package edu.uw.tcss450.team2.weather;

import android.annotation.SuppressLint;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import edu.uw.tcss450.team2.R;
import edu.uw.tcss450.team2.databinding.FragmentChooseLocationBinding;
import edu.uw.tcss450.team2.model.LocationViewModel;

public class ChooseLocationFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener {
    private static final float MARKER_ZOOM_LEVEL = 14f;

    private LocationViewModel mModel;
    private GoogleMap mMap;
    private Marker currentMarker;
    private WeatherFragment.MyLocation currentLocation;
    private FragmentChooseLocationBinding binding;
    private Geocoder geocoder;

    public ChooseLocationFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_choose_location, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding = FragmentChooseLocationBinding.bind(getView());
        geocoder = new Geocoder(getContext(), Locale.getDefault());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            binding.locationInfoTextView.setAutoSizeTextTypeUniformWithConfiguration(
                    1, 20, 1, TypedValue.COMPLEX_UNIT_DIP);
        }

        mModel = new ViewModelProvider(getActivity()).get(LocationViewModel.class);
        mModel.addLocationObserver(getViewLifecycleOwner(), this::handleNewLocation);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        //add this fragment as the OnMapReadyCallback -> See onMapReady()
        mapFragment.getMapAsync(this);

        binding.findZipCodeButton.setOnClickListener(this::findZipCode);
        binding.selectLocationButton.setOnClickListener(this::selectLocation);
        binding.closeIcon.setOnClickListener(this::closeCard);
        binding.pinImageView.setOnClickListener(this::focusPin);
    }

    private void focusPin(View view) {
        if (currentMarker != null) {
            mMap.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                            currentMarker.getPosition(), MARKER_ZOOM_LEVEL));
        }
    }

    private void selectLocation(View view) {
        if (currentLocation != null) {
            ChooseLocationFragmentDirections.ActionChooseLocationFragmentToNavigationWeather action = ChooseLocationFragmentDirections.actionChooseLocationFragmentToNavigationWeather();
            action.setNewMyLocationObject(currentLocation);
            Navigation.findNavController(getView()).navigate(action);
        }
    }

    private void closeCard(View view) {
        if (currentMarker != null) {
            currentMarker.remove();
        }
        binding.bottomCardLayout.setVisibility(View.GONE);
        currentLocation = null;
    }

    private void findZipCode(View view) {
        String zipCodeText = binding.editZipCodeTextField.getText().toString();
        try {
            Log.e("Map", "zipCodeText: " + zipCodeText);
            Log.e("Map", "Integer.parseInt(zipCodeText): " + Integer.parseInt(zipCodeText));
            int zipCode = Integer.parseInt(zipCodeText);
            List<Address> addresses = geocoder.getFromLocationName(String.valueOf(zipCode), 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                LatLng zipLocation = new LatLng(address.getLatitude(), address.getLongitude());  // TODO: Get latlng from zipcode
                createNewMarkerAtLatLng(zipLocation);
                openBottomCard(new WeatherFragment.ZipCode(geocoder, zipCode));

            } else {
                Log.e("Map", "Address returned null or empty");
                binding.editZipCodeTextField.setError("Invalid Zip Code");
            }
        } catch (NumberFormatException | IOException e) {
            e.printStackTrace();
            binding.editZipCodeTextField.setError("Invalid Zip Code");
        }
    }

    private void handleNewLocation(Location location) {
        if (mMap != null && location != null) {
//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15.0f));

        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LocationViewModel model = new ViewModelProvider(getActivity())
                .get(LocationViewModel.class);
        model.addLocationObserver(getViewLifecycleOwner(), location -> {
            if(location != null) {
                googleMap.getUiSettings().setZoomControlsEnabled(true);
                googleMap.setMyLocationEnabled(true);
                final LatLng c = new LatLng(location.getLatitude(), location.getLongitude());
                //Zoom levels are from 2.0f (zoomed out) to 21.f (zoomed in)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(c, 15.0f));
            }
        });
        mMap.setOnMapClickListener(this);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        Log.d("LAT/LONG", latLng.toString());
        createNewMarkerAtLatLng(latLng);
        Location temp = new Location(LocationManager.GPS_PROVIDER);
        temp.setLatitude(latLng.latitude);
        temp.setLongitude(latLng.longitude);
        openBottomCard(new WeatherFragment.LongLat(geocoder, temp));
    }

    private void openBottomCard(WeatherFragment.MyLocation location) {
        binding.locationInfoTextView.setText(location.displayName());
        binding.bottomCardLayout.setVisibility(View.VISIBLE);
        currentLocation = location;
    }

    private void createNewMarkerAtLatLng(LatLng location) {
        // Remove old marker if present
        if (currentMarker != null) {
            currentMarker.remove();
        }
        currentMarker = mMap.addMarker(new MarkerOptions()
                .position(location)
                .title("Location Selected"));
        mMap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                        location, mMap.getCameraPosition().zoom));
    }
}