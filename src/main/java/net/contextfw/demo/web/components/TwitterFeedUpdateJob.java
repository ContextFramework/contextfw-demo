package net.contextfw.demo.web.components;

import net.contextfw.demo.web.service.TweetResult;
import net.contextfw.demo.web.service.TwitterService;
import net.contextfw.web.application.component.ComponentRegister;
import net.contextfw.web.application.scope.Execution;
import net.contextfw.web.application.scope.PageScopedExecutor;

import com.google.inject.Provider;

public class TwitterFeedUpdateJob implements Execution {
    
    private final TwitterService twitterService;
    
    private final Provider<ComponentRegister> componentRegister;
    private final String id;
    private final String search;
    private final Long sinceId;
    
    public TwitterFeedUpdateJob(String id, 
                                String search,
                                Long sinceId,
                                Provider<ComponentRegister> componentRegister,
                                TwitterService twitterService) {
        this.id = id;
        this.componentRegister = componentRegister;
        this.twitterService = twitterService;
        this.search = search;
        this.sinceId = sinceId;
    }
    
    @Override
    public void execute(PageScopedExecutor executor) {
        final TweetResult[] results = new TweetResult[1];
        try {
            results[0] = twitterService.searchTweets(search, sinceId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        executor.execute(new Runnable() {
            public void run() {
                componentRegister.get()
                    .findComponent(TwitterFeed.class, id)
                    .addTweets(results[0], search);
            }
        });
    }
}
