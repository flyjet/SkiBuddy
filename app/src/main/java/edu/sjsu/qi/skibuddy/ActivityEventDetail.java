package edu.sjsu.qi.skibuddy;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
                    //TODO: put extra for Event Title
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
                if (e == null) {
                    //get event details
                    event = new Event();
                    event = (Event) eventList.get(0);
                    //eventList size is right

                    listUsers = new ArrayList<ParseUser>();
                    listUsers.clear();
                    for (ParseObject event : eventList) {
                        listUsers.add(((Event) event).getAuthor());
                    }
                    //listUsers size is right
                    updateEventsDetail();

                } else {
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
        final ArrayAdapter<ParseUser> adapter = new ArrayAdapter<ParseUser>(getApplicationContext(),
                R.layout.listview_user_item, listUsers){

            @Override
            public View getView(int position, View convertView, ViewGroup parent){

                ViewHolder holder = new ViewHolder();

                if(convertView == null){
                    convertView = getLayoutInflater().inflate(R.layout.listview_user_item, parent, false);
                }
                image = (ImageView)convertView.findViewById(R.id.user_thumbnail);
                image.setTag(listUsers.get(position));
                tvName = (TextView)convertView.findViewById(R.id.user_name);
                user = listUsers.get(position);

                holder.position = position;
                Log.d(TAG, "==== position " + position);

                //Set TextView for name
                String name = "";
                try {
                    name = user.fetchIfNeeded().getString("username");
                } catch (ParseException e) {
                    Log.e(TAG, e.toString());
                }
                tvName.setText(name);

                String url ="";
                try {
                    url = user.fetchIfNeeded().getString("PhotoURL");
                } catch (ParseException e) {
                    Log.e(TAG, e.toString());
                }
                holder.url =url;
                holder.image = image;

                //Set ImageView
                new  DownloadPhotoAsync(position, holder).execute();
                return convertView;
            }
        };

        //Assign adapter to ListView
        lvUsersFound.setAdapter(adapter);

    }

    //New class called ProfilePhotoAsync and make it extend AsyncTask
    //Download profile photo from giving URL.
    class DownloadPhotoAsync extends AsyncTask <Object, String, Bitmap> {

        private int myPosition;
        private ViewHolder myHolder;
        private ImageView myImage;
        private String path;

        public Bitmap bitmap;
        public User myUser = new User();
        public DownloadPhotoAsync(int position, ViewHolder holder){
            myHolder = holder;
            myPosition = position;
            this.path = image.getTag().toString();
        }

        @Override
        protected Bitmap doInBackground(Object... args) {
            // Fetching data from URI and storing in bitmap


            //TODO: some bug here, image download many times

            bitmap = myUser.DownloadImageBitmap(myHolder.url);
            Log.d(TAG, "IMAGE finished download giving URL" + myHolder);
            return  bitmap;
        }
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

          //TODO: have bug here, repeatly show last user photo
          //The following if does not work
          //if (!image.getTag().toString().equals(path)) {
               /* The path is not same. This means that this
                  image view is handled by some other async task.
                  We don't do anything and return. */
           //   return;
          //}
            if(myHolder.position == myPosition){
                myHolder.image.setImageBitmap(bitmap);
            }
        }
    }

    private class ViewHolder{
        public ImageView image;
        public String url;
        public int position;
    }

}
