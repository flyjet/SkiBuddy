package edu.sjsu.qi.skibuddy;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;


public class ActivityTrackDetail extends Activity {

    private static final String TAG = ActivityTrackDetail.class.getSimpleName();

    private String trackTitle;
    private String imageURL;
    private ImageButton ibBack;
    private TextView tvName;
    private ImageView ivMap;
    private Track track;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_detail);

        //Get Track Name from TrackFragment
        Bundle extras = getIntent().getExtras();
        trackTitle = extras.getString("Track_Title");

        tvName = (TextView)findViewById(R.id.textView_recordName);
        tvName.setText(trackTitle);

        ivMap = (ImageView)findViewById(R.id.imageView_map);

        queryTrackDetailFromParse();

        // Set up the action bar
        ActionBar actionBar = getActionBar();

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
                    finish();
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_track_detail, menu);
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

    //query event detail by event title
    public void queryTrackDetailFromParse(){

        //Create query for objects of type "Event"
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Track");
        query.whereEqualTo("TrackTitle", trackTitle);

        // Run the query
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> trackList, ParseException e) {
                if (e == null) {
                    //get event details
                    track = new Track();
                    track = (Track) trackList.get(0);
                    //eventList size is right
                    imageURL = track.getImageURL();
                    updateTrackDetail();

                } else {
                    Log.d(TAG, "Event retrieval error: " + e.getMessage());
                }
            }
        });
    }

    //update TrackDetail UI
    public void updateTrackDetail(){

        DownloadImageAsync imageAsync = new DownloadImageAsync();
        imageAsync.execute();
    }

    //New class called ProfilePhotoAsync and make it extend AsyncTask
    //Download profile photo from giving URL.
    class DownloadImageAsync extends AsyncTask<String, String, String> {
        public Bitmap bitmap;
        @Override
        protected String doInBackground(String... params) {
            // Fetching data from URI and storing in bitmap

            try {
                URL aURL = new URL(imageURL);
                URLConnection conn = aURL.openConnection();
                conn.connect();
                InputStream is = conn.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(is);
                bitmap = BitmapFactory.decodeStream(bis);
                bis.close();
                is.close();
            } catch (IOException e) {
                Log.e("IMAGE", "Error getting bitmap", e);
            }
            Log.d(TAG, "IMAGE finished download giving URL");
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            ivMap.setImageBitmap(bitmap);
        }
    }


}
