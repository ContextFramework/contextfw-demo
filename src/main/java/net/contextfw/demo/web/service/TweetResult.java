package net.contextfw.demo.web.service;

import java.util.ArrayList;
import java.util.List;

import net.contextfw.demo.web.components.Tweet;
import twitter4j.QueryResult;

public class TweetResult {

    private final long sinceId;
    
    private final List<Tweet> tweets;
    
    public TweetResult(QueryResult result) {
        this.sinceId = result.getMaxId();
        tweets = new ArrayList<Tweet>();
        for (twitter4j.Tweet tweet : result.getTweets()) {
            tweets.add(new Tweet(tweet));
        }
    }

    public long getSinceId() {
        return sinceId;
    }

    public List<Tweet> getTweets() {
        return tweets;
    }
}
