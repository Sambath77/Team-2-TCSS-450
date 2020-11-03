package edu.uw.tcss450.team2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import edu.uw.tcss450.team2.fragments.FriendFragment;
import edu.uw.tcss450.team2.fragments.GalleryFragment;
import edu.uw.tcss450.team2.fragments.HistoryFragment;
import edu.uw.tcss450.team2.fragments.LogoutFragment;
import edu.uw.tcss450.team2.fragments.MoreFragment;
import edu.uw.tcss450.team2.fragments.ProfileFragment;
import edu.uw.tcss450.team2.fragments.SettingFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private AppBarConfiguration mAppBarConfiguration;
    Toolbar toolbar, topBar;
    DrawerLayout mDrawLayout;
    ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Find the toolbar Id
        //drawer toolbar
        toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        //Appbar id
        topBar = findViewById(R.id.main_toolbar);
        setSupportActionBar(topBar);

        //main_layout is the id activity_main
        mDrawLayout = findViewById(R.id.main_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawLayout,
                toolbar, R.string.open, R.string.close);

        mDrawLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        NavigationView navigationView = findViewById(R.id.drawer_nav);
        navigationView.setNavigationItemSelectedListener(this);

        //NavigationView navigationView1 = findViewById(R.id.main_toolbar);


        //MainActivityArgs args = MainActivityArgs.fromBundle(getIntent().getExtras());
        //String email = args.getEmail();
        //String jwt = args.getJwt();
        //UserInfoViewModel model = new ViewModelProvider(this).get(UserInfoViewModel.class);
        //model.getmEmail();

//        Log.d("ACTIVITY", email);
//        new ViewModelProvider(this, new UserInfoViewModel.UserInfoViewModelFactory(email, jwt)).
//                get(UserInfoViewModel.class);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        BadgeDrawable badgeDrawable = navView.getOrCreateBadge(R.id.navigation_chat);
        badgeDrawable.setVisible(true);
        badgeDrawable.setNumber(7);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_home, R.id.navigation_chat, R.id.navigation_weather).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController1 = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController1, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.top_menu, menu);
//        return true;
//    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.profile:
                showDrawerFragment(new ProfileFragment());
                break;
            case R.id.friend:
                showDrawerFragment(new FriendFragment());
                break;
            case R.id.gallery:
                showDrawerFragment(new GalleryFragment());
                break;
            case R.id.history:
                showDrawerFragment(new HistoryFragment());
                break;
            case R.id.setting:
                showDrawerFragment(new SettingFragment());
                break;
            case R.id.log_out:
                showDrawerFragment(new LogoutFragment());
                break;
            default:
                showDrawerFragment(new MoreFragment());
                break;
        }
        mDrawLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showDrawerFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (mDrawLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawLayout.closeDrawer(GravityCompat.START);

        } else {
            super.onBackPressed();
        }
    }


}