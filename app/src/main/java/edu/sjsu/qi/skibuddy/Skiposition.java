package edu.sjsu.qi.skibuddy;

/**
 * Created by lingqianxie on 12/2/15.
 */

import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/**
 * Data model for a post.
 */
@ParseClassName("Skiposition")
public class Skiposition extends ParseObject {
    public Skiposition(){
        //Default constructor is required
    }
    public String getName() {
        return getString("name");
    }

    public void setName(String value) {
        put("name", value);
    }

    public String getTitleName() {
        return getString("TitleName");
    }

    public void setTitleName(String value) {
        put("TitleName", value);
    }

    public ParseUser getUser() {
        return getParseUser("user");
    }

    public void setUser(ParseUser value) {
        put("user", value);
    }

    public ParseGeoPoint getLocation() {
        return getParseGeoPoint("location");
    }

    public void setLocation(ParseGeoPoint value) {
        put("location", value);
    }

    public static ParseQuery<Skiposition> getQuery() {
        return ParseQuery.getQuery(Skiposition.class);
    }
}
