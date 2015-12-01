package edu.sjsu.qi.skibuddy;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


public class ActivityEventDetail extends Activity {

    private static final String TAG = ActivityEventDetail.class.getSimpleName();

    private String eventId = null;
    private ImageButton ibBack;
    private Button btMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get Event ID from EventFragment
        Bundle extras = getIntent().getExtras();
        eventId = extras.getString("Event_ID");


        //TODO query event detail from Parse by Event_ID


        setContentView(R.layout.activity_event_detail);

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
                    //TODO: put extra
                    startActivity(intent);

                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
            }
        });

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
}
