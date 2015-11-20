package edu.sjsu.qi.skibuddy;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


public class ActivityUserProfile extends Activity {

    private static final String TAG = ActivityUserProfile.class.getSimpleName();

    ImageButton ibBack;
    Button btSignout;
    ItemUser queryResult = new ItemUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_user_profile);

        // Set up the action bar
        ActionBar actionBar = getActionBar();

        //Hiding ActionBar icon and title
        actionBar.setDisplayShowHomeEnabled(false);

        //Show the custom ActionBar with the icon_back
        View mActionBarView = getLayoutInflater().inflate(R.layout.action_bar_back,null);
        actionBar.setCustomView(mActionBarView);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        ibBack = (ImageButton) findViewById(R.id.button_back);
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Goto activity Category
                try {
                    Intent intent = new Intent(ActivityUserProfile.this, ActivityFragmentContainer.class);
                    startActivity(intent);
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
            }
        });


        //Signout button action
        btSignout = (Button) findViewById(R.id.button_SignOut);
        btSignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //TODO: Implement SignOut with Facebook account

                //return back to main activity
                try{
                    Intent intent = new Intent(ActivityUserProfile.this, MainActivity.class);
                    startActivity(intent);
                }catch(Exception e){
                    Log.e(TAG, e.toString());
                }
            }
        });


        //TODO: implement query user profile from facebook account and list here
        //The follow is hard code for example
        queryResult.setName("Qi Cao");
        queryResult.setThumbnailName("qi_thumbnail");
        queryResult.setTagline("Don't cry because it's over, smile because it happened.");

        updateUserProfile();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_user_profile, menu);
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


    private void updateUserProfile(){
        ImageView thumbnail = (ImageView) findViewById(R.id.user_thumbnail);
        TextView name = (TextView) findViewById(R.id.user_name);
        TextView tagline = (TextView) findViewById(R.id.user_Tagline);

        //thumbnail.setImage??
        name.setText(queryResult.getName());
        tagline.setText(queryResult.getTagline());

    }
}
