package edu.sjsu.qi.skibuddy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class EventFragment extends Fragment {

    private final String TAG = EventFragment.class.getSimpleName();

    private ListView eventsFound;

    private String name = null;
    private String facebookId = null;
    private List<Event> listEvents;

    private Button btCreate;



    public EventFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get Current User Name and Id from ActivityFragmentContainer
        name = getArguments().getString("UserName");
        facebookId = getArguments().getString("FacebookId");
        Log.d(TAG, "User Name: " + name + " from EventFragment");
        Log.d(TAG, "User Id: " + facebookId + " from EventFragment");

        listEvents = new ArrayList<Event>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event, container, false);
        eventsFound = (ListView)view.findViewById(R.id.listView_event);

        //TODO: need to query event list from Parse
        // Hardcode for test event list view
        for(int i = 0; i<7; i++){
            Event event = new Event();
            event.setEventId("000" + i);
            event.setEventTitle("Test Event " + i + " for SkiBuddy");
            listEvents.add(event);
        }

        updateEventsList();

        //sets the OnItemClickListener of the ListView
        //so user can click on a event and get event details
        eventsFound.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> av, View v, int pos, long id) {
                Intent intent = new Intent(getActivity(), ActivityEventDetail.class);

                //Todo put Extra of eventID
                intent.putExtra("Event_ID", listEvents.get(pos).getEventId());
                startActivity(intent);

            }
        });


        //Click button Create to get ActivityCreateNewEvent
        btCreate = (Button)view.findViewById(R.id.button_Create);
        btCreate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Goto activity CreateNewEvent
                try {
                    Intent intent = new Intent(getActivity(), ActivityCreateNewEvent.class);
                    intent.putExtra("UserName", name);
                    intent.putExtra("FacebookId", facebookId);
                    startActivity(intent);
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
            }
        });
        return view;
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    //Use ArrayAdapter and pass it to ListView to display event results
    //In the getView method, inflate the listview_event_item.xml layout and update its view
    private void updateEventsList(){

        //create an ArrayAdapter
        ArrayAdapter<Event> adapter = new ArrayAdapter<Event>(getActivity().getApplicationContext(),
                R.layout.listview_event_item, listEvents){

            @Override
            public View getView(int position, View convertView, ViewGroup parent){

                if(convertView == null){
                    convertView = getActivity().getLayoutInflater().inflate(R.layout.listview_event_item, parent, false);
                }

                TextView tvTitle = (TextView)convertView.findViewById(R.id.textView_eventTitle);
                TextView tvStart = (TextView)convertView.findViewById(R.id.textView_start);
                TextView tvEnd = (TextView)convertView.findViewById(R.id.textView_end);

                Event event = listEvents.get(position);

                tvTitle.setText(event.getEventTitle());

                //TODO, need change the testing code

                tvStart.setText("Nov 30, 2015 at 9:00 AM");
                tvEnd.setText("Dec 02, 2015 at 9:00 PM");
                //tvStart.setText(event.getStartTime().toString());
                //tvEnd.setText(event.getEndTime().toString());

                return convertView;
            }
        };

        //Assign adapter to ListView
        eventsFound.setAdapter(adapter);
    }

}
