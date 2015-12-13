package edu.sjsu.qi.skibuddy;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by qi on 12/13/15.
 */

@ParseClassName("Track")
public class Track extends ParseObject {

    public Track(){
        //Default constructor is required
    }

    public String getTrackId() {
        return getString("TrackID");
    }

    public void setTrackId(String id) {
        put("TrackID", id);
    }

    public String getTrackTitle() {
        return getString("TrackTitle");
    }

    public void setTrackTitle(String title) {
        put("TrackTitle", title);
    }

    public ParseUser getAuthor() {
        return getParseUser("author");
    }

    public void setAuthor(ParseUser user) {
        put("author", user);
    }

    public String getTime(){
        return getString("Time");
    }

    public void setTime(String Time){
        put("Time", Time);
    }

    public String getImageURL() {
        return getString("ImageURL");
    }

    public void setImageURL(String imageURL) {
        put("ImageURL", imageURL);
    }

}
