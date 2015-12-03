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
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class ActivityEventDetail extends Activity {

    private static final String TAG = ActivityEventDetail.class.getSimpleName();

    private String eventTitle = null;
    private ImageButton ibBack;
    private Button btMap;
    private Event event;
    private List<ParseUser> listUsers;

    private TextView tvTitle, tvStart, tvEnd, tvDes;
    private ListView lvUsersFound;
    private ImageView image;
    private TextView tvName;
    ParseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get Event ID from EventFragment
        Bundle extras = getIntent().getExtras();
        eventTitle = extras.getString("Event_Title");
        setContentView(R.layout.activity_event_detail);

        tvTitle = (TextView)findViewById(R.id.textView_titleContent);
        tvStart = (TextView)findViewById(R.id.textView_StartTime);
        tvEnd = (TextView)findViewById(R.id.textView_EndTime);
        tvDes = (TextView)findViewById(R.id.textView_DesContent);
        lvUsersFound = (ListView)findViewById(R.id.listView_user);

        queryEventDetailFromParse();

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

        btMap = (Button)findViewById(R.id.button_map);
        btMap.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Goto activity showEventUsersMap
                try {
                    Intent intent = new Intent(ActivityEventDetail.this, ActivityShowEventUsersMap.class);
                    intent.putExtra("Event_Title", eventTitle);
                    startActivity(intent);

                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
            }
        });

        /*
        //sets the OnItemClickListener of the ListView of users
        //so user can click on a user and get user details
        lvUsersFound.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> av, View v, int pos, long id) {
                //may need start new activity
            }
        }); */

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_event_detail, menu);
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
    public void queryEventDetailFromParse(){

        //Create query for objects of type "Event"
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Event");
        query.whereEqualTo("EventTitle", eventTitle);

        // Run the query
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> eventList, ParseException e) {
                if(e == null){
                    //get event details
                    event = new Event();
                    event = (Event)eventList.get(0);

                    listUsers = new ArrayList<>();
                    listUsers.clear();
                    for(ParseObject event : eventList){
                        listUsers.add(((Event)event).getAuthor());
                    }
                    updateEventsDetail();

                }else{
                    Log.d(TAG, "Event retrieval error: " + e.getMessage());
                }
            }
        });
    }

    public void updateEventsDetail(){

        tvTitle.setText(event.getEventTitle());
        tvStart.setText(event.getStartTime());
        tvEnd.setText(event.getEndTime());
        tvDes.setText(event.getDescription());

        //create an ArrayAdapter
        ArrayAdapter<ParseUser> adapter = new ArrayAdapter<ParseUser>(getApplicationContext(),
                R.layout.listview_user_item, listUsers){

            @Override
            public View getView(int position, View convertView, ViewGroup parent){

                if(convertView == null){
                    convertView = getLayoutInflater().inflate(R.layout.listview_user_item, parent, false);
                }
                image = (ImageView)convertView.findViewById(R.id.user_thumbnail);
                tvName = (TextView)convertView.findViewById(R.id.user_name);
                user = listUsers.get(position);
                updateUserProfile();
                return convertView;
            }
        };
        //Assign adapter to ListView
        lvUsersFound.setAdapter(adapter);

    }

    private void updateUserProfile(){
        //AsyncTask to download user photo
        DownloadPhotoAsync photoAsync = new DownloadPhotoAsync();
        photoAsync.execute();
    }

    //New class called ProfilePhotoAsync and make it extend AsyncTask
    //Download profile photo from giving URL.
    class DownloadPhotoAsync extends AsyncTask<String, String, String> {
        public Bitmap bitmap;
        public User myUser = new User();
        @Override
        protected String doInBackground(String... params) {
            // Fetching data from URI and storing in bitmap
            bitmap = myUser.DownloadImageBitmap(user.get("PhotoURL").toString());
            Log.d(TAG, "IMAGE finished download giving URL");
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            tvName.setText(user.getUsername());
            image.setImageBitmap(bitmap);
        }
    }
}
