package net.contextfw.demo.web.components;

import net.contextfw.demo.web.service.TweetResult;
import net.contextfw.demo.web.service.TwitterService;
import net.contextfw.web.commons.async.AsyncRunnable;
import net.contextfw.web.commons.async.Function;

public class TwitterFeedUpdateJob extends AsyncRunnable<TwitterFeed> {
    
    
    
    private final TwitterService twitterService;
    private final String search;
    private Long sinceId;
    private final String updater;
    
    public TwitterFeedUpdateJob(TwitterFeed feed, String search,
                                Long sinceId,
                                String updater,
                                TwitterService twitterService) {
        super(feed);
        this.twitterService = twitterService;
        this.search = search;
        this.sinceId = sinceId;
        this.updater = updater;
    }
    
    @Override
    public void run() {
        long maxAge = System.currentTimeMillis() + 30 * 1000;
        boolean isRunning = true;
        while (isRunning) {
            try {
                //Tracker.debug("Search:" + search);
                final TweetResult[] results = new TweetResult[1];
                results[0] = twitterService.searchTweets(search, sinceId);
                AsyncAction action = executeScoped(new Function<TwitterFeed, AsyncAction>() { 
                    public AsyncAction apply(TwitterFeed in) {
                                return in.addTweetsAsync(results[0], search, updater);
                            }});
                //Tracker.debug("AsyncAction: " + action);
                
                if (action == AsyncAction.DIE) {
                    return;
                } else if (maxAge < System.currentTimeMillis() || !results[0].getTweets().isEmpty()) {
                    isRunning = false;
                } else {
                    //Tracker.debug("Sleeping: "+ search);
                    sinceId = results[0].getSinceId();
                     Thread.sleep(5000);
                }
                //Tracker.debug("request refres");
            } catch (InterruptedException e) {
                // Ignore
            } catch (Exception e) {
                e.printStackTrace();
                isRunning = false;
            }
        }
        //Tracker.debug("request refres");
        requestRefresh();
    }
}
