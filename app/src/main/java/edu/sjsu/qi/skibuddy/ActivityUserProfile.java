package edu.sjsu.qi.skibuddy;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
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

import com.parse.ParseUser;


public class ActivityUserProfile extends Activity {

    private static final String TAG = ActivityUserProfile.class.getSimpleName();

    private String username = null;
    private String facebookId = null;
    private String photoURL = null;

    ImageView thumbnailImage;
    TextView tvName;

    ImageButton ibBack;
    Button btSignout;
    ItemUser currentUser = new ItemUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get User name, ID, photoURL from ActivityFragmentContainer

        Bundle extras = getIntent().getExtras();
        username = extras.getString("UserName");
        facebookId = extras.getString("FacebookId");
        photoURL = extras.getString("PhotoURL");

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

                ParseUser.logOut();

                //return back to main activity
                try{
                    Intent intent = new Intent(ActivityUserProfile.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }catch(Exception e){
                    Log.e(TAG, e.toString());
                }
            }
        });


        //Update current user name, id, photo
        currentUser.setName(username);
        currentUser.setUserId(facebookId);
        currentUser.setThumbnailURL(photoURL);

        updateUserProfile();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_user_profile, menu);
        return true;
    }

    private void updateUserProfile(){

        thumbnailImage = (ImageView) findViewById(R.id.user_thumbnail);
        tvName = (TextView) findViewById(R.id.user_name);

        //AsyncTask to download user photo
        DownloadPhotoAsync photoAsync = new DownloadPhotoAsync();
        photoAsync.execute();
    }

    //New class called ProfilePhotoAsync and make it extend AsyncTask
    //Download profile photo from giving URL.
    class DownloadPhotoAsync extends AsyncTask<String, String, String> {
        public Bitmap bitmap;
        @Override
        protected String doInBackground(String... params) {
            // Fetching data from URI and storing in bitmap
            bitmap = currentUser.DownloadImageBitmap(currentUser.getThumbnailURL());
            Log.d(TAG, "IMAGE finished download giving URL");
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            tvName.setText(currentUser.getName());
            thumbnailImage.setImageBitmap(bitmap);
        }
    }

}
