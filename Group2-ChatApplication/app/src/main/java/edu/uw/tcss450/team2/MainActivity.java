package edu.uw.tcss450.team2;


import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
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

import android.annotation.SuppressLint;
import android.content.Intent;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;


import edu.uw.tcss450.team2.chat.ChatMessage;
import edu.uw.tcss450.team2.chat.ChatViewModel;

import edu.uw.tcss450.team2.databinding.ActivityMainBinding;
import edu.uw.tcss450.team2.databinding.FragmentHomeBinding;
import edu.uw.tcss450.team2.model.NewMessageCountViewModel;
import edu.uw.tcss450.team2.model.UserInfoViewModel;
import edu.uw.tcss450.team2.services.PushReceiver;
import edu.uw.tcss450.team2.signin.SignInFragment;

import edu.uw.tcss450.team2.model.UserInfoViewModel;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Date;

import edu.uw.tcss450.team2.model.UserInfoViewModel;
import edu.uw.tcss450.team2.signin.SignInFragment;

public class MainActivity extends AppCompatActivity {

    private MainPushMessageReceiver mPushMessageReceiver;
    private NewMessageCountViewModel mNewMessageModel;
    private AppBarConfiguration mAppBarConfiguration;
    private Switch mSwitch;
    private FragmentHomeBinding binding;
    private SwitchCompat switchCompat;
    private SharedPreferences sharedPreferences = null;
    //    private DrawerLayout mDrawLayout;
//    private ActionBarDrawerToggle mDrawerToggle;
//    private SignInFragment signInFragment;

    private String email;
    private String jwt;
    private int memberId;
    int messageCount;



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
//TODO need to put it back at appropriate location
//        aSwitch = (Switch)findViewById(R.id.switch_button);
//        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
//            aSwitch.setChecked(true);
//        }
//        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//                    // restartApp();
//                } else {
//                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//                    //restartApp();
//                }
//            }
//        });

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
        memberId = args.getMemberId();

         UserInfoViewModel tempUserViewModel = new ViewModelProvider(this, new UserInfoViewModel.UserInfoViewModelFactory(email, jwt, memberId)).
                get(UserInfoViewModel.class);


        //TODO 1. mock up notification in home page - need to replace with actual notification
        BottomNavigationView navView = findViewById(R.id.nav_view);
<<<<<<< HEAD
//        BadgeDrawable badgeDrawable = navView.getOrCreateBadge(R.id.navigation_chat);
//        badgeDrawable.setVisible(true);
//        badgeDrawable.setNumber(7);
=======

        BadgeDrawable badgeDrawable = navView.getOrCreateBadge(R.id.navigation_chat);
        badgeDrawable.setVisible(true);
        badgeDrawable.setNumber(2);
>>>>>>> f193c243e71dc3a40664b2dc7049d0c0f348e234

//        BadgeDrawable badgeDrawable = navView.getOrCreateBadge(R.id.navigation_chat);
//        badgeDrawable.setVisible(true);
//        badgeDrawable.setNumber(7);


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_home,
                R.id.navigation_chat, R.id.navigation_contact, R.id.navigation_find_friend, R.id.navigation_weather).build();
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
            //TODO this place causes an error
            //BadgeDrawable badge = binding.navView.getOrCreateBadge(R.id.navigation_chat);
            BadgeDrawable badge = navView.getOrCreateBadge(R.id.navigation_chat);
            //BadgeDrawable badge2 = navView.getOrCreateBadge(R.id.image_chatIcon);
            badge.setMaxCharacterCount(2);

            //TODO need to be erased
            System.out.println("Incoming Message Count: "+ count);

//            R.id.text_unreadChat

            tempUserViewModel.setUnreadMessageCount(count);

            if (count > 0) {
                //new messages! update and show the notification badge.
                badge.setNumber(count);
                badge.setVisible(true);

//                badge2.setNumber(count);
//                badge2.setVisible(true);
            } else {
                //user did some action to clear the new messages, remove the badge
                badge.clearNumber();
                badge.setVisible(false);

//                badge2.setNumber(count);
//                badge2.setVisible(true);
            }


        });
        //switchTheme();
    }



    private void switchTheme() {


        switchCompat = findViewById(R.id.switch_button);
        sharedPreferences = getSharedPreferences("night", 0);
        boolean booleanValue = sharedPreferences.getBoolean("night mode", true);
        if (booleanValue) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            switchCompat.setChecked(true);
        }

        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    switchCompat.setChecked(true);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("night node", true);
                    editor.commit();
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    switchCompat.setChecked(true);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("night node", false);
                    editor.commit();
                }
            }
        });
//
//        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
//            setTheme(R.style.DarkTheme);
//        } else {
//            setTheme(R.style.LightTheme);
//        }
//        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
//            mSwitch.setChecked(true);
//        }
//        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//                } else {
//                    getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//                }
//            }
//        });
    }


    public void restartApp() {
    Intent i = new Intent(getApplicationContext(), MainActivity.class);
    startActivity(i);
    finish();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.notification_bar:
                Toast.makeText(this, "Notificaiton", Toast.LENGTH_SHORT).show();
                break;
            case R.id.profile:
                //createFragment(new ProfileFragment());
                break;
            case R.id.log_out:
                Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
                signOut();
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

    /**
     * A BroadcastReceiver that listens for messages sent from PushReceiver
     */
    private class MainPushMessageReceiver extends BroadcastReceiver {
        private ChatViewModel mModel =
                new ViewModelProvider(MainActivity.this)
                        .get(ChatViewModel.class);

        private UserInfoViewModel userInfoViewModel =
                new ViewModelProvider(MainActivity.this).get(UserInfoViewModel.class);

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

                cm.setDateReceived(new Date());
                int chatId = intent.getIntExtra("chatid", -1);

                userInfoViewModel.getChatRoomsIdForNewMessage().
                        put(chatId, cm);

                if(nd.getId() == R.id.navigation_chat) {
                    userInfoViewModel.getChatListViewModel().connectGet(jwt, email);
                }
                else if (nd.getId() == R.id.personalChatFragment && userInfoViewModel.getCurrentChatRoom() == chatId) {
                    userInfoViewModel.getChatRoomsIdForNewMessage().remove(new Integer(chatId));
                }
                else {
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

    private void signOut() {
        SharedPreferences prefs = getSharedPreferences(
                getString(R.string.keys_shared_prefs),
                Context.MODE_PRIVATE);
        prefs.edit().remove(getString(R.string.keys_prefs_jwt)).apply();
        Intent i = new Intent(getApplicationContext(), AuthActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        //finishAndRemoveTask();
    }


    private void createFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void onDestroy() {
        super.onDestroy();
        binding = null;
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
////
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