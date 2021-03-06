package edu.sjsu.qi.skibuddy;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
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


public class TrackFragment extends Fragment {
    private final String TAG = TrackFragment.class.getSimpleName();

    private String name = null;
    private String facebookId = null;
    private Button btStart;

    private ListView tracksFound;
    private List<Track> listTracks = new ArrayList<>();

    public TrackFragment() {
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
        View view = inflater.inflate(R.layout.fragment_track, container, false);
        tracksFound = (ListView)view.findViewById(R.id.listView_track);
        queryTrackListFromParse();


        //sets the OnItemClickListener of the ListView
        //so user can click on a event and get Track details
        tracksFound.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> av, View v, int pos, long id) {
                Intent intent = new Intent(getActivity(), ActivityTrackDetail.class);
                intent.putExtra("Track_Title", listTracks.get(pos).getTrackTitle());
                startActivity(intent);
            }
        });


        //Click button Create to get ActivityTrack
        btStart = (Button)view.findViewById(R.id.button_Start);
        btStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Goto activity ActivityTrack
                try {
                    Intent intent = new Intent(getActivity(), ActivityTrack.class);
                    //intent.putExtra("UserName", name);
                    //intent.putExtra("FacebookId", facebookId);
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


    //query Events for current user
    private void queryTrackListFromParse(){

        //Create query for objects of type "Track"
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Track");
        // Restrict to cases where the author is the current user.
        //pass in a ParseUser and not String of that user

        query.whereEqualTo("author", ParseUser.getCurrentUser());
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
        ArrayAdapter<Track> adapter = new ArrayAdapter<Track>(getActivity().getApplicationContext(),
                R.layout.listview_track_item, listTracks){

            @Override
            public View getView(int position, View convertView, ViewGroup parent){

                if(convertView == null){
                    convertView = getActivity().getLayoutInflater().inflate(R.layout.listview_track_item, parent, false);
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
