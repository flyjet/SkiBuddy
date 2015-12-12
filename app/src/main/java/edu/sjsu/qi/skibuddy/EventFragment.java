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

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class EventFragment extends Fragment {

    private final String TAG = EventFragment.class.getSimpleName();

    private ListView eventsFound;

    private String name = null;
    private String facebookId = null;
    private List<Event> listEvents=new ArrayList<>();

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event, container, false);
        eventsFound = (ListView)view.findViewById(R.id.listView_event);
        queryEventListFromParse();

        //sets the OnItemClickListener of the ListView
        //so user can click on a event and get event details
        eventsFound.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> av, View v, int pos, long id) {
                Intent intent = new Intent(getActivity(), ActivityEventDetail.class);
                intent.putExtra("Event_Title", listEvents.get(pos).getEventTitle());
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

    @Override
    public void onResume(){
        super.onResume();
        queryEventListFromParse();
    }

    //query Events for current user
    private void queryEventListFromParse(){

        //Create query for objects of type "Event"
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Event");
        // Restrict to cases where the author is the current user.
        //pass in a ParseUser and not String of that user

        query.whereEqualTo("author", ParseUser.getCurrentUser());
        query.orderByAscending("createAt");

        // Run the query
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> eventList, ParseException e) {
                if(e == null){
                    // If there are results, update the list of event and notify the adapter
                    listEvents.clear();
                    for(ParseObject event : eventList){
                        listEvents.add((Event)event);
                    }

                    updateEventsList();

                }else{
                    Log.d(TAG, "Event retrieval error: " + e.getMessage());
                }
            }
        });
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
                tvStart.setText(event.getStartTime());
                tvEnd.setText(event.getEndTime());

                return convertView;
            }
        };

        //Assign adapter to ListView
        eventsFound.setAdapter(adapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            // If a new event has been added, update the list of events
            queryEventListFromParse();
        }
    }

}
