package edu.sjsu.qi.skibuddy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONException;

import java.util.Arrays;
import java.util.List;


public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private ImageButton bt_login_facebook;
    private String name = null;
    private String email = null;
    private String facebookId = null;
    private String photoURL = null;
    private Profile mFbProfile;
    private ParseUser parseUser;

    private Button bt_test;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFbProfile = Profile.getCurrentProfile();

        //The following code is for test button without login
        bt_test = (Button)findViewById(R.id.bt_test);
        bt_test.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Just click");
                ParseUser.logInInBackground("lingqian", "test", new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {

                        if (e == null) {
                            Intent intent = new Intent(MainActivity.this, ActivityFragmentContainer.class);
                            intent.putExtra("UserName", "Raymond N Sophia");
                            intent.putExtra("FacebookId", "844661355653516");
                            intent.putExtra("PhotoURL",
                                    "https://graph.facebook.com/844661355653516/picture?height=200&width=200&migration_overrides=%7Boctober_2012%3Atrue%7D");
                            startActivity(intent);
                        } else {
                            Log.d(TAG, "error login");
                        }
                    }
                });
            }
        });


        bt_login_facebook = (ImageButton)findViewById(R.id.button_login_facebook);

        //Action of Facebook Login Button
        bt_login_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Login with facebook account

                List<String> permissions = Arrays.asList("public_profile", "email");

                ParseFacebookUtils.logInWithReadPermissionsInBackground(MainActivity.this,
                        permissions, new LogInCallback() {
                            @Override
                            public void done(ParseUser user, ParseException e) {

                                if (user == null) {
                                    Log.d(TAG, "The user cancelled the Facebook login.");
                                } else if (user.isNew()) {
                                    Log.d(TAG, "User signed up and logged in through Facebook.");
                                    getUserDetailsFromFB();
                                } else {
                                    Log.d(TAG, "User logged in through Facebook.");
                                    getUserDetailsFromParse();
                                }
                            }
                        });
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }

    //getUserDetailsFromFB() method will fetch Facebook data.
    private void getUserDetailsFromFB(){

        //fetch sorts of data from Facebook
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        //handle the result
                        try {
                            //Todo, need fix bug of  org.json.JSONException: No value for email
                            //email = response.getJSONObject().getString("email");
                            name = response.getJSONObject().getString("name");
                            facebookId = response.getJSONObject().getString("id");

                            //Get user profile photo Url from Facebook
                            mFbProfile = Profile.getCurrentProfile();
                            photoURL = mFbProfile.getProfilePictureUri(200, 200).toString();

                            //requested data has been fetched then store data in Parse
                            saveNewUserToParse();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).executeAsync();
    }

    //Store the data fetched from facebook to Parse
    private void saveNewUserToParse(){
        parseUser = ParseUser.getCurrentUser();

        //currentUser.setEmail(email);
        parseUser.setUsername(name);
        parseUser.put("FacebookId", facebookId);
        parseUser.put("PhotoURL", photoURL);

        //Save all the user details
        parseUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

                //after saved user details, go to ActivityFragmentContainer
                Toast.makeText(getApplicationContext(), "New user:" + name + " Signed up",
                        Toast.LENGTH_LONG).show();
                try {
                    startActivityFragmentContainer();

                } catch (Exception err) {
                    Log.e(TAG, err.toString());
                }
            }
        });
    }


    private void getUserDetailsFromParse(){
        parseUser = ParseUser.getCurrentUser();

        //user profile already saved in Parse
        name = parseUser.getUsername();
        facebookId = parseUser.get("FacebookId").toString();
        photoURL = parseUser.get("PhotoURL").toString();

        Toast.makeText(getApplicationContext(), "Welcome back " + name,
                Toast.LENGTH_LONG).show();

        try{
            startActivityFragmentContainer();

        }catch(Exception err){
            Log.e(TAG,err.toString());
        }
    }

    private void startActivityFragmentContainer(){
        Intent intent = new Intent(MainActivity.this, ActivityFragmentContainer.class);
        intent.putExtra("UserName", name);
        intent.putExtra("FacebookId", facebookId);
        intent.putExtra("PhotoURL", photoURL);
        startActivity(intent);
    }
}
