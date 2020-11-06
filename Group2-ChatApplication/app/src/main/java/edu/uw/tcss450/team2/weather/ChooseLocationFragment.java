package edu.uw.tcss450.team2.weather;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import edu.uw.tcss450.team2.R;


/**
 * A fragment that lets users select points on a map to receive weather information about.
 *
 * @author Sam Spillers
 * @version 1.0
 */
public class ChooseLocationFragment extends Fragment implements LocationListener {

    private FusedLocationProviderClient fusedLocationProviderClient;
    private GoogleMap myGoogleMap;


    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            myGoogleMap = googleMap;
            Log.d("ChooseLocationFragment", "Map ready!");

            LatLng sydney = new LatLng(-34, 151);
            googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        }
    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_choose_location, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        Log.d("ChooseLocationFragment", "Permissions! fine location: " + ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) + ", coarse location: " + ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION));
        Log.d("ChooseLocationFragment", "Permissions! permission granted: " + PackageManager.PERMISSION_GRANTED + ", permission denied: " + PackageManager.PERMISSION_DENIED);
//        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//
//        }
//        fusedLocationProviderClient.getLastLocation().
//                .addOnSuccessListener(getActivity(), this);
    }
//
//    public void handleNewLocation(Location location) {
//        Log.d("ChooseLocationFragment", "New location found in handleNewLocation! Location: " + location + ", myGoogleMap: " + myGoogleMap);
//        if (location != null && myGoogleMap != null) {
//            LatLng newLocation = new LatLng(location.getLatitude(), location.getLongitude());
//            myGoogleMap.addMarker(new MarkerOptions().position(newLocation).title("Marker in current Location"));
//            myGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(newLocation));
//        }
//    }

    /**
     * Handles an update of the current location.
     * @param location The updated GPS location.
     *
     * @author Sam Spillers
     * @version 1.0
     */
    @Override
    public void onLocationChanged(@NonNull Location location) {
        Log.d("ChooseLocationFragment", "New location found in onLocationChanged! Location: " + location + ", myGoogleMap: " + myGoogleMap);
        if (location != null && myGoogleMap != null) {
            LatLng newLocation = new LatLng(location.getLatitude(), location.getLongitude());
            myGoogleMap.addMarker(new MarkerOptions().position(newLocation).title("Marker in current Location"));
            myGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(newLocation));
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}
    @Override
    public void onProviderEnabled(@NonNull String provider) {}
    @Override
    public void onProviderDisabled(@NonNull String provider) {}
}