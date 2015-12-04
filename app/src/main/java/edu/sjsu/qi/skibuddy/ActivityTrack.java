package edu.sjsu.qi.skibuddy;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class ActivityTrack extends Activity {

    private static final String TAG = ActivityTrack.class.getSimpleName();

    // Google Map
    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);

        try {
            // Loading map
            initilizeMap();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * function to load map. If map is not created it will create it for you
     * */
    private void initilizeMap() {
        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.map_track_fragment)).getMap();

            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
        }

        //Todo: set event location from Parse
        LatLng heavenly = new LatLng(38.9365091, -119.9025936);
        System.out.println("heavenly ===> " + heavenly);
        System.out.println("googleMap ===> " + googleMap);

        googleMap.setMyLocationEnabled(true);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(heavenly, 13));
        googleMap.addMarker(new MarkerOptions()
                .title("Ski Event #277")
                .snippet("Lake Tahoe Ski Event at Heavenly")
                .position(heavenly));

        drawTracking();
    }

    /**
     * function to draw tracking lines on map
     * Todo: ready players' position records to draw lines
     * */
    private void drawTracking() {

        //Player A
        LatLng pos_A = new LatLng(38.935728, -119.894832);
        googleMap.addMarker(new MarkerOptions()
                .title("Player A")
                .position(pos_A)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));

        Polyline line_A_1 = googleMap.addPolyline(new PolylineOptions()
                .add(pos_A, new LatLng(38.934119, -119.895521), new LatLng(38.932321, -119.897386))
                .width(10)
                .color(Color.YELLOW));

        Polyline line_A_2 = googleMap.addPolyline(new PolylineOptions()
                .add(new LatLng(38.931693, -119.891717), new LatLng(38.930440, -119.893258), new LatLng(38.927198, -119.896935), new LatLng(38.926393, -119.898425))
                .width(10)
                .color(Color.YELLOW));

        //Player A
        LatLng pos_B = new LatLng(38.939213, -119.899096);
        googleMap.addMarker(new MarkerOptions()
                .title("Player B")
                .position(pos_B)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

        Polyline line_B_1 = googleMap.addPolyline(new PolylineOptions()
                .add(pos_B, new LatLng(38.937680, -119.899809), new LatLng(38.934842, -119.897796))
                .width(10)
                .color(Color.GREEN));

        Polyline line_B_2 = googleMap.addPolyline(new PolylineOptions()
                .add(new LatLng(38.937021, -119.899454), new LatLng(38.932952, -119.902597), new LatLng(38.931501, -119.902901))
                .width(10)
                .color(Color.GREEN));

        Polyline line_B_3 = googleMap.addPolyline(new PolylineOptions()
                .add(new LatLng(38.935266, -119.895657), new LatLng(38.933603, -119.895196), new LatLng(38.930993, -119.895238), new LatLng(38.928579, -119.891715))
                .width(10)
                .color(Color.GREEN));
    }

    @Override
    protected void onResume() {
        super.onResume();
        initilizeMap();
    }

}