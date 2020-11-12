/*
   Author: Kim, Hyeong Suk, Spillers, Sam D, Tran, Anh Tu, Sambath Pech
   Class: TCSS 450
   Project: Chat application
   Description: this is a chat apllicaiton which the user can be able to register and connect
                to friends around the world
 */

package edu.uw.tcss450.team2;



import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.snackbar.Snackbar;

import edu.uw.tcss450.team2.chat.ChatMessage;
import edu.uw.tcss450.team2.chat.ChatViewModel;
import edu.uw.tcss450.team2.databinding.ActivityMainBinding;
import edu.uw.tcss450.team2.fragments.FriendFragment;
import edu.uw.tcss450.team2.fragments.GalleryFragment;
import edu.uw.tcss450.team2.fragments.HistoryFragment;
import edu.uw.tcss450.team2.fragments.LogoutFragment;
import edu.uw.tcss450.team2.fragments.MoreFragment;
import edu.uw.tcss450.team2.fragments.ProfileFragment;
import edu.uw.tcss450.team2.fragments.SettingFragment;
import edu.uw.tcss450.team2.model.NewMessageCountViewModel;
import edu.uw.tcss450.team2.model.UserInfoViewModel;
import edu.uw.tcss450.team2.services.PushReceiver;
import edu.uw.tcss450.team2.signin.SignInFragment;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private MainPushMessageReceiver mPushMessageReceiver;
    private NewMessageCountViewModel mNewMessageModel;

    private ActivityMainBinding binding;

    private AppBarConfiguration mAppBarConfiguration;
    Toolbar toolbar;
    DrawerLayout mDrawLayout;
    ActionBarDrawerToggle mDrawerToggle;
    private SignInFragment signInFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //binding = ActivityMainBinding.inflate(getLayoutInflater());

        //Find the toolbar Id
        //drawer toolbar
        toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        //Appbar id
        MaterialToolbar materialToolbar = findViewById(R.id.topBar);
        setSupportActionBar(materialToolbar);

        //main_layout is the id activity_main
        mDrawLayout = findViewById(R.id.main_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawLayout,
                toolbar, R.string.open, R.string.close);

        mDrawLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        NavigationView navigationView = findViewById(R.id.drawer_nav);
        navigationView.setNavigationItemSelectedListener(this);

        //NavigationView navigationView1 = findViewById(R.id.main_toolbar);


        MainActivityArgs args = MainActivityArgs.fromBundle(getIntent().getExtras());
        String email = args.getEmail();
        String jwt = args.getJwt();
//        String fName = args.getFName();
//        String lName = args.getLname();
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
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_home, R.id.navigation_chat, R.id.navigation_weather).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);


        mNewMessageModel = new ViewModelProvider(this).get(NewMessageCountViewModel.class);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.navigation_chat) {
                //When the user navigates to the chats page, reset the new message count.
                //This will need some extra logic for your project as it should have
                //multiple chat rooms.
                mNewMessageModel.reset();
            }
        });
        mNewMessageModel.addMessageCountObserver(this, count -> {
            /*
            BadgeDrawable badge = binding.navView.getOrCreateBadge(R.id.navigation_chat);
            badge.setMaxCharacterCount(2);
            if (count > 0) {
                //new messages! update and show the notification badge.
                badge.setNumber(count);
                badge.setVisible(true);
            } else {
                //user did some action to clear the new messages, remove the badge
                badge.clearNumber();
                badge.setVisible(false);
            }
             */
        });

    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController1 = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController1, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }



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
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(MainActivity.this);
                builder.setTitle("Log out?");
                builder.setMessage("This will log you out!");
                builder.setIcon(R.drawable.ic_baseline_error_24);
                builder.setBackground(getResources().getDrawable(R.drawable.drawable_dialog, null));
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showDrawerFragment(new LogoutFragment());
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();

                break;
            default:
                showDrawerFragment(new MoreFragment());
                break;
        }
        mDrawLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    /*
     * Helper class to navigate a item in menu to a new  fragment
     * @params: Fragement
     *
     */
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

    /**
     * A BroadcastReceiver that listens for messages sent from PushReceiver
     */
    private class MainPushMessageReceiver extends BroadcastReceiver {
        private ChatViewModel mModel =
                new ViewModelProvider(MainActivity.this)
                        .get(ChatViewModel.class);
        @Override
        public void onReceive(Context context, Intent intent) {
            NavController nc =
                    Navigation.findNavController(
                            MainActivity.this, R.id.nav_host_fragment);
            NavDestination nd = nc.getCurrentDestination();
            if (intent.hasExtra("chatMessage")) {
                ChatMessage cm = (ChatMessage) intent.getSerializableExtra("chatMessage");
                //If the user is not on the chat screen, update the
                // NewMessageCountView Model
                if (nd.getId() != R.id.navigation_chat) {
                    mNewMessageModel.increment();
                }
                //Inform the view model holding chatroom messages of the new
                //message.
                mModel.addMessage(intent.getIntExtra("chatid", -1), cm);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPushMessageReceiver == null) {
            mPushMessageReceiver = new MainPushMessageReceiver();
        }
        IntentFilter iFilter = new IntentFilter(PushReceiver.RECEIVED_NEW_MESSAGE);
        registerReceiver(mPushMessageReceiver, iFilter);
    }
    @Override
    public void onPause() {
        super.onPause();
        if (mPushMessageReceiver != null){
            unregisterReceiver(mPushMessageReceiver);
        }
    }



}