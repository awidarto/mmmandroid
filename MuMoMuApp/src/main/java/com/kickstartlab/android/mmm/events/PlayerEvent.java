package com.kickstartlab.android.mmm.events;


import com.kickstartlab.android.mmm.rest.models.Feed;

/**
 * Created by awidarto on 12/3/14.
 */
public class PlayerEvent {

    public static final String BUFFERING_COMPLETE = "bufferingComplete";
    public static final String BUFFERING_UPDATE = "bufferingUpdate";
    public static final String BUFFERING = "buffering";
    public static final String PLAY = "play";
    public static final String STOP = "stop";
    public static final String PREVIOUS = "previous";
    public static final String NEXT = "next";


    private String action = "refresh";

    private Feed feed;

    private int progress;

    public PlayerEvent(String action){
        this.action = action;
    }

    public PlayerEvent(String action, int progress){
        this.progress = progress;
        this.action = action;
    }


    public PlayerEvent(String action, Feed feed){
        this.action = action;
        this.feed = feed;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
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
