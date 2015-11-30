package edu.sjsu.qi.skibuddy;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;


public class ActivityFragmentContainer extends Activity {
    //This is the container for two tap of fragments (Event and Track)

    private static final String TAG = ActivityFragmentContainer.class.getSimpleName();

    private String name = null;
    private String facebookId = null;
    private String photoURL = null;

    ImageButton ibUser;

    //Declaring two tabs and corresponding fragments
    Tab eventTab, trackTab;
    Fragment eventFragment = new EventFragment();
    Fragment trackFragment = new TrackFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get User name, ID, photoURL from main activity
        Bundle extras = getIntent().getExtras();
        name = extras.getString("UserName");
        facebookId = extras.getString("FacebookId");
        photoURL = extras.getString("PhotoURL");

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_fragment_container);

        // Set up the action bar
        ActionBar actionBar = getActionBar();

        //Hiding ActionBar icon and title
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);

        //Show the custom ActionBar with the icon_user
        View mActionBarView = getLayoutInflater().inflate(R.layout.action_bar_user,null);
        actionBar.setCustomView(mActionBarView);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        ibUser = (ImageButton) findViewById(R.id.button_user);

        //click user button to show user profile and Logout
        ibUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Goto activity Category
                try {
                    Intent intent = new Intent(ActivityFragmentContainer.this, ActivityUserProfile.class);
                    intent.putExtra("UserName", name);
                    intent.putExtra("FacebookId", facebookId);
                    intent.putExtra("PhotoURL", photoURL);
                    startActivity(intent);
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
            }
        });

        //Creating ActionBar tabs
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        //Setting tabs
        eventTab = actionBar.newTab().setText("EVENT");
        trackTab = actionBar.newTab().setText("SKI TRACK");

        //Setting tab listeners
        eventTab.setTabListener(new TabListener(eventFragment));
        trackTab.setTabListener(new TabListener(trackFragment));

        //Adding tabs to the ActionBar
        actionBar.addTab(eventTab);
        actionBar.addTab(trackTab);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_fragment_container, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }


    //TabListener
    public class TabListener implements ActionBar.TabListener{

        private Fragment fragment;

        //Constructor
        public TabListener(Fragment fragment){
            this.fragment = fragment;
        }

        //When a tab is tapped, the FragmentTransaction replaces the content of FragmentContainer layout
        @Override
        public void onTabSelected(Tab tab, FragmentTransaction ft){
            ft.replace(R.id.activity_fragment_container, fragment);
        }

        @Override
        public void onTabUnselected(Tab tab, FragmentTransaction ft){
            ft.remove(fragment);
        }

        @Override
        public void onTabReselected(Tab tab, FragmentTransaction ft) {
        }
    }
}
