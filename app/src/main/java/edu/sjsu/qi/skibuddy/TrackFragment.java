package edu.sjsu.qi.skibuddy;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class TrackFragment extends Fragment {

    private final String TAG = TrackFragment.class.getSimpleName();
    private String name = null;
    private String facebookId = null;

    private Button btStart;

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

        //Click button Create to get ActivityTrack
        btStart = (Button)view.findViewById(R.id.button_Start);
        btStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Goto activity ActivityTrack
                try {
                    Intent intent = new Intent(getActivity(), ActivityTrack.class);
//                    intent.putExtra("UserName", name);
//                    intent.putExtra("FacebookId", facebookId);
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
}
