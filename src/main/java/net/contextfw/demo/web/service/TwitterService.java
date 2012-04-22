package net.contextfw.demo.web.service;

import java.util.UUID;

import net.contextfw.demo.services.CommonService;
import net.contextfw.demo.web.components.TwitterFeed;
import net.contextfw.demo.web.components.TwitterFeedUpdateJob;
import net.contextfw.web.application.WebApplicationException;
import twitter4j.Query;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class TwitterService {

    private Twitter twitter = new TwitterFactory().getInstance();
    
    private final CommonService commonService;
    
    @Inject
    public TwitterService(CommonService commonService) {
        this.commonService = commonService;
    }

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
    
    public String createUpdateJob(TwitterFeed feed, String search, Long sinceId, boolean immediately) {
        String updater = UUID.randomUUID().toString();
        commonService.executeAsync(
                new TwitterFeedUpdateJob(feed, search, sinceId, updater, this),
                immediately ? 0 : 5000);
        return updater;
    }
}
