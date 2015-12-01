package edu.sjsu.qi.skibuddy;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


public class ActivityCreateNewEvent extends Activity {

    private static final String TAG = ActivityCreateNewEvent.class.getSimpleName();

    private Event event;
    private ImageButton ibCancel;
    private EditText etTitle, etDescription, etInvite;
    private TextView tvStart, tvEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_event);

        event = new Event();
        etTitle = (EditText)findViewById(R.id.editText_title);
        etDescription = (EditText)findViewById(R.id.editText_des);
        etInvite = (EditText)findViewById(R.id.editText_invite);
        tvStart = (TextView)findViewById(R.id.textView_start);
        tvEnd = (TextView)findViewById(R.id.textView_end);

        tvStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //TODO, implement to pick event start time

                    Toast.makeText(getApplicationContext(),
                            "Start Time clicked", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
            }
        });

        tvEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //TODO, implement to pick event end time

                    Toast.makeText(getApplicationContext(),
                            "End Time clicked", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
            }
        });

        //TODO, implement to invite people

        // Set up the action bar
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);

        //Show the custom ActionBar with the icon_back
        View mActionBarView = getLayoutInflater().inflate(R.layout.action_bar_cancel, null);
        actionBar.setCustomView(mActionBarView);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        ibCancel = (ImageButton) findViewById(R.id.button_cancel);
        ibCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        getMenuInflater().inflate(R.menu.menu_activity_create_new_event, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_Save) {

            //TODO: implement to save all input data to Parse
            Toast.makeText(getApplicationContext(),
                    " Save button clicked", Toast.LENGTH_LONG).show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
