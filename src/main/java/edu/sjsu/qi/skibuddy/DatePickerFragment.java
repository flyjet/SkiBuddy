package edu.sjsu.qi.skibuddy;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class DatePickerFragment extends DialogFragment  {

    private static final String TAG = DatePickerFragment.class.getSimpleName();
    private Date mDate;
    private int year, month, day, hour,min;
    private String type = null;

    public DatePickerFragment() {
        // Required empty public constructor
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        type = getArguments().get("Type").toString();

        //mDate = (Date)getArguments().getSerializable("EXTRA_DATE");
        mDate = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mDate);
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        min = calendar.get(Calendar.MINUTE);

        View v = getActivity().getLayoutInflater().inflate(R.layout.fragment_date_picker, null);

        DatePicker datePicker = (DatePicker)v.findViewById(R.id.datePicker);
        TimePicker timePicker = (TimePicker)v.findViewById(R.id.timePicker);

        datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            public void onDateChanged(DatePicker view, int year, int month, int day) {
                DatePickerFragment.this.year = year;
                DatePickerFragment.this.month = month;
                DatePickerFragment.this.day = day;
                updateDateTime();
            }
        });

        timePicker.setCurrentHour(hour);
        timePicker.setCurrentMinute(min);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            public void onTimeChanged(TimePicker view, int hour, int min) {
                DatePickerFragment.this.hour = hour;
                DatePickerFragment.this.min = min;
                updateDateTime();
            }
        });


        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle("Select Date and Time")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //sendResult(Activity.RESULT_OK);
                        getActivity().setResult(Activity.RESULT_OK);
                    }
                })
                .create();
    }

    public void updateDateTime() {
        mDate = new GregorianCalendar(year, month, day, hour, min).getTime();

        //Save update Date and Time and passing to Activity CreateNewEvent
        UpdateDateTimeListener activity;
        activity =  (UpdateDateTimeListener) getActivity();
        activity.onFinishDatePickerDialog(type + String.valueOf(mDate));
    }

    public interface UpdateDateTimeListener{
        void onFinishDatePickerDialog(String date);
    }


}
