package edu.uw.tcss450.team2;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import edu.uw.tcss450.team2.databinding.FragmentHomeBinding;
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
public class HomeFragment extends Fragment implements OnMapReadyCallback {
    //Initialize Map variables
    private static HomeFragment INSTANCE = null;
    View view;
    GoogleMap map;
    MapView mapView;


    /**
     * Empty constructor
     *
     */
    public HomeFragment() {

    }


    /**
     * Returns an instance when called.
     *
     * @return new Fragment if Instance is null, other wise returns instance as it is
     */
    public static HomeFragment getINSTANCE() {
        if(INSTANCE == null) {
            INSTANCE = new HomeFragment();
        }
        return INSTANCE;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }


    /**
     * In onViewCreated, first it received user email string and shows it in the application top.
     * Second, it would need to handle (TODO) hit API and retireve data for live weather forecating.
     *
     */
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



        binding.textEmail.setText("Welcome Home " + model.getEmail() + "!");

        //TODO(flag) handling map
        mapView = view.findViewById(R.id.mapsView);
        if(mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }

        //TODO handling notification - need to replace these with actual messages not mock up data
        binding.textPubdate.setText("You have 7 Unread Messages");
//        binding.textTitle.setText("Hey, pls dont forget to ... - sambath777");
        binding.buttonFullPost.setOnClickListener(button ->
                Navigation.findNavController(getView()).navigate(
                    HomeFragmentDirections.actionNavigationHomeToNavigationChat()));
        binding.buttonHomeWeather.setOnClickListener(button ->
                Navigation.findNavController(getView()).navigate(
                    HomeFragmentDirections.actionNavigationHomeToNavigationWeather()));





        //On button click, navigate to Second Home
//        binding.buttonNext.setOnClickListener(button ->
//                Navigation.findNavController(getView()).navigate(
//                        HomeFragmentDirections
//                                .actionHomeFragmentToSecondHomeFragment()));
    }

    /**
     * It checks the currentLocation of the User to find lat/long.
     * this will be modified and used later when hitting Weather API
     * with lat/long endpoint.
     */
//    private void getCurrentLocation() {
//        //Initializing task location
//        Task<Location> task = client.getLastLocation();
//        task.addOnSuccessListener(new OnSuccessListener<Location>() {
//            @Override
//            public void onSuccess(Location location) {
//                if(location != null) {
//                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
//                        @Override
//                        public void onMapReady(GoogleMap googleMap) {
//                            LatLng latLng = new LatLng(location.getLatitude(),
//                                    location.getLongitude());
//                            MarkerOptions options = new MarkerOptions().position(latLng)
//                                    .title("I am here");
//
//                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
//
//                            googleMap.addMarker(options);
//
//                        }
//                    });
//                }
//            }
//        });
//
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        if(requestCode == 44) {
//            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                getCurrentLocation();
//            }
//        }
//    }

    /**
     * @param googleMap when google map is ready it will update
     *        map variable with newly updated googleMap data.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        System.out.println("onMapReady triggered");
        MapsInitializer.initialize(getContext());
        map = googleMap;
    }
}