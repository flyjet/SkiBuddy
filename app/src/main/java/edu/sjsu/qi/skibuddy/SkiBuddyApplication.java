package edu.sjsu.qi.skibuddy;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;

/**
 * Created by qi on 11/28/15.
 */
public class SkiBuddyApplication extends Application {

    private final String applicationId = "8AifEDWIz9AqJ8nwAeCIUdw0cNFCIzfHn8MzHcVs";
    private final String clienttKey = "YMn055gl2HyTx6xGeLOZBYXQ31kPspyHmFYoK6d4";

    @Override
    public void onCreate(){
        super.onCreate();

        //Enable local Datastore
        Parse.enableLocalDatastore(this);

        //Subclass ParseObject to create and modify Event Objects
        ParseObject.registerSubclass(Event.class);
        ParseObject.registerSubclass(Track.class);

        ParseObject.registerSubclass(Skiposition.class);
        //Fill in this section with Parse credentials
        Parse.initialize(this, applicationId, clienttKey);

        //Initialize ParseFacebookUtils
        ParseFacebookUtils.initialize(this);


    }
}
