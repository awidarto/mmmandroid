package com.kickstartlab.android.mmm.events;


import com.kickstartlab.android.mmm.rest.models.Feed;
import com.kickstartlab.android.mmm.rest.models.Location;

/**
 * Created by awidarto on 12/3/14.
 */
public class FeedEvent {

    private String action = "refresh";

    private Feed feed;

    public FeedEvent(String action){
        this.action = action;
    }

    public FeedEvent(String action, Feed feed){
        this.action = action;
        this.feed = feed;
    }

    public Feed getFeed() {
        return feed;
    }

    public void setFeed(Feed feed) {
        this.feed = feed;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
