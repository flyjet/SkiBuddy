package edu.sjsu.qi.skibuddy;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


public class ActivityTrackContainer extends Activity {

    private static final String TAG = ActivityTrackContainer.class.getSimpleName();

    private String userName = null;
    ImageButton ibBack;

    private ListView tracksFound;
    private List<Track> listTracks = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_container);


        Bundle extras = getIntent().getExtras();
        userName = extras.getString("UserName");

        tracksFound = (ListView)findViewById(R.id.listView_track);
        queryTrackListFromParse();

        //sets the OnItemClickListener of the ListView
        //so user can click on a event and get Track details
        tracksFound.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> av, View v, int pos, long id) {
                Intent intent = new Intent(ActivityTrackContainer.this, ActivityTrackDetail.class);
                intent.putExtra("Track_Title", listTracks.get(pos).getTrackTitle());
                startActivity(intent);
            }
        });



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
        getMenuInflater().inflate(R.menu.menu_activity_track_container, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    //query Events for current user
    private void queryTrackListFromParse(){

        //Create query for objects of type "Track"
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Track");
        // Restrict to cases where the author is the current user.
        //pass in a ParseUser and not String of that user

        //Todo: have bug with query user by userName

        query.whereEqualTo("UserName", userName);
        query.orderByAscending("createAt");

        // Run the query
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> trackList, ParseException e) {
                if (e == null) {
                    // If there are results, update the list of event and notify the adapter
                    listTracks.clear();
                    for (ParseObject track : trackList) {
                        listTracks.add((Track) track);
                    }
                    updateTracksList();

                } else {
                    Log.d(TAG, "Event retrieval error: " + e.getMessage());
                }
            }
        });
    }

    //Use ArrayAdapter and pass it to ListView to display track results
    //In the getView method, inflate the listview_track_item.xml layout and update its view
    private void updateTracksList(){
        //create an ArrayAdapter
        ArrayAdapter<Track> adapter = new ArrayAdapter<Track>(getApplicationContext(),
                R.layout.listview_track_item, listTracks){

            @Override
            public View getView(int position, View convertView, ViewGroup parent){

                if(convertView == null){
                    convertView = getLayoutInflater().inflate(R.layout.listview_track_item, parent, false);
                }

                TextView tvTitle = (TextView)convertView.findViewById(R.id.textView_trackName);
                TextView tvTime = (TextView)convertView.findViewById(R.id.textView_trackTime);

                Track track = listTracks.get(position);

                tvTitle.setText(track.getTrackTitle());
                tvTime.setText(track.getTime());
                return convertView;
            }
        };
        //Assign adapter to ListView
        tracksFound.setAdapter(adapter);
    }
}
