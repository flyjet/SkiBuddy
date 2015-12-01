package edu.sjsu.qi.skibuddy;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by qi on 11/20/15.
 */

@ParseClassName("Event")

public class Event extends ParseObject {

    public Event(){
        //Default constructor is required
    }

    public String getEventId() {
        return getString("EventID");
    }

    public void setEventId(String id) {
        put("EventID", id);
    }

    public String getEventTitle() {
        return getString("EventTitle");
    }

    public void setEventTitle(String title) {
        put("EventTitle", title);
    }

    public String getDescription() {
        return getString("Description");
    }

    public void setDescription(String description) {
        put("Description", description);
    }

    public ParseUser getAuthor() {
        return getParseUser("author");
    }

    public void setAuthor(ParseUser user) {
        put("author", user);
    }

    public Date getStartTime(){
        return getDate("StartTime");
    }

    public void setStartTime(Date startTime){
        put("StartTime", startTime);
    }

    public Date getEndTime(){
        return getDate("EndTime");
    }

    public void setEndTime(Date endTime){
        put("EndTime", endTime);
    }

}

