package edu.sjsu.qi.skibuddy;

import java.util.Calendar;

/**
 * Created by qi on 11/20/15.
 */
public class ItemEvent {
    private String id;
    private String eventTitle;
    private String description;
    private Calendar startTime;
    private Calendar endTime;

    public String getId() {
        return id;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public String getDescription() {
        return description;
    }

    public Calendar getStartTime() {
        return startTime;
    }

    public Calendar getEndTime() {
        return endTime;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStartTime(Calendar startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(Calendar endTime) {
        this.endTime = endTime;
    }


}

