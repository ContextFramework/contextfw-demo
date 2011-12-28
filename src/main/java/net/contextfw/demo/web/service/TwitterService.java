package net.contextfw.demo.web.service;

import net.contextfw.web.application.WebApplicationException;
import twitter4j.Query;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

import com.google.inject.Singleton;

@Singleton
public class TwitterService {

    private Twitter twitter = new TwitterFactory().getInstance();
    
    public TweetResult searchTweets(String search, Long sinceId) {
        try {
            Query query = new Query(search);
            if (sinceId != null) {
                query.setSinceId(sinceId);
            }
            return new TweetResult(twitter.search(query));
        } catch (TwitterException e) {
            throw new WebApplicationException(e);
        }
    }
}
