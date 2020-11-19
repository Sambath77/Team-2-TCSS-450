package edu.uw.tcss450.team2;



import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.ActionBar;


import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import android.annotation.SuppressLint;
import android.content.Intent;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;



import android.view.View;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;

//import edu.uw.tcss450.team2.fragments.FriendFragment;
//import edu.uw.tcss450.team2.fragments.GalleryFragment;
import edu.uw.tcss450.team2.fragments.HistoryFragment;
import edu.uw.tcss450.team2.fragments.LogoutFragment;
//import edu.uw.tcss450.team2.fragments.MoreFragment;
//import edu.uw.tcss450.team2.fragments.ProfileFragment;
import edu.uw.tcss450.team2.fragments.SettingFragment;
import edu.uw.tcss450.team2.model.UserInfoViewModel;
import edu.uw.tcss450.team2.signin.SignInFragment;
import edu.uw.tcss450.team2.utils.GpsTracker;
//import im.delight.android.location.SimpleLocation;
import android.widget.Toast;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import edu.uw.tcss450.team2.model.UserInfoViewModel;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import edu.uw.tcss450.team2.model.UserInfoViewModel;


public class MainActivity extends AppCompatActivity /*implements NavigationView.OnNavigationItemSelectedListener*/ {

    private AppBarConfiguration mAppBarConfiguration;

    Toolbar toolbar;
    DrawerLayout mDrawLayout;
    ActionBarDrawerToggle mDrawerToggle;
    private SignInFragment signInFragment;
    //    private GpsTracker gpsTracker;
//    private TextView tvLatitude,tvLongitude;
    double longitude;
    double latitude;
    //private SimpleLocation location;

    private Switch aSwitch;

//    private DrawerLayout mDrawLayout;
//    private ActionBarDrawerToggle mDrawerToggle;
//    private SignInFragment signInFragment;
    String email;
    String jwt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("WorldChat");
        actionBar.setIcon(R.drawable.ic_world_language_24);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.Theme_WeatherApp);
        }

        aSwitch = (Switch)findViewById(R.id.switch_button);
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            aSwitch.setChecked(true);
        }
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                   // restartApp();
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    //restartApp();
                }
            }
        });
        //Appbar id
//        MaterialToolbar materialToolbar = findViewById(R.id.topBar);
//        setSupportActionBar(materialToolbar);

        //main_layout is the id activity_main
//        mDrawLayout = findViewById(R.id.main_layout);
//        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawLayout,
//                toolbar, R.string.open, R.string.close);

//        mDrawLayout.addDrawerListener(mDrawerToggle);
//        mDrawerToggle.syncState();

        //NavigationView navigationView = findViewById(R.id.drawer_nav);
        //navigationView.setNavigationItemSelectedListener(this);


        MainActivityArgs args = MainActivityArgs.fromBundle(getIntent().getExtras());
        email = args.getEmail();
        jwt = args.getJwt();
        new ViewModelProvider(this, new UserInfoViewModel.UserInfoViewModelFactory(email, jwt)).
                get(UserInfoViewModel.class);

//        new ViewModelProvider(this, new UserInfoViewModel.UserInfoViewModelFactory(email, jwt, fName, lName)).
//                get(UserInfoViewModel.class);
//        Log.d("ACTIVITY", email);
//        new ViewModelProvider(this, new UserInfoViewModel.UserInfoViewModelFactory(email, jwt)).
//                get(UserInfoViewModel.class);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        BadgeDrawable badgeDrawable = navView.getOrCreateBadge(R.id.navigation_chat);
        badgeDrawable.setVisible(true);
        badgeDrawable.setNumber(7);


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_home,
                R.id.navigation_chat, R.id.navigation_contact, R.id.navigation_weather).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);


//        //TODO handlong getting current lat/long
//        tvLatitude = (TextView)findViewById(R.id.latitude);
//        tvLongitude = (TextView)findViewById(R.id.longitude);
//        try {
//            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
//                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
//            }
//        } catch (Exception e){
//            e.printStackTrace();
//        }

        // construct a new instance of SimpleLocation
//        location = new SimpleLocation(this);
//
//        // if we can't access the location yet
//        if (!location.hasLocationEnabled()) {
//            // ask the user to enable location access
//            SimpleLocation.openSettings(this);
//        }
//        latitude = location.getLatitude();
//        longitude = location.getLongitude();
//        System.out.println("lat/long: " + latitude + longitude);

//        findViewById(R.id.navigation_home).setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                final double latitude = location.getLatitude();
//                final double longitude = location.getLongitude();
//                System.out.println("lat/long: " + latitude + longitude);
//                // TODO
//            }
//
//        });


    }

//    //TODO added by Hyeong, helper method for getting current lat/long
//    public void getLocation(View view){
//        gpsTracker = new GpsTracker(MainActivity.this);
//        if(gpsTracker.canGetLocation()){
//            double latitude = gpsTracker.getLatitude();
//            double longitude = gpsTracker.getLongitude();
//
//            //System.out.println("lat/long = " + latitude + ", " + longitude);
//            tvLatitude.setText(String.valueOf(latitude));
//            tvLongitude.setText(String.valueOf(longitude));
//        }else{
//            gpsTracker.showSettingsAlert();
//        }


//    }

//    public void restartApp() {
//        Intent i = new Intent(getApplicationContext(), MainActivity.class);
//        startActivity(i);
//        finish();
//    }
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.app_bar_search:
                Toast.makeText(this, "Seach", Toast.LENGTH_SHORT).show();
                break;
            case R.id.notification_bar:
                Toast.makeText(this, "Notificaiton", Toast.LENGTH_SHORT).show();
                break;
            case R.id.profile:
                //startActivity(new Intent(MainActivity.this, ProfileAcitvity.class));
                break;
            case R.id.log_out:
                Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController1 = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController1, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}



//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//
//        int id = item.getItemId();
//        switch (id) {
//            case R.id.profile:
//                startActivity(new Intent(this, Profile.class));
//                break;
//            case R.id.friend:
//                showDrawerFragment(new FriendFragment());
//                break;
//            case R.id.log_out:
//               // showDrawerFragment(new LogoutFragment());
//                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(MainActivity.this);
//                builder.setTitle("Log out?");
//                builder.setMessage("This will log you out!");
//                builder.setIcon(R.drawable.ic_baseline_error_24);
//                builder.setBackground(getResources().getDrawable(R.drawable.drawable_dialog, null));
//                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        showDrawerFragment(new LogoutFragment());
//                    }
//                });
//                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                });
//                builder.show();
//                break;
//        }
//        mDrawLayout.closeDrawer(GravityCompat.START);
//        return true;
//    }
//
/*
 * Helper class to navigate a item in menu to a new  fragment
 * @params: Fragement
 *
 */
//    private void showDrawerFragment(Fragment fragment) {
//        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.replace(R.id.main_layout, fragment);
//        fragmentTransaction.commit();
//    }
//
//
//    @Override
//    public void onBackPressed() {
//        if (mDrawLayout.isDrawerOpen(GravityCompat.START)) {
//            mDrawLayout.closeDrawer(GravityCompat.START);
//
//        } else {
//            super.onBackPressed();
//        }
//    }
//    }
