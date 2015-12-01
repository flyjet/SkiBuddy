package edu.sjsu.qi.skibuddy;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Date;


public class ActivityCreateNewEvent extends Activity implements DatePickerFragment.UpdateDateTimeListener {

    private static final String TAG = ActivityCreateNewEvent.class.getSimpleName();

    private Event event;

    private DatePickerFragment myDatePickerDialogStart;
    private DatePickerFragment myDatePickerDialogEnd;

    private DatePicker datePicker;
    private ImageButton ibCancel;
    private EditText etTitle, etDescription;
    private TextView tvStart, tvEnd,tvInvite;
    private Date currentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_event);

        event = new Event();

        //Set up default value for event start and end date time
        currentDate = new Date();
        Date nextDate = new Date();
        nextDate.setDate(currentDate.getDate()+1);
        String current = String.valueOf(currentDate);
        String next = String.valueOf(nextDate);
        String start = current.substring(0,10) + ", " + current.substring(24,28) + " at " +  current.substring(11, 16);
        String end = next.substring(0,10) + ", " + next.substring(24,28) + " at " +  next.substring(11, 16);

        myDatePickerDialogStart = new DatePickerFragment();
        myDatePickerDialogEnd = new DatePickerFragment();

        etTitle = (EditText)findViewById(R.id.editText_title);
        etDescription = (EditText)findViewById(R.id.editText_des);
        tvInvite = (TextView)findViewById(R.id.textView_invite);
        tvStart = (TextView)findViewById(R.id.textView_start);
        tvEnd = (TextView)findViewById(R.id.textView_end);

        tvStart.setText(start);
        tvEnd.setText(end);

        tvStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //Show dialog for pick date and time
                    Bundle args = new Bundle();
                    args.putString("Type", "Start");
                    myDatePickerDialogStart.setArguments(args);
                    myDatePickerDialogStart.show(getFragmentManager(), "Start");
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
            }
        });


        tvEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Bundle args = new Bundle();
                    args.putString("Type", "End  ");
                    myDatePickerDialogEnd.setArguments(args);
                    myDatePickerDialogEnd.show(getFragmentManager(), "End");
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
    public void onFinishDatePickerDialog(String date) {

        //Update Date and Time from picker
        String type = date.substring(0,5);
        String changedDate = date.substring(5,15)+", " + date.substring(29,33) + " at " +  date.substring(16, 21);
        if(type.equals("Start") ){
            tvStart.setText(changedDate);

        }else if(type.equals("End  ") ){
            tvEnd.setText(changedDate);
        }
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