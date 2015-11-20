package edu.sjsu.qi.skibuddy;

/**
 * Created by qi on 11/20/15.
 */
public class ItemUser {
    private String userId;
    private String userName;
    private String thumbnailName;
    private String Tagline;

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return userName;
    }

    public String getTagline() {
        return Tagline;
    }

    public String getThumbnailName() {
        return thumbnailName;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setName(String name) {
        this.userName = name;
    }

    public void setThumbnailName(String thumbnailName) {
        this.thumbnailName = thumbnailName;
    }

    public void setTagline(String tagline) {
        Tagline = tagline;
    }
}
